package com.linkage.community.schedule.push.xinge;

import com.tencent.xinge.XingeApp;

public class LinkageXingeQiye
{
	public static final long ACCESS_ID = 2200044551L;
	
	public static final String ACCESS_KEY = "INV232IPX45K";
	
	public static final String SECRET_KEY = "ebb02d2e7371bfacd3e62362fb007eba";
	
	private LinkageXingeQiye()
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
