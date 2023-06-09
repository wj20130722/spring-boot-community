package com.linkage.community.schedule.mail;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TestMailSend
{
	public static void main(String[] args) throws Exception
  {
  
	//	 SimpleMailSender sms = MailSenderFactory
	//        .getSender(MailSenderType.SERVICE);
		 
  //  StringBuffer sb = new StringBuffer();
   // sb.append("<br>");
   // sb.append("<br>");
   // sb.append("<a href='http://www.baidu.com'>百度</a>");
  //  sms.send("xxxxxxx", "测试", sb.toString());
    
    int userId = 1111;
    int shopId = 22222;
    
	List<String> recipients = new ArrayList<String>();
	recipients.add("xxxxxx");
	recipients.add("xxxxxxx");
	SimpleMailSender sms = MailSenderFactory.getSender(MailSenderType.SERVICE);
	String mailSubject = "�û�" + userId + "��" + DateFormat.getInstance().format(Calendar.getInstance().getTime())+ "����������̣����̱�ţ�" + shopId;
	sms.send(recipients, mailSubject, "");
	System.out.println("������ɣ�");
  }
}
