package so.microcloud.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import so.microcloud.common.HaResponse;
import so.microcloud.common.PageInfo;
import so.microcloud.common.ResultCodeVar;
import so.microcloud.service.UserService;
import so.microcloud.vo.UserVO;

@Controller
@RequestMapping("/data")
public class DataStatisticsCtrl {
	
	private static Log log = LogFactory.getLog(DataStatisticsCtrl.class);
	
	@Resource(name = "userServiceImpl")
	private UserService userService;
	
	@RequestMapping(value = "/tocontactstatistics", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView view = new ModelAndView("contactStatistics");
		return view;
	}

	
	// 获取用户联系量
	@ResponseBody
	@RequestMapping(value = "/getcontactstatistics", method = RequestMethod.GET)
	public HaResponse getcontactstatistics(String keyword, PageInfo pageInfo, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<UserVO> voList = userService.getContactStatistics(keyword, pageInfo);
			return HaResponse.sussess("contactList", voList).page(pageInfo);
		} catch (Exception e) {
			log.error("联系量查询错误", e);
			return HaResponse.fail(response).errCode(ResultCodeVar.RESULTCODE_500);
		}
	}
	
}
