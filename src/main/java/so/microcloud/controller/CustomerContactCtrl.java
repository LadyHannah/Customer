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

import so.microcloud.bean.User;
import so.microcloud.common.Basics;
import so.microcloud.common.HaResponse;
import so.microcloud.common.PageInfo;
import so.microcloud.common.ResultCodeVar;
import so.microcloud.service.CustomerContactService;
import so.microcloud.vo.CustomerContactVO;

@Controller
@RequestMapping("/sys/contact")
public class CustomerContactCtrl {
	
	private static Log log = LogFactory.getLog(CustomerContactCtrl.class);
	
	@Resource(name = "customerContactServiceImpl")
	private CustomerContactService contactService;
	
	/**
	 * 获取联系小计列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getcustomercontactlist", method = RequestMethod.GET)
	public HaResponse getcustomercontactlist(Integer customerId, String keyword, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<CustomerContactVO> voList = contactService.getCustomerContactList(customerId, keyword, pageInfo);
			return HaResponse.sussess("customerContact", voList);
		} catch (Exception e) {
			log.error("联系小计列表查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 保存联系小计
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public HaResponse save(CustomerContactVO contactVO, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		if (StringUtils.isBlank(contactVO.getContent())) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			int i = contactService.save(contactVO, user);
			if (i == 0) {
				return HaResponse.fail().errCode(ResultCodeVar.RESULTCODE_500);
			}
			return HaResponse.sussess("saveorupdate", 1);
		} catch (Exception e) {
			log.error("保存联系小计错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 更新联系小计
	 */
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public HaResponse update(CustomerContactVO contactVO, HttpServletRequest request, 
			HttpServletResponse response) {
		if (StringUtils.isBlank(contactVO.getContent())) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			int i = contactService.update(contactVO);
			if (i == 0) {
				return HaResponse.fail().errCode(ResultCodeVar.RESULTCODE_500);
			}
			return HaResponse.sussess("update", 1);
		} catch (Exception e) {
			log.error("更新联系小计错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 删除联系小计
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public HaResponse delete(Integer contactId, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		if (contactId == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			int i = contactService.delete(contactId);
			if (i == 0) {
				return HaResponse.fail().errCode(ResultCodeVar.RESULTCODE_500);
			}
			return HaResponse.sussess("delete", 1);
		} catch (Exception e) {
			log.error("删除联系小计错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
}
