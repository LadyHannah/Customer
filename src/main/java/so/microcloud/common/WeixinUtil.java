package so.microcloud.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("deprecation")
public class WeixinUtil {
	private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);

	@SuppressWarnings({ "resource" })
	public static String getOpenId(String code, String appid, String secret) {
		String openid = "";
		try {
			String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", appid, secret, code);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			String tokens = EntityUtils.toString(httpEntity, "utf-8");
			JSONObject object = JSONObject.parseObject(tokens);
			openid = object != null && object.get("openid") != null ? object.get("openid").toString() : "";
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("获取微信openid失败", ex);
		}
		return openid;
	}
}