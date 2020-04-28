package so.microcloud.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import so.microcloud.bean.CustomerExample;
import so.microcloud.bean.CustomerType;
import so.microcloud.bean.CustomerTypeExample;
import so.microcloud.bean.User;
import so.microcloud.dao.CustomerMapper;
import so.microcloud.dao.CustomerTypeMapper;
import so.microcloud.service.CustomerTypeService;
import so.microcloud.vo.CustomerTypeVO;

@Service("customerTypeServiceImpl")
public class CustomerTypeServiceImpl implements CustomerTypeService {

	@Autowired
	private CustomerTypeMapper customerTypeMapper;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Override
	public List<CustomerTypeVO> getCustomerTypeList() {
		List<CustomerTypeVO> voList = new ArrayList<>();
		CustomerTypeExample example = new CustomerTypeExample();
		example.createCriteria().andStateEqualTo(1);
		List<CustomerType> list = customerTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for (CustomerType customerType : list) {
				CustomerTypeVO vo = convertToCustomerTypeVO(customerType);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	private CustomerTypeVO convertToCustomerTypeVO(CustomerType customerType) {
		CustomerTypeVO vo = new CustomerTypeVO();
		vo.setId(customerType.getId());
		vo.setTypeName(customerType.getTypeName());
		return vo;
	}

	@Override
	public int saveOrUpdate(CustomerTypeVO customerTypeVO, User user) {
		CustomerType type = new CustomerType();
		type.setTypeName(customerTypeVO.getTypeName());
		if (customerTypeVO.getId() == null) {
			type.setCreateUser(user.getId());
			type.setCreateTime(new Date());
			type.setState(1);
			return customerTypeMapper.insert(type);
		} else {
			type.setId(customerTypeVO.getId());
			return customerTypeMapper.updateByPrimaryKeySelective(type);
		}
	}

	@Override
	public int delete(Integer typeId) {
		CustomerExample example = new CustomerExample();
		example.createCriteria().andTypeIdEqualTo(typeId).andStateEqualTo(1);
		long count = customerMapper.countByExample(example);
		if (count >= 0) {
			CustomerType type = customerTypeMapper.selectByPrimaryKey(typeId);
			type.setState(0);
			return customerTypeMapper.updateByPrimaryKey(type);
		}
		return 0;
	}

}
