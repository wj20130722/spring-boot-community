package com.linkage.community.schedule.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 服务器邮箱登录验证
 * @author wangjie
 * 
 */
public class MailAuthenticator extends Authenticator
{
	 /**
   * 用户名（登录邮箱）
   */
  private String username;
  /**
   * 密码
   */
  private String password;
  
  
  
	
	public MailAuthenticator(String username, String password)
  {
	  this.username = username;
	  this.password = password;
  }

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@Override
  protected PasswordAuthentication getPasswordAuthentication()
  {
		return new PasswordAuthentication(username, password);
  }

}
