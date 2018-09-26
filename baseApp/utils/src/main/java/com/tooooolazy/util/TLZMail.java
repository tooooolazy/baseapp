package com.tooooolazy.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The properties required can be loaded using {@link TLZUtils#loadProperties(String)}
 * <ul>Requires:
 * <li>activation.jar</li>
 * <li>mail.jar</li>
 * @author tooooolazy
 *
 */
public class TLZMail {
	public static final String MAIL_SMTP_AUTH_PASSWORD = "mail.smtp.auth.password";
	public static final String MAIL_SMTP_AUTH_USER = "mail.smtp.auth.user";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String MAIL_SENDER = "mail.sender";

	public static void sendEmail(final Properties config, String to, String subject, String msg) throws AddressException, MessagingException {
		sendEmail(config, new String[] {to}, null, null, subject, msg, false);
	}
	public static void sendHTMLEmail(final Properties config, String to, String subject, String msg) throws AddressException, MessagingException {
		sendEmail(config, new String[] {to}, null, null, subject, msg, true);
	}
	public static void sendEmail(final Properties config, String[] to, String[] to_cc, String[] to_bcc, String subject, String msg, boolean isHtml) throws AddressException, MessagingException {
		verifyConfiguration(config);
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty(MAIL_SMTP_HOST, config.getProperty(MAIL_SMTP_HOST));
		if (config.getProperty(MAIL_SMTP_PORT) != null)
			properties.setProperty(MAIL_SMTP_PORT, config.getProperty(MAIL_SMTP_PORT));

		Session session = null;
		if ( "true".equals( config.getProperty(MAIL_SMTP_AUTH) ) ) {
			properties.setProperty(MAIL_SMTP_AUTH, "true");
			// Get the Session object.
			session = getSecureSession(config, properties);
		} else {
			session = Session.getDefaultInstance(properties);
		}

		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		// Set From: header field of the header.
		message.setFrom(new InternetAddress(config.getProperty(MAIL_SENDER)));

		// Set To: header field of the header.
		if (to != null) setRecipients(Message.RecipientType.TO, message, to);
		// Set CC: header field of the header.
		if (to_cc != null) setRecipients(Message.RecipientType.CC, message, to_cc);
		// Set BCC: header field of the header.
		if (to_bcc != null) setRecipients(Message.RecipientType.BCC, message, to_bcc);

		// Set Subject: header field
		message.setSubject( subject, "UTF-8" );

		// Now set the actual message
		if (!isHtml)
			message.setText( msg );
		else
			message.setContent(msg, "text/html; charset=utf-8");

		// Send message
		Transport.send(message);
		System.out.println("Sent message successfully....");
	}

	private static void verifyConfiguration(Properties config) {
		if (!config.containsKey(MAIL_SMTP_HOST))
			throw new RuntimeException("SMTP_HOST not defined");
		if (!config.containsKey(MAIL_SENDER))
			throw new RuntimeException("MAIL_SENDER not defined");
	}
	/**
	 * @param config
	 * @param properties
	 * @return
	 */
	protected static Session getSecureSession(final Properties config, Properties properties) {
		return Session.getInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(config.getProperty(MAIL_SMTP_AUTH_USER), config.getProperty(MAIL_SMTP_AUTH_PASSWORD));
			}
		});
	}

	public static void setRecipients(RecipientType rt, MimeMessage message, String[] to) throws AddressException, MessagingException {
		for (int i=0; i<to.length; i++)
			message.addRecipient(rt, new InternetAddress(to[i]));
	}

}
