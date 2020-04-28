package so.microcloud.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import so.microcloud.bean.User;
import so.microcloud.bean.UserExample;
import so.microcloud.common.MD5Util;
import so.microcloud.common.PageInfo;
import so.microcloud.dao.UserMapper;
import so.microcloud.service.UserService;
import so.microcloud.vo.UserVO;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public User getUserByPhoneAndPassword(String phone, String password) {
		UserExample example = new UserExample();
		example.createCriteria().andPhoneEqualTo(phone).andPasswordEqualTo(password).andStateEqualTo(1);
		List<User> userList = userMapper.selectByExample(example);
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		}
		return null;
	}
	
	@Override
	public List<UserVO> getUserList(String keyword, PageInfo pageInfo) {
		List<UserVO> voList = new ArrayList<UserVO>();
		List<User> list = userMapper.getUserList(keyword, pageInfo);
		if (list != null && list.size() > 0) {
			for (User user : list) {
				UserVO vo = convertToUserVO(user);
				voList.add(vo);
			}
		}
		return voList;
	}
	
	@Override
	public UserVO getUserById(Integer id) {
		UserVO vo = new UserVO();
		User user = userMapper.selectByPrimaryKey(id);
		if (user != null) {
			vo = convertToUserVO(user);
		}
		return vo;
	}
	
	private UserVO convertToUserVO(User user) {
		UserVO vo = new UserVO();
		vo.setId(user.getId());
		vo.setUsername(user.getUserName());
		vo.setNickname(user.getNickName());
		vo.setPhone(user.getPhone());
		vo.setIsAdmin(user.getIsAdmin());
		return vo;
	}

	@Override
	public int saveOrUpdate(UserVO userVO, User createUser) {
		User user = new User();
		user.setUserName(userVO.getUsername());
		user.setNickName(userVO.getNickname());
		user.setPassword(MD5Util.MD5(userVO.getPassword()));
		user.setIsAdmin(userVO.getIsAdmin());
		if (userVO.getId() == null) {
			UserExample example = new UserExample();
			example.createCriteria().andPhoneEqualTo(userVO.getPhone()).andStateEqualTo(1);
			long count = userMapper.countByExample(example);
			if (count > 0) {
				return 2;
			}
			user.setPhone(userVO.getPhone());
			user.setCreateUser(createUser.getId());
			user.setCreateTime(new Date());
			user.setState(1);
			return userMapper.insert(user);
		} else {
			user.setId(userVO.getId());
			return userMapper.updateByPrimaryKeySelective(user);
		}
	}

	@Override
	public int changePassword(Integer userId, String password) {
		User user = new User();
		user.setId(userId);
		user.setPassword(MD5Util.MD5(password));
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int delete(Integer id) {
		User user = new User();
		user.setId(id);
		user.setState(0);
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public List<UserVO> getContactStatistics(String keyword, PageInfo pageInfo) {
		List<UserVO> voList = new ArrayList<>();
		List<User> list = userMapper.getContactStatistics(keyword, pageInfo);
		if (list != null && list.size() > 0) {
			for (User user : list) {
				UserVO vo = new UserVO();
				vo.setNickname(user.getNickName());
				vo.setUsername(user.getUserName());
				vo.setTodayCount(StringUtils.isBlank(user.getTodayCount()) ? "" : user.getTodayCount());
				vo.setMonthCount(StringUtils.isBlank(user.getMonthCount()) ? "" : user.getMonthCount());
				voList.add(vo);
			}
		}
		return voList;
	}

}
