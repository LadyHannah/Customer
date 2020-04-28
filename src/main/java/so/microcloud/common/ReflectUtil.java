package so.microcloud.common;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * java反射工具类
 *
 */
public class ReflectUtil {
	
	/**
	 * 获取指定名称对象域，如果当前对象不存在此域，递归查找父类对象
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) throws NoSuchFieldException {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取对象指定域的值，如果当前对象不存在此域，递归查找父类对象
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueByFieldName(Object obj, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		Field field = getFieldByFieldName(obj, fieldName);
		T value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = (T) field.get(obj);
			} else {
				field.setAccessible(true);
				value = (T) field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置对象指定域的值
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj,fieldName);
		Class<?>  filedType = field.getType();
		Class<?> valueType = null;
		if(value!=null)
			valueType = value.getClass();
		boolean isAccessible = field.isAccessible();
		if(!isAccessible)
			field.setAccessible(true);
		try{
			if(value!=null && String.class.equals(valueType)){
				if(filedType.isAssignableFrom(Byte.class) || filedType.isAssignableFrom(byte.class))
					field.set(obj, Byte.valueOf((String)value));
				else if(filedType.isAssignableFrom(Short.class) || filedType.isAssignableFrom(short.class))
					field.set(obj, Short.valueOf((String)value));
				else if(filedType.isAssignableFrom(Integer.class) || filedType.isAssignableFrom(int.class))
					field.set(obj, Integer.valueOf((String)value));
				else if(filedType.isAssignableFrom(Float.class) || filedType.isAssignableFrom(float.class))
					field.set(obj, Float.valueOf((String)value));
				else if(filedType.isAssignableFrom(Double.class) || filedType.isAssignableFrom(double.class))
					field.set(obj, Double.valueOf((String)value));
				else if(filedType.isAssignableFrom(Long.class) || filedType.isAssignableFrom(long.class))
					field.set(obj, Long.valueOf((String)value));
				else if(filedType.isAssignableFrom(BigInteger.class))
					field.set(obj, new BigInteger((String)value));
				else if(filedType.isAssignableFrom(BigDecimal.class))
					field.set(obj, new BigDecimal((String)value));
				else if(filedType.isAssignableFrom(Date.class))
					field.set(obj, DateUtil.parseDate((String)value));
				else if(filedType.isAssignableFrom(Calendar.class)){
					Calendar v= Calendar.getInstance();
					v.setTime(DateUtil.parseDate((String)value));
					field.set(obj, v);
				}
				else if(filedType.equals(String.class))
					field.set(obj, value);
			}
			else
				field.set(obj, value);
		}
		finally{
			if(!isAccessible)
				field.setAccessible(false);
		}
	}
	
	
	/**
	 * 初始化一个对象的数字包装类型（如：Integer、Long等）字段为对应原生类型缺省值0，防止后面出现空指针。
	 * @param obj
	 */
	public static void initNumberFields(Object obj){
		if(obj!=null){
			Field[] fields = obj.getClass().getDeclaredFields();
			for(Field field: fields){
				boolean isAccessible = field.isAccessible();
				if(!isAccessible)
					field.setAccessible(true);
				try {
					Object value = field.get(obj);
					if(value!=null)
						continue;
					Class<?> filedType = field.getType();
					if(filedType.isAssignableFrom(Byte.class))
						field.set(obj, (byte)0);
					else if(filedType.isAssignableFrom(Short.class))
						field.set(obj, (short)0);
					else if(filedType.isAssignableFrom(Integer.class))
						field.set(obj, 0);
					else if(filedType.isAssignableFrom(Float.class))
						field.set(obj, 0F);
					else if(filedType.isAssignableFrom(Double.class))
						field.set(obj, 0D);
					else if(filedType.isAssignableFrom(Long.class))
						field.set(obj, 0L);
					else if(filedType.equals(BigInteger.class))
						field.set(obj, BigInteger.ZERO);
					else if(filedType.equals(BigDecimal.class))
						field.set(obj, BigDecimal.ZERO);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				finally{
					if(!isAccessible)
						field.setAccessible(false);
				}
			}
		}
	}
	
	/**
	 * 抽取一个迭代器中各元素的指定字段值到一个集合中
	 * @param i
	 * @param filedName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static <E, T> Set<T> gatherSet(Iterator<E> i, String filedName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		Set<T> set = new HashSet<T>();
		while(i.hasNext()){
			E e = i.next();
			T value = getValueByFieldName(e, filedName);
			set.add(value);
		}
		return set;
	}

}
