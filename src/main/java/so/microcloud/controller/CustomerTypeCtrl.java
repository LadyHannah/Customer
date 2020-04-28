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
import so.microcloud.service.CustomerTypeService;
import so.microcloud.vo.CustomerTypeVO;

@Controller
@RequestMapping("/sys/type")
public class CustomerTypeCtrl {
	
	private static Log log = LogFactory.getLog(CustomerTypeCtrl.class);
	
	@Resource(name = "customerTypeServiceImpl")
	private CustomerTypeService customerTypeService;
	
	/**
	 * 获取客户类型列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getcustomertypelist", method = RequestMethod.GET)
	public HaResponse getcustomertypelist(Integer typeId, String keyword, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<CustomerTypeVO> voList = customerTypeService.getCustomerTypeList();
			return HaResponse.sussess("customerType", voList);
		} catch (Exception e) {
			log.error("客户类型列表查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 保存或更新客户类型
	 */
	@ResponseBody
	@RequestMapping(value = "saveorupdate", method = RequestMethod.POST)
	public HaResponse saveorupdate(CustomerTypeVO typeVO, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		User user = (User) session.getAttribute(Basics.LOGINUSER);
		if (StringUtils.isBlank(typeVO.getTypeName())) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			int i = customerTypeService.saveOrUpdate(typeVO, user);
			if (i == 0) {
				return HaResponse.fail().errCode(ResultCodeVar.RESULTCODE_500);
			}
			return HaResponse.sussess("saveorupdate", 1);
		} catch (Exception e) {
			log.error("保存或更新客户类型错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
	/**
	 * 删除客户类型
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public HaResponse delete(Integer typeId, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		if (typeId == null) {
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_400);
		}
		try {
			int i = customerTypeService.delete(typeId);
			if (i == 0) {
				return HaResponse.fail().errCode(ResultCodeVar.RESULTCODE_500);
			}
			return HaResponse.sussess("delete", 1);
		} catch (Exception e) {
			log.error("删除客户类型错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
}
