package so.microcloud.service;

import java.util.List;

import so.microcloud.bean.User;
import so.microcloud.common.PageInfo;
import so.microcloud.vo.UserVO;

public interface UserService {
	
	User getUserByPhoneAndPassword(String phone, String password);
	
	List<UserVO> getUserList(String keyword, PageInfo pageInfo);
	
	UserVO getUserById(Integer id);
	
	int saveOrUpdate(UserVO userVO, User user);
	
	int delete(Integer id);
	
	int changePassword(Integer userId, String password);
	
	List<UserVO> getContactStatistics(String keyword, PageInfo pageInfo);
	
}
