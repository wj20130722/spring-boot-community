package com.linkage.community.schedule.push.xinge;

import com.tencent.xinge.XingeApp;

public class LinkageXingeAndroid
{
	public static final long ACCESS_ID = 2100044594L;
	
	public static final String ACCESS_KEY = "A5ZJC69K84NQ";
	
	public static final String SECRET_KEY = "99c7b1363ea121e6277f8f24bc13b3df";
	
	private LinkageXingeAndroid()
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
