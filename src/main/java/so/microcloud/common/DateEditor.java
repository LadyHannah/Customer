package so.microcloud.common;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import so.microcloud.common.DateUtil;

public class DateEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(DateEditor.class);

	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? DateUtil.getTimeSampleString(value) : "");
	}

	public void setAsText(String text) {
		if (StringUtils.isNotBlank(text)) {
			try {
				setValue(DateUtil.getGenTime(text));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
}
