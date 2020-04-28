package so.microcloud.common;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import so.microcloud.common.ResultCodeVar;

public class HaResponse extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public static final String KEY_SUSSESS = "success";

	public static final String KEY_DATA = "data";

	public static final String KEY_MESSAGE = "message";

	public static final String KEY_ERR_MESSAGE = "errmsg";

	public static final String KEY_RESULT_CODE = "resultCode";

	public static final String KEY_ERROR_CODE = "errCode";

	public static final String KEY_CURRENT_PAGE = "currentPage";

	public static final String KEY_TOTAL_COUNT = "total";

	public static final String KEY_TOTAL_PAGE = "totalPage";

	public static final String SUCCESS_CODE = "200";

	public static HaResponse sussess(Object data) {
		HaResponse resp = new HaResponse();
		resp.put(KEY_SUSSESS, true);
		resp.put(KEY_DATA, data);
		resp.put(KEY_RESULT_CODE, SUCCESS_CODE);
		return resp;
	}

	public static HaResponse sussess(String key, Object data) {
		HaResponse resp = new HaResponse();
//		resp.put(KEY_SUSSESS, true);
		resp.put(key, data);
//		resp.put(KEY_RESULT_CODE, SUCCESS_CODE);
		return resp;
	}

	public static HaResponse sussess() {
		HaResponse resp = new HaResponse();
		resp.put(KEY_SUSSESS, true);
		resp.put(KEY_RESULT_CODE, SUCCESS_CODE);
		return resp;
	}

	public static HaResponse fail(Object data) {
		HaResponse resp = new HaResponse();
		resp.put(KEY_SUSSESS, false);
		resp.put(KEY_DATA, data);
		return resp;
	}

	public static HaResponse fail() {
		HaResponse resp = new HaResponse();
//		resp.put(KEY_SUSSESS, false);
		return resp;
	}

	public static HaResponse fail(HttpServletResponse response) {
		HaResponse resp = new HaResponse();
		response.setStatus(500);
//		resp.put(KEY_SUSSESS, false);
		return resp;
	}

	public HaResponse message(String message) {
		this.put(KEY_MESSAGE, message);
		return this;
	}

	public HaResponse append(String key, Object value) {
		this.put(key, value);
		return this;
	}

	public HaResponse errorCode(String resultCode) {
		this.put(KEY_RESULT_CODE, resultCode);
		if (null != resultCode) {
			for (ResultCodeVar obj : ResultCodeVar.values()) {
				if (resultCode.equals(obj.getIndex() + "")) {
					this.put(KEY_MESSAGE, obj.getName());
					break;
				}
			}
		}
		return this;
	}

	public HaResponse page(PageInfo page) {
		this.put(KEY_CURRENT_PAGE, page.getCurrentPage());
		this.put(KEY_TOTAL_COUNT, page.getTotalCount());
		int totalPage = page.getTotalCount() / page.getPageSize();
		if (page.getTotalCount() % page.getPageSize() > 0) {
			totalPage++;
		}
		this.put(KEY_TOTAL_PAGE, totalPage);
		return this;
	}

	/**
	 * 返回错误码
	 * 
	 * @param resultCode
	 * @return
	 */
	public HaResponse errCode(ResultCodeVar resultCodeVar) {
		if (resultCodeVar != null) {
			this.put(KEY_ERROR_CODE, resultCodeVar.getIndex());
			this.put(KEY_MESSAGE, resultCodeVar.getName());
		}
		return this;
	}
	
}
