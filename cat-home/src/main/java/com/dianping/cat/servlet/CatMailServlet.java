package com.dianping.cat.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.cat.Cat;
import com.dianping.cat.utils.NetworkUtil;

public class CatMailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Properties mailProperties = new Properties();
	private String MAIL_SMTP_USER = "";
	private String MAIL_SMTP_USER_NAME = "";
	private String MAIL_SMTP_PASSWORD = "";
	private String MAIL_SMTP_HOST = "";

	public void init() throws ServletException {
		try {
			mailProperties.load(new FileInputStream(new File(Cat.getCatHome()
					+ "/mail.properties")));
			MAIL_SMTP_HOST = mailProperties.getProperty("mail.smtp.host");
			MAIL_SMTP_USER_NAME = mailProperties
					.getProperty("mail.smtp.user.name");
			MAIL_SMTP_PASSWORD = mailProperties
					.getProperty("mail.smtp.password");
			MAIL_SMTP_USER = mailProperties.getProperty("mail.smtp.user");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!NetworkUtil.getIpAddress(req).equals("127.0.0.1")
				&& !NetworkUtil.getIpAddress(req).equals("localhost")
				&& !NetworkUtil.getIpAddress(req).equals("0:0:0:0:0:0:0:1")) {
			resp.setStatus(500);
			return;
		}

		ServletInputStream inStream = req.getInputStream(); // 取HTTP请求流
		int size = req.getContentLength(); // 取HTTP请求流长度
		byte[] buffer = new byte[size]; // 用于缓存每次读取的数据
		byte[] in_b = new byte[size]; // 用于存放结果的数组
		int count = 0;
		int rbyte = 0;
		// 循环读取
		while (count < size) {
			rbyte = inStream.read(buffer); // 每次实际读取长度存于rbyte中 sflj
			for (int i = 0; i < rbyte; i++) {
				in_b[count + i] = buffer[i];
			}
			count += rbyte;
		}
		Map<String, String> map = URLRequest(new String(in_b, 0, in_b.length));

		String receiver = map.get("to");
		String content = URLDecoder.decode(map.get("value"), "utf-8");
		String title = content.split(",")[0];
		String[] receiverArray = receiver.split(",");

		List<String> receiverList = Arrays.asList(receiverArray);

		PrintWriter out = resp.getWriter();
		// 组织邮件内容
		Session session = Session.getDefaultInstance(mailProperties, null);
		Message msg = new MimeMessage(session);
		InternetAddress fromAddress = new InternetAddress(MAIL_SMTP_USER,
				MAIL_SMTP_USER_NAME);
		try {
			msg.setFrom(fromAddress);
			msg.setRecipients(Message.RecipientType.TO,
					strListToInternetAddresses(receiverList));
			msg.setSubject(title);
			msg.setSentDate(new Date());
			// msg.saveChanges();
			msg.setHeader("X-Mailer", "smtpsend");

			msg.setContent(content, "text/html;charset=UTF-8");

			// 获取连接，发送邮件
			Transport transport = session.getTransport("smtp");
			transport.connect(MAIL_SMTP_HOST, MAIL_SMTP_USER,
					MAIL_SMTP_PASSWORD);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			resp.setStatus(200);
			out.print(200);
		} catch (Exception e) {
			Cat.logError(e);
			resp.setStatus(500);
			out.print(e);
		} finally {
			out.close();
		}

	}

	private static InternetAddress[] strListToInternetAddresses(
			List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		int size = list.size();
		InternetAddress[] arr = new InternetAddress[size];
		for (int i = 0; i < size; i++) {
			try {
				arr[i] = new InternetAddress(list.get(i));
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
		return arr;
	}

	public static Map<String, String> URLRequest(String strUrlParam) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		if (strUrlParam == null) {
			return mapRequest;
		}
		// 每个键值为一组 www.2cto.com
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

}
