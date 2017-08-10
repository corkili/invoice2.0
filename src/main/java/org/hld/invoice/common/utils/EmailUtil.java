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
	public static boolean sendMail(String email, String code) {
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
			Message message = createSimpleMail(email, code, session);
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
	private static MimeMessage createSimpleMail(String email, String code, Session session) 
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
		StringBuilder sb = new StringBuilder();
		sb.append("<p>欢迎使用企业增值税发票数据分析系统！<br></p>");
		sb.append("<p>您的验证码为：" + code + "<br></p>");
		sb.append("<p>为了您的账号安全，请勿将验证码泄露给他人，谢谢！<br></p>");
		message.setContent(sb.toString(), "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}
}
