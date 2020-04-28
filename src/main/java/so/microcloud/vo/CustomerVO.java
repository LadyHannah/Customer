package so.microcloud.vo;

import java.util.List;

public class CustomerVO {

	private Integer id;
	
	private String shopName;
	
	private String customerName;
	
	private String phone;
	
	private String content;
	
	private Integer typeId;
	
	private String typeName;
	
	private String product;
	
	private Integer isPublic;
	
	private String claimUserName;
	
	private String claimTime;
	
	private String createUserName;
	
	private String createTime;
	
	private List<CustomerContactVO> contactList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public String getClaimUserName() {
		return claimUserName;
	}

	public void setClaimUserName(String claimUserName) {
		this.claimUserName = claimUserName;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<CustomerContactVO> getContactList() {
		return contactList;
	}

	public void setContactList(List<CustomerContactVO> contactList) {
		this.contactList = contactList;
	}

}
