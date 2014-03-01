package com.zappos.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.zappos.bean.ProductAndUser;

public class MailService {
	static Logger log = Logger.getLogger(MailService.class.getName());

	private static final String smtpFilePath = "B:\\terracotta_bundle\\terracotta_workspace\\MailTrigger\\MyMailConfig.txt";
	private static Properties fMailServerConfig = new Properties();

	/**
	 * Allows the config to be refreshed at runtime, instead of requiring a
	 * restart.
	 */
	public static void refreshConfig() {
		fMailServerConfig.clear();
		fetchConfig();
	}

	// PRIVATE
	static {
		fetchConfig();
	}

	/**
	 * Open a specific text file containing mail server parameters, and populate
	 * a corresponding Properties object.
	 */
	private static void fetchConfig() {
		// This file contains the javax.mail config properties mentioned above.
		Path path = Paths.get(smtpFilePath);
		try {
			InputStream input = Files.newInputStream(path);
			fMailServerConfig.load(input);
		} catch (IOException ex) {
			log.severe("Cannot open and load mail server properties file.");
		}catch(Exception e){
			log.severe("Exception occurred"+e.getMessage());
		}
	}

	public static void sendMail(ProductAndUser productAndUser) {
		Session session = Session.getDefaultInstance(fMailServerConfig,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fMailServerConfig
								.getProperty("mail.smtp.user"),
								fMailServerConfig
										.getProperty("mail.smtp.password"));
					}
				});
		MimeMessage message = new MimeMessage(session);
		try {
			StringBuilder sb = new StringBuilder();
			for (String eid : productAndUser.emailIds) {
				sb.append(eid + ",");
			}

			sb.deleteCharAt(sb.lastIndexOf(","));
			log.info(sb.toString());
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(sb.toString()));
			message.setSubject("Zappos: " + productAndUser.getProductName()
					+ " has reached " + productAndUser.getPercentOff()
					+ "% discount!! :)");
			String body = "<h1>Hello,</h1>";
			body += "<p>The product you requested now has a whopping discount of "
					+ productAndUser.getPercentOff() + "%";
			body += "<br><br>" + "<a href=\"" + productAndUser.getProductUrl()
					+ "\">Click me for the product's link!</a>";
			body += "<br><br>Thank you,<br>The Zappos Team" + "</p>";
			 message.setText(body, "UTF-8", "html");
			//message.setText(body, "UTF-8");
			Transport.send(message);
		} catch (MessagingException ex) {
			log.severe("Cannot send email. " + ex.getMessage());
		} catch (Exception ex) {
			log.severe("Cannot send email. " + ex.getMessage());
		}
	}

}
