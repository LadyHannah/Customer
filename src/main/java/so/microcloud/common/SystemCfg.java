package so.microcloud.common;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

/**
 * 系统全局配置项定义
 */
public class SystemCfg {
	public static int timer_delay_min = 10;
	private static Properties properties;
	private static Map<String,String> config = Maps.newHashMap();
	static{
		properties = new Properties();
		URL url = null;
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			url = classloader.getResource("system.properties");
			properties.load(url.openStream());
			for (String name : properties.stringPropertyNames()) {
				config.put(name, properties.getProperty(name));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String get(String key){
		return config.get(key);
	}
	
	public static String get(String key, String defaultValue) {
		String value = null;
		if (config.containsKey(key)) {
			value = config.get(key);
		}
		if (value == null || "".equals(value)) {
			value = defaultValue;
		}
		return value;
	}

}
