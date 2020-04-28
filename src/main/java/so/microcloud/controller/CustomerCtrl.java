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
import so.microcloud.common.PageInfo;
import so.microcloud.common.ResultCodeVar;
import so.microcloud.service.CustomerService;
import so.microcloud.vo.CustomerVO;

@Controller
@RequestMapping("/sys")
public class CustomerCtrl {
	
	private static Log log = LogFactory.getLog(CustomerCtrl.class);
	
	@Resource(name = "customerServiceImpl")
	private CustomerService customerService;
	
	// 跳转至我的客户列表
	@RequestMapping(value = "/toindex", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView view = new ModelAndView("index");
		return view;
	}
	
	// 跳转至公海客户列表
	@RequestMapping(value = "/topubliccustomer", method = RequestMethod.GET)
	public ModelAndView topubliccustomer() {
		ModelAndView view = new ModelAndView("publicCustomer");
		return view;
	}
	
	// 跳转至新增客户页面
	@RequestMapping(value = "/toaddcustomer", method = RequestMethod.GET)
	public ModelAndView toaddcustomer() {
		ModelAndView view = new ModelAndView("addCustomer");
		return view;
	}
	
	// 跳转至新增客户页面
	@RequestMapping(value = "/tomodifycustomer", method = RequestMethod.GET)
	public ModelAndView tomodifycustomer() {
		ModelAndView view = new ModelAndView("modifyCustomer");
		return view;
	}
	
	
	/**
	 * 获取公海客户列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getpubliccustomerlist", method = RequestMethod.GET)
	public HaResponse getpubliccustomerlist(Integer typeId, String keyword, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		try {
			List<CustomerVO> voList = customerService.getPublicCustomerList(typeId, keyword, user, pageInfo);
			return HaResponse.sussess("customer", voList).page(pageInfo);
		} catch (Exception e) {
			log.error("公海客户列表查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 获取客户列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getcustomerlist", method = RequestMethod.GET)
	public HaResponse getcustomerlist(Integer typeId, String keyword, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		try {
			List<CustomerVO> voList = customerService.getCustomerList(typeId, keyword, user, pageInfo);
			return HaResponse.sussess("customer", voList).page(pageInfo);
		} catch (Exception e) {
			log.error("客户列表查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 查询客户信息详情
	 */
	@ResponseBody
	@RequestMapping(value = "/getcustomer", method = RequestMethod.GET)
	public HaResponse getcustomer(Integer id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		if (id == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			CustomerVO vo = customerService.getCustomerById(id);
			return HaResponse.sussess("customer", vo);
		} catch (Exception e) {
			log.error("客户信息详情查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 查询库容，每个用户库容量应<=500
	 */
	@ResponseBody
	@RequestMapping(value = "/getmycustomercount", method = RequestMethod.GET)
	public HaResponse getmycustomercount(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		try {
			long count = customerService.getMyCustomerCount(user.getId());
			return HaResponse.sussess("count", count);
		} catch (Exception e) {
			log.error("客户信息详情查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 根据手机号码查重
	 */
	@ResponseBody
	@RequestMapping(value = "/duplicate", method = RequestMethod.GET)
	public HaResponse duplicate(String phone, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		try {
			String current = customerService.getDuplicateByPhone(phone, user.getId());
			if (StringUtils.isNotBlank(current)) {
				return HaResponse.sussess("current", current);
			}
			return HaResponse.sussess("duplicate", "false"); // 没有重复
		} catch (Exception e) {
			log.error("手机号查重错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 保存客户信息
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public HaResponse save(CustomerVO customerVO, String content, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		if (StringUtils.isBlank(customerVO.getCustomerName()) || StringUtils.isBlank(customerVO.getPhone())
				|| customerVO.getTypeId() == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			// 查询我的库容，超过500后不可再添加客户
			long count = customerService.getMyCustomerCount(user.getId());
			if (count >= 500) {
				return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_1004);
			}
			int i = customerService.save(customerVO, content, user);
			if (i == 0) {
				return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
			} else if (i == 2) {
				return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_1002);
			}
			return HaResponse.sussess("save", 1);
		} catch (Exception e) {
			log.error("保存客户信息错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 修改客户信息
	 */
	@ResponseBody
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public HaResponse modify(CustomerVO customerVO, String content, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		if (customerVO.getId() == null || StringUtils.isBlank(customerVO.getCustomerName()) 
				|| StringUtils.isBlank(customerVO.getPhone())	|| customerVO.getTypeId() == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			int i = customerService.modify(customerVO, content, user);
			if (i == 0) {
				return HaResponse.fail().errCode(ResultCodeVar.RESULTCODE_500);
			} else if (i == 2) {
				return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_1002);
			}
			return HaResponse.sussess("modify", 1);
		} catch (Exception e) {
			log.error("修改客户信息错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 删除客户
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public HaResponse delete(Integer id, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		if (id == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			customerService.delete(id);
			return HaResponse.sussess("deleted", 1);
		} catch (Exception e) {
			log.error("删除客户信息错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 扔入公海
	 */
	@ResponseBody
	@RequestMapping(value = "drop", method = RequestMethod.POST)
	public HaResponse drop(Integer id, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		if (id == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			customerService.drop(id);
			return HaResponse.sussess("drop", 1);
		} catch (Exception e) {
			log.error("扔入公海错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 取回客户
	 */
	@ResponseBody
	@RequestMapping(value = "getback", method = RequestMethod.POST)
	public HaResponse getback(Integer id, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		if (id == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			customerService.getBack(id, user);
			return HaResponse.sussess("getback", 1);
		} catch (Exception e) {
			log.error("取回错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
}
