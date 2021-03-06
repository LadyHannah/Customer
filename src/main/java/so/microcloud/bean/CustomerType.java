package so.microcloud.bean;

import java.io.Serializable;
import java.util.Date;

public class CustomerType implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_customer_type.id
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_customer_type.type_name
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    private String typeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_customer_type.create_user
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    private Integer createUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_customer_type.create_time
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_customer_type.state
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    private Integer state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_customer_type
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_customer_type.id
     *
     * @return the value of t_customer_type.id
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_customer_type.id
     *
     * @param id the value for t_customer_type.id
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_customer_type.type_name
     *
     * @return the value of t_customer_type.type_name
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_customer_type.type_name
     *
     * @param typeName the value for t_customer_type.type_name
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_customer_type.create_user
     *
     * @return the value of t_customer_type.create_user
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_customer_type.create_user
     *
     * @param createUser the value for t_customer_type.create_user
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_customer_type.create_time
     *
     * @return the value of t_customer_type.create_time
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_customer_type.create_time
     *
     * @param createTime the value for t_customer_type.create_time
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_customer_type.state
     *
     * @return the value of t_customer_type.state
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_customer_type.state
     *
     * @param state the value for t_customer_type.state
     *
     * @mbg.generated Sun Apr 12 00:01:45 CST 2020
     */
    public void setState(Integer state) {
        this.state = state;
    }
}