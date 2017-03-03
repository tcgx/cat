package com.dianping.cat.report.alert.sender.sender;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dianping.cat.Cat;
import com.dianping.cat.report.alert.sender.AlertChannel;
import com.dianping.cat.report.alert.sender.AlertMessageEntity;

public class SlackSender extends AbstractSender {

	public static final String ID = AlertChannel.SLACK.getName();

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean send(AlertMessageEntity message) {
		com.dianping.cat.home.sender.entity.Sender sender = querySender();
		boolean result = false;
		result = sendMessage(message, sender);
		return result;
	}

	private boolean sendMessage(AlertMessageEntity message,
			com.dianping.cat.home.sender.entity.Sender sender) {
		String title = message.getTitle().replaceAll(",", " ");
		String content = message.getContent().replaceAll("(<a href.*(?=</a>)</a>)|(\n)", "");
		String urlPrefix = sender.getUrl();
		String urlPars = m_senderConfigManager.queryParString(sender);
		String time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

		try {
			urlPars = urlPars
					.replace("${title}", title)
					.replace("${content}", content)
					.replace("${time}", time);

		} catch (Exception e) {
			Cat.logError(e);
		}
		String jsonString=urlPars;
		return httpPostWithJson(sender.getSuccessCode(), urlPrefix, jsonString);
	}
}
