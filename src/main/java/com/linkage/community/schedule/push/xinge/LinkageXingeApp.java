package com.linkage.community.schedule.push.xinge;

import com.tencent.xinge.XingeApp;

public class LinkageXingeApp
{
	public static final long ACCESS_ID = 2200037748L;
	
	public static final String ACCESS_KEY = "IQ7P24J3N1VH";
	
	public static final String SECRET_KEY = "7229586de9acf96d67ea352653138ccc";
	
	private LinkageXingeApp()
	{
		
	}
	
	private static class XingeAppInstance 
	{
		private static XingeApp xinge = new XingeApp(ACCESS_ID, SECRET_KEY);
  }
	
	public static XingeApp getInstance()
	{
		return XingeAppInstance.xinge;
	}
	
	
}
