package org.hld.invoice.common.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件工具类
 * @author 李浩然 2017年7月22日
 * @version 1.0
 */
public class EmailUtil {

	/**
	 * 发送一封邮件
	 * @author 李浩然
	 * @param email 邮件地址
	 * @param code 验证码
	 * @return 邮件发送结果
	 */
	public static boolean sendMail(String email, String address, String code, String action) {
		Properties prop = new Properties();
		prop.setProperty("mail.host", "smtp.163.com");
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		try {
			Transport ts = session.getTransport();
			// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
			ts.connect("smtp.163.com", "15528235793@163.com", "lhr6412145");
			// 4、创建邮件
			Message message = createSimpleMail(email, address, code, action, session);
			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	/**
	 * 创建一封邮件
	 * @author 李浩然
	 * @param email 邮件地址
	 * @param code 验证码
	 * @param session 邮件Session对象
	 * @return 创建好的邮件
	 * @throws MessagingException 可能抛出该异常
	 */
	private static MimeMessage createSimpleMail(String email, String address, String code, String action, Session session)
			throws MessagingException {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress("15528235793@163.com"));
		// 指明邮件的收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		// 邮件的标题
		message.setSubject("企业增值税发票数据分析系统");
		// 邮件的文本内容
		String link = address + "?action=" + action + "&code=" + code;
        String sb = "<p>欢迎使用企业增值税发票数据分析系统！<br></p>" +
                "<p><a href=\"" + link + "\">" + "<font color=\"#FF0000\">请点击此处验证邮件</font>" + "</a><br></p>" +
                "<p>若无法点击上述验证按钮，请复制以下链接至浏览器地址栏：<br></p>" +
                "<p>" + link + "<br></p>" +
                "<p><font color=\"#FF0000\">请在一天内验证，且链接只能使用一次</font></p>";
        message.setContent(sb, "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}
}
