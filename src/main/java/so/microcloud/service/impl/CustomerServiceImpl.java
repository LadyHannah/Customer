package so.microcloud.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import so.microcloud.bean.Customer;
import so.microcloud.bean.CustomerContact;
import so.microcloud.bean.CustomerExample;
import so.microcloud.bean.User;
import so.microcloud.common.DateUtil;
import so.microcloud.common.PageInfo;
import so.microcloud.dao.CustomerContactMapper;
import so.microcloud.dao.CustomerMapper;
import so.microcloud.dao.UserMapper;
import so.microcloud.service.CustomerContactService;
import so.microcloud.service.CustomerService;
import so.microcloud.vo.CustomerContactVO;
import so.microcloud.vo.CustomerVO;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private CustomerContactMapper contactMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Resource(name = "customerContactServiceImpl")
	private CustomerContactService contactService;
	
	@Override
	public List<CustomerVO> getPublicCustomerList(Integer typeId, String keyword, User user, PageInfo pageInfo) {
		List<CustomerVO> voList = new ArrayList<CustomerVO>();
		List<Customer> list = customerMapper.getPublicCustomerList(typeId, keyword, user, pageInfo);
		if (list != null && list.size() > 0) {
			for (Customer customer : list) {
				CustomerVO vo = convertToCustomerVO(customer);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	@Override
	public List<CustomerVO> getCustomerList(Integer typeId, String keyword, User user, PageInfo pageInfo) {
		List<CustomerVO> voList = new ArrayList<CustomerVO>();
		List<Customer> list = customerMapper.getCustomerList(typeId, keyword, user, pageInfo);
		if (list != null && list.size() > 0) {
			for (Customer customer : list) {
				CustomerVO vo = convertToCustomerVO(customer);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	@Override
	public CustomerVO getCustomerById(Integer id) {
		CustomerVO vo = new CustomerVO();
		Customer customer = customerMapper.selectByPrimaryKey(id);
		if (customer != null) {
			List<CustomerContactVO> contactList = contactService.getCustomerContactList(id);
			vo.setShopName(StringUtils.isBlank(customer.getShopName()) ? "" : customer.getShopName());
			vo.setCustomerName(StringUtils.isBlank(customer.getCustomerName()) ? "" : customer.getCustomerName());
			vo.setPhone(StringUtils.isBlank(customer.getPhone()) ? "" : customer.getPhone());
			vo.setTypeId(customer.getTypeId());
			vo.setTypeName(customer.getTypeName());
			vo.setProduct(StringUtils.isBlank(customer.getProduct()) ? "" : customer.getProduct());
			vo.setIsPublic(customer.getIsPublic());
			vo.setContactList(contactList);
		}
		return vo;
	}

	private CustomerVO convertToCustomerVO(Customer customer) {
		CustomerVO vo = new CustomerVO();
		vo.setId(customer.getId());
		vo.setShopName(StringUtils.isBlank(customer.getShopName()) ? "" : customer.getShopName());
		vo.setCustomerName(StringUtils.isBlank(customer.getCustomerName()) ? "" : customer.getCustomerName());
		vo.setPhone(StringUtils.isBlank(customer.getPhone()) ? "" : customer.getPhone());
		vo.setTypeId(customer.getTypeId());
		vo.setTypeName(customer.getTypeName());
		vo.setProduct(StringUtils.isBlank(customer.getProduct()) ? "" : customer.getProduct());
		vo.setIsPublic(customer.getIsPublic());
		vo.setClaimUserName(customer.getClaimUserName());
		vo.setClaimTime(DateUtil.getFormattedDate(customer.getClaimTime(), DateUtil.noSecondFormat));
		vo.setCreateUserName(customer.getCreateUserName());
		vo.setCreateTime(DateUtil.getFormattedDate(customer.getCreateTime(), DateUtil.noSecondFormat));
		return vo;
	}
	
	@Override
	public long getMyCustomerCount(Integer userId) {
		CustomerExample example = new CustomerExample();
		example.createCriteria().andStateEqualTo(1).andClaimUserEqualTo(userId);
		return customerMapper.countByExample(example);
	}
	
	@Override
	@Transactional(rollbackOn=Exception.class)
	public int save(CustomerVO customerVO, String content, User user) {
		if (customerVO.getId() == null) {
			// 手机号码为客户唯一识别字段，查询此手机号是否已存在
			CustomerExample example = new CustomerExample();
			example.createCriteria().andPhoneEqualTo(customerVO.getPhone()).andStateEqualTo(1);
			long count = customerMapper.countByExample(example);
			if (count > 0) {
				return 2; // 手机号已存在
			}
			Customer customer = new Customer();
			customer.setShopName(customerVO.getShopName());
			customer.setCustomerName(customerVO.getCustomerName());
			customer.setPhone(customerVO.getPhone());
			customer.setTypeId(customerVO.getTypeId());
			customer.setProduct(customerVO.getProduct());
			customer.setIsPublic(0);
			customer.setClaimUser(user.getId());
			customer.setCreateUser(user.getId());
			customer.setCreateTime(new Date());
			customer.setState(1);
			customerMapper.insertSelective(customer);
			
			if (StringUtils.isNotBlank(content)) {
				CustomerContact contact = new CustomerContact();
				contact.setCustomerId(customer.getId());
				contact.setContent(content);
				contact.setTypeId(customerVO.getTypeId());
				contact.setCreateUser(user.getId());
				contact.setCreateTime(new Date());
				contact.setLastModifyTime(new Date());
				contact.setState(1);
				contactMapper.insert(contact);
			}
			return 1;
		}
		return 0;
	}
	
	@Override
	public int modify(CustomerVO customerVO, String content, User user) {
		if (customerVO.getId() != null) {
			Customer customer = customerMapper.selectByPrimaryKey(customerVO.getId());
			// 手机号若有修改，则需判断数据库中已存在
			if (StringUtils.isNotBlank(customer.getPhone()) && !customerVO.getPhone().equals(customer.getPhone())) {
				CustomerExample example = new CustomerExample();
				example.createCriteria().andPhoneEqualTo(customerVO.getPhone()).andStateEqualTo(1);
				long count = customerMapper.countByExample(example);
				if (count > 0) {
					return 2; // 手机号已存在
				}
			}
			customer.setShopName(customerVO.getShopName());
			customer.setCustomerName(customerVO.getCustomerName());
			customer.setPhone(customerVO.getPhone());
			customer.setTypeId(customerVO.getTypeId());
			customer.setProduct(customerVO.getProduct());
			customerMapper.updateByPrimaryKeySelective(customer);
			
			if (StringUtils.isNotBlank(content)) {
				CustomerContact contact = new CustomerContact();
				contact.setCustomerId(customer.getId());
				contact.setContent(content);
				contact.setTypeId(customerVO.getTypeId());
				contact.setCreateUser(user.getId());
				contact.setCreateTime(new Date());
				contact.setLastModifyTime(new Date());
				contact.setState(1);
				contactMapper.insert(contact);
			}
			return 1;
		}
		return 0;
	}

	@Override
	public int delete(Integer id) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setState(0);
		return customerMapper.updateByPrimaryKeySelective(customer);
	}

	@Override
	public int drop(Integer id) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setIsPublic(1);
		return customerMapper.updateByPrimaryKeySelective(customer);
	}

	@Override
	public int getBack(Integer id, User user) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setIsPublic(0);
		customer.setClaimUser(user.getId());
		customer.setClaimTime(new Date());
		return customerMapper.updateByPrimaryKeySelective(customer);
	}

	@Override
	public String getDuplicateByPhone(String phone, Integer userId) {
		String current = "";
		CustomerExample example = new CustomerExample();
		example.createCriteria().andPhoneEqualTo(phone).andStateEqualTo(1);
		List<Customer> cList = customerMapper.selectByExample(example);
		if (cList != null && cList.size() > 0) {
			User user = userMapper.selectByPrimaryKey(cList.get(0).getClaimUser());
			if (cList.get(0).getClaimUser() == userId) {
				current = "你的";
			} else {
				current = user.getNickName();
			}
		}
		return current;
	}

}
