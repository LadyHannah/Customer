package so.microcloud.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import so.microcloud.bean.CustomerContact;
import so.microcloud.bean.CustomerContactExample;
import so.microcloud.common.PageInfo;

public interface CustomerContactMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	long countByExample(CustomerContactExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int deleteByExample(CustomerContactExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int insert(CustomerContact record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int insertSelective(CustomerContact record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	List<CustomerContact> selectByExample(CustomerContactExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	CustomerContact selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int updateByExampleSelective(@Param("record") CustomerContact record,
			@Param("example") CustomerContactExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int updateByExample(@Param("record") CustomerContact record, @Param("example") CustomerContactExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int updateByPrimaryKeySelective(CustomerContact record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table t_customer_contact
	 * @mbg.generated  Sat Apr 25 12:40:17 CST 2020
	 */
	int updateByPrimaryKey(CustomerContact record);

	List<CustomerContact> getCustomerContactList(@Param("customerId")Integer customerId, @Param("keyword")String keyword,
    		@Param("pageInfo")PageInfo pageInfo);
    
    List<CustomerContact> getContactList(@Param("customerId")Integer customerId);
}