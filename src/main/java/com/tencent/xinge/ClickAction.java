package com.tencent.xinge;

import org.json.JSONObject;

public class ClickAction {
	public static final int TYPE_ACTIVITY = 1;
	public static final int TYPE_URL = 2;
	public static final int TYPE_INTENT = 3;
	
	public void setActionType(int actionType)
	{
		this.m_actionType = actionType;
	}
	public void setActivity(String activity)
	{
		this.m_activity = activity;
	}
	public void setUrl(String url)
	{
		this.m_url = url;
	}
	public void setConfirmOnUrl(int confirmOnUrl)
	{
		this.m_confirmOnUrl = confirmOnUrl;
	}
	public void setIntent(String intent)
	{
		this.m_intent = intent;
	}
	
	public String toJson()
	{
		JSONObject json = new JSONObject();
		json.put("action_type", m_actionType);
		JSONObject browser = new JSONObject();
		browser.put("url", m_url);
		browser.put("confirm", m_confirmOnUrl);
		json.put("browser", browser);
		json.put("activity", m_activity);
		json.put("intent", m_intent);
		return json.toString();
	}
	
	public JSONObject toJsonObject()
	{
		JSONObject json = new JSONObject();
		json.put("action_type", m_actionType);
		JSONObject browser = new JSONObject();
		browser.put("url", m_url);
		browser.put("confirm", m_confirmOnUrl);
		json.put("browser", browser);
		json.put("activity", m_activity);
		json.put("intent", m_intent);
		return json;
	}
	
	public boolean isValid()
	{
		if (m_actionType<TYPE_ACTIVITY || m_actionType>TYPE_INTENT) return false;

		if (m_actionType == TYPE_URL)
		{
			if (m_url.isEmpty() || m_confirmOnUrl<0 || m_confirmOnUrl>1) return false;
			return true;
		}
		if(m_actionType == TYPE_INTENT)
		{
			if(m_intent.isEmpty()) return false;
			return true;
		}
		return true;
	}
	
	public ClickAction()
	{
		m_url = "";
		m_actionType = 1;
		m_activity = "";
	}
	
	private int m_actionType;
	private String m_url;
	private int m_confirmOnUrl;
	private String m_activity;
	private String m_intent;
}