package so.microcloud.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import so.microcloud.bean.CustomerContact;
import so.microcloud.bean.CustomerContactExample;
import so.microcloud.bean.User;
import so.microcloud.common.DateUtil;
import so.microcloud.common.PageInfo;
import so.microcloud.dao.CustomerContactMapper;
import so.microcloud.service.CustomerContactService;
import so.microcloud.vo.CustomerContactVO;

@Service("customerContactServiceImpl")
public class CustomerContactServiceImpl implements CustomerContactService {

	@Autowired
	private CustomerContactMapper customerContactMapper;
	
	@Override
	public List<CustomerContactVO> getCustomerContactList(Integer customerId, String keyword, PageInfo pageInfo) {
		List<CustomerContactVO> voList = new ArrayList<>();
		List<CustomerContact> list = customerContactMapper.getCustomerContactList(customerId, keyword, pageInfo);
		if (list != null && list.size() > 0) {
			for (CustomerContact contact : list) {
				CustomerContactVO vo = convertToCustomerContact(contact);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	@Override
	public List<CustomerContactVO> getCustomerContactList(Integer customerId) {
		List<CustomerContactVO> voList = new ArrayList<>();
		CustomerContactExample example = new CustomerContactExample();
		example.createCriteria().andCustomerIdEqualTo(customerId).andStateEqualTo(1);
		List<CustomerContact> list = customerContactMapper.getContactList(customerId);
		if (list !=null && list.size() > 0) {
			for (CustomerContact contact : list) {
				CustomerContactVO vo = convertToCustomerContact(contact);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	private CustomerContactVO convertToCustomerContact(CustomerContact contact) {
		CustomerContactVO vo = new CustomerContactVO();
		vo.setId(contact.getId());
		vo.setCustomerId(contact.getCustomerId());
		vo.setContent(contact.getContent());
		vo.setCreateUserName(contact.getCreateUserName());
		vo.setCreateTime(DateUtil.getFormattedDate(contact.getCreateTime(), DateUtil.noSecondFormat));
		vo.setLastModifyTime(DateUtil.getFormattedDate(contact.getLastModifyTime(), DateUtil.noSecondFormat));
		return vo;
	}

	@Override
	public int save(CustomerContactVO contactVO, User user) {
		if (contactVO.getId() == null) {
			CustomerContact contact = new CustomerContact();
			contact.setCustomerId(contactVO.getCustomerId());
			contact.setContent(contactVO.getContent());
			contact.setCreateUser(user.getId());
			contact.setCreateTime(new Date());
			contact.setLastModifyTime(new Date());
			contact.setState(1);
			return customerContactMapper.insert(contact);
		}
		return 0;
	}
	
	@Override
	public int update(CustomerContactVO contactVO) {
		if (contactVO.getId() != null) {
			CustomerContact contact = customerContactMapper.selectByPrimaryKey(contactVO.getId());
			contact.setContent(contact.getContent());
			contact.setLastModifyTime(new Date());
			return customerContactMapper.updateByPrimaryKeySelective(contact);
		}
		return 0;
	}

	@Override
	public int delete(Integer contactId) {
		CustomerContact contact = customerContactMapper.selectByPrimaryKey(contactId);
		contact.setState(1);
		return customerContactMapper.updateByPrimaryKey(contact);
	}

}
