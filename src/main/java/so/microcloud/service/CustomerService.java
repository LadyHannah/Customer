package so.microcloud.service;

import java.util.List;

import so.microcloud.bean.User;
import so.microcloud.common.PageInfo;
import so.microcloud.vo.CustomerVO;

public interface CustomerService {
	
	List<CustomerVO> getPublicCustomerList(Integer typeId, String keyword, User user, PageInfo pageInfo);

	List<CustomerVO> getCustomerList(Integer typeId, String keyword, User user, PageInfo pageInfo);
	
	CustomerVO getCustomerById(Integer id);
	
	long getMyCustomerCount(Integer userId);
	
	int save(CustomerVO customerVO, String content, User user);
	
	int modify(CustomerVO customerVO, String content, User user);
	
	int delete(Integer id);
	
	int drop(Integer id);
	
	int getBack(Integer id, User user);
	
	String getDuplicateByPhone(String phone, Integer userId);
}
