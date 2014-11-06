package com.linkage.community.schedule.mail;

public class MailSenderFactory
{
	private static SimpleMailSender serviceSms = null;

	public static SimpleMailSender getSender(MailSenderType type)
	{
		if (type == MailSenderType.SERVICE)
		{
			if (serviceSms == null)
			{
				serviceSms = new SimpleMailSender("785592116@qq.com", "w258305311");
			}
			return serviceSms;
		}
		return null;
	}
}
