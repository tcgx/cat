package com.dianping.cat.report.alert.sender.spliter;

import java.util.regex.Pattern;

import com.dianping.cat.report.alert.sender.AlertChannel;

public class SlackSpliter implements Spliter {

	public static final String ID = AlertChannel.SLACK.getName();

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String process(String content) {
		String slackContent = content.replaceAll("<br/>", "\n");
		return Pattern.compile("<div.*(?=</div>)</div>", Pattern.DOTALL).matcher(slackContent).replaceAll("");
	}

}
