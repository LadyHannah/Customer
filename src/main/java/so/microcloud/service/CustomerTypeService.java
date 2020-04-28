package so.microcloud.service;

import java.util.List;

import so.microcloud.bean.User;
import so.microcloud.vo.CustomerTypeVO;

public interface CustomerTypeService {

	List<CustomerTypeVO> getCustomerTypeList();
	
	int saveOrUpdate(CustomerTypeVO customerTypeVO, User user);
	
	int delete(Integer typeId);
}
