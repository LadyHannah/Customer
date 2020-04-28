package so.microcloud.common;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集成了分页功能的MyBatis拦截器插件。结合PageInfo类实现MyBatis查询的自动分页。
 * 可通过以下三种方法传递分页信息：
 * <li>Mapper方法仅有一个参数，且该参数为PageInfo类型（或子类型）的实例</li>
 * <li>Mapper方法仅有一个参数，且该参数对象包含了一个PageInfo类型的属性，且属性名称为“pageInfo”</li>
 * <li>Mapper方法有多个参数，其中一个参数是PageInfo类型，且参数名称为“pageInfo”</li>
 * <br>
 *
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	final static Logger logger = LoggerFactory.getLogger(PageInterceptor.class);
   
	private static final String DEFAULT_DIALECT = "mysql";
	private static String dialect = DEFAULT_DIALECT;
	public static final String PAGE_INFO_KEY = "pageInfo";
	

	@SuppressWarnings("unchecked")
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
			BaseStatementHandler delegate =  ReflectUtil.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = ReflectUtil.getValueByFieldName(delegate, "mappedStatement");
			BoundSql boundSql = delegate.getBoundSql();
			Object parameterObject = boundSql.getParameterObject();
			if (this.isNeedPaging(parameterObject)) {
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject error");
				} else {
					Connection connection = (Connection) invocation.getArgs()[0];
					String sql = boundSql.getSql();
					int orderByIdx = sql.lastIndexOf("order by");
					if (orderByIdx != -1)
						sql = sql.substring(0, orderByIdx);
					String countSql = "select count(*) from (" + sql + ") myCount";
					logger.debug("总数sql 语句:" + countSql);
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
//					boundSql.
					setParameters(countStmt, mappedStatement, countBS, parameterObject, boundSql);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();

					PageInfo pageInfo = null;
					if (parameterObject instanceof PageInfo) {
						pageInfo = (PageInfo) parameterObject;
						pageInfo.setTotalCount(count);
					} else if (parameterObject instanceof Map) {
						Map<String, Object> map = (Map<String, Object>) parameterObject;
						pageInfo = (PageInfo) map.get(PAGE_INFO_KEY);
						if (pageInfo == null)
							pageInfo = new PageInfo();
						pageInfo.setTotalCount(count);
					} else {
						Field pageField = ReflectUtil.getFieldByFieldName(parameterObject, PAGE_INFO_KEY);
						if (pageField != null) {
							pageInfo = (PageInfo) ReflectUtil.getValueByFieldName(parameterObject, PAGE_INFO_KEY);
							if (pageInfo == null)
								pageInfo = new PageInfo();
							pageInfo.setTotalCount(count);
							ReflectUtil.setValueByFieldName(parameterObject, PAGE_INFO_KEY, pageInfo);
						} else {
							throw new NoSuchFieldException(parameterObject.getClass().getName());
						}
					}
					String pageSql = generatePageSql(boundSql.getSql(), pageInfo);
					logger.debug("page sql:" + pageSql);
					ReflectUtil.setValueByFieldName(boundSql, "sql", pageSql);
				}
			}
		}
		return invocation.proceed();
	}

	private boolean isNeedPaging(Object parameterObject) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		return parameterObject instanceof PageInfo && ((PageInfo)parameterObject).getCurrentPage()>0
				|| parameterObject instanceof Map && ((Map<?, ?>) parameterObject).containsKey(PAGE_INFO_KEY)
					&& ((PageInfo)((Map<?, ?>) parameterObject).get(PAGE_INFO_KEY)).getCurrentPage()>0
				|| ReflectUtil.getFieldByFieldName(parameterObject, PAGE_INFO_KEY) != null 
					&& ReflectUtil.getValueByFieldName(parameterObject, PAGE_INFO_KEY)!=null
					&&	((PageInfo)ReflectUtil.getValueByFieldName(parameterObject, PAGE_INFO_KEY)).getCurrentPage()>0 ;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql countSql, Object parameterObject, BoundSql boundSql) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = countSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value = null;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					}else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						value = metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
					}
					JdbcType jdbcType = parameterMapping.getJdbcType();
					if (value == null && jdbcType == null)
						jdbcType = configuration.getJdbcTypeForNull();
					typeHandler.setParameter(ps, i + 1, value, jdbcType);
				}
			}
		}
	}

	private String generatePageSql(String sql, PageInfo page) {
		if (page != null) {
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + page.getRowOffset() + "," + page.getPageSize());
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				/* TODO: to be optimized */
				pageSql.append(sql);
				pageSql.append(")  tmp_tb where ROWNUM<=");
				pageSql.append(page.getRowOffset() + page.getPageSize());
				pageSql.append(") where row_id>");
				pageSql.append(page.getRowOffset());
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		if (StringUtils.isNotEmpty(p.getProperty("dialect")))
			dialect = p.getProperty("dialect");
	}

}