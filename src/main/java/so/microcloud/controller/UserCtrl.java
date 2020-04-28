package so.microcloud.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import so.microcloud.bean.User;
import so.microcloud.common.Basics;
import so.microcloud.common.HaResponse;
import so.microcloud.common.MD5Util;
import so.microcloud.common.PageInfo;
import so.microcloud.common.ResultCodeVar;
import so.microcloud.service.UserService;
import so.microcloud.vo.UserVO;

@Controller
@RequestMapping("/user")
public class UserCtrl {
	
	private static Log log = LogFactory.getLog(UserCtrl.class);
	
	@Resource(name = "userServiceImpl")
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView view = new ModelAndView("login");
		return view;
	}
	
	/**
	 * 用户登录
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(String phone, String password, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isBlank(phone) || StringUtils.isBlank(password)) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			User user = userService.getUserByPhoneAndPassword(phone, MD5Util.MD5(password));
			if (user != null) {
				session.setAttribute(Basics.LOGINUSER, user);
				UserVO userVO = new UserVO();
				userVO.setId(user.getId());
				userVO.setPhone(user.getPhone());
				userVO.setNickname(user.getNickName());
				userVO.setIsAdmin(user.getIsAdmin());
				return HaResponse.sussess("user", userVO);
			} else {
				return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_1001);
			}
		} catch (Exception e) {
			log.error("用户登录错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 注销--跳转登录页面
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		if(user != null){
			session.removeAttribute(Basics.LOGINUSER);
		}
		ModelAndView view = new ModelAndView("login");
		return view;
	}
	
	// 跳转至用户列表
	@RequestMapping(value = "/tousermanage", method = RequestMethod.GET)
	public ModelAndView tousermanage() {
		ModelAndView view = new ModelAndView("userManage");
		return view;
	}
	
	// 获取用户列表
	@ResponseBody
	@RequestMapping(value = "/getuserlist", method = RequestMethod.GET)
	public HaResponse getpubliccustomerlist(String keyword, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<UserVO> voList = userService.getUserList(keyword, pageInfo);
			return HaResponse.sussess("userList", voList).page(pageInfo);
		} catch (Exception e) {
			log.error("用户列表查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	// 获取用户详情
	@ResponseBody
	@RequestMapping(value = "/getuser", method = RequestMethod.GET)
	public HaResponse getuser(Integer id, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			UserVO vo = userService.getUserById(id);
			return HaResponse.sussess("user", vo);
		} catch (Exception e) {
			log.error("用户查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
		
	//保存或修改用户信息
	@ResponseBody
	@RequestMapping(value = "/saveorupdate", method = RequestMethod.POST)
	public HaResponse saveorupdate(UserVO userVO, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		try {
			int i = userService.saveOrUpdate(userVO, user);
			if (i == 2) {
				return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_1002);
			}
			return HaResponse.sussess("saveorupdate", i);
		} catch (Exception e) {
			log.error("保存或更新用户错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	// 删除用户信息
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public HaResponse saveorupdate(Integer id, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			userService.delete(id);
			return HaResponse.sussess("deleted", 1);
		} catch (Exception e) {
			log.error("删除用户错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	// 修改密码
	@ResponseBody
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public HaResponse changepassword(Integer userId, String password, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			userService.changePassword(userId, password);
			return HaResponse.sussess("change", 1);
		} catch (Exception e) {
			log.error("修改密码错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	
}
