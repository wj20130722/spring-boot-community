package com.tencent.xinge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class Message {
	public static final int TYPE_NOTIFICATION  = 1;
	public static final int TYPE_MESSAGE = 2;
	
	public Message()
	{
		this.m_title = "";
		this.m_content = "";
		this.m_sendTime = "2013-12-20 18:31:00";
		this.m_acceptTimes = new Vector<TimeInterval>();
		this.m_multiPkg = 0;
		this.m_raw = "";
	}
	
	public void setTitle(String title)
	{
		this.m_title = title;
	}
	public void setContent(String content)
	{
		this.m_content = content;
	}
	public void setExpireTime(int expireTime)
	{
		this.m_expireTime = expireTime;
	}
	public int getExpireTime()
	{
		return this.m_expireTime;
	}
	public void setSendTime(String sendTime)
	{
		this.m_sendTime = sendTime;
	}
	public String getSendTime()
	{
		return this.m_sendTime;
	}
	public void addAcceptTime(TimeInterval acceptTime)
	{
		this.m_acceptTimes.add(acceptTime);
	}
	public String acceptTimeToJson()
	{
		JSONArray json_arr = new JSONArray();
		for (TimeInterval ti : m_acceptTimes) 
		{
			JSONObject jtmp = ti.toJsonObject();
			json_arr.put(jtmp);
		}
		return json_arr.toString();
	}
	public JSONArray acceptTimeToJsonArray()
	{
		JSONArray json_arr = new JSONArray();
		for (TimeInterval ti : m_acceptTimes) 
		{
			JSONObject jtmp = ti.toJsonObject();
			json_arr.put(jtmp);
		}
		return json_arr;
	}
	public void setType(int type)
	{
		this.m_type = type;
	}
	public int getType()
	{
		return m_type;
	}
	public void setMultiPkg(int multiPkg)
	{
		this.m_multiPkg = multiPkg;
	}
	public int getMultiPkg()
	{
		return m_multiPkg;
	}
	public void setStyle(Style style)
	{
		this.m_style = style;
	}
	public void setAction(ClickAction action)
	{
		this.m_action = action;
	}
	public void setCustom(Map<String, Object> custom)
	{
		this.m_custom = custom;
	}
	public void setRaw(String raw)
	{
		this.m_raw = raw;
	}
	
	
	public boolean isValid()
	{
		if (!m_raw.isEmpty()) return true;
		if (m_type < TYPE_NOTIFICATION || m_type > TYPE_MESSAGE) 
			return false;
		if (m_multiPkg < 0 || m_multiPkg > 1)
			return false;
		if (m_type == TYPE_NOTIFICATION)
		{
			if(!m_style.isValid()) return false;
			if(!m_action.isValid()) return false;
		}
		if (m_expireTime<0 || m_expireTime>3*24*60*60)
			return false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			sdf.parse(m_sendTime);
		}catch(ParseException e){
			return false;
		}
		for (TimeInterval ti : m_acceptTimes) {
			if(!ti.isValid()) return false;
		}
		
		return true;
	}
	
	public String toJson()
	{
		if (!m_raw.isEmpty()) return m_raw;
		JSONObject json = new JSONObject();
		if(m_type == TYPE_NOTIFICATION)
		{
			json.put("title", m_title);
			json.put("content", m_content);
			json.put("accept_time", acceptTimeToJsonArray());
			json.put("builder_id",m_style.getBuilderId());
			json.put("ring", m_style.getRing());
			json.put("vibrate", m_style.getVibrate());
			json.put("clearable", m_style.getClearable());
			json.put("n_id", m_style.getNId());
			json.put("action", m_action.toJsonObject());
		}
		else if(m_type == TYPE_MESSAGE)
		{
			json.put("title", m_title);
			json.put("content", m_content);
			json.put("accept_time", acceptTimeToJsonArray());
		}
		json.put("custom_content", m_custom);
		return json.toString();
	}
	
	private String m_title;
	private String m_content;
	private int m_expireTime;
	private String m_sendTime;
	private Vector<TimeInterval> m_acceptTimes;
	private int m_type;
	private int m_multiPkg;
	private Style m_style;
	private ClickAction m_action;
	private Map<String, Object> m_custom;
	private String m_raw;
}