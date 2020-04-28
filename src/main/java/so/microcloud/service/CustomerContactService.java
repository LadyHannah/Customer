package so.microcloud.service;

import java.util.List;

import so.microcloud.bean.User;
import so.microcloud.common.PageInfo;
import so.microcloud.vo.CustomerContactVO;

public interface CustomerContactService {

	List<CustomerContactVO> getCustomerContactList(Integer customerId, String keyword, PageInfo pageInfo);
	
	List<CustomerContactVO> getCustomerContactList(Integer customerId);
	
	int save(CustomerContactVO contactVO, User user);
	
	int update(CustomerContactVO contactVO);
	
	int delete(Integer typeId);
}
