package com.linkage.community.schedule.push.xinge;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

public class XingeAppPushUtil
{
	public static JSONObject pushSingleAccountAndriod(Message message,String tokenid) 
	{
		XingeApp xinge = LinkageXingeAndroid.getInstance();
		JSONObject ret = xinge.pushSingleAccount(XingeApp.DEVICE_ANDROID, tokenid, message);
		return (ret);
	}
	
	//IOS 下发单个账号
	public static JSONObject pushSingleAccountIOS(MessageIOS message,String tokenid) 
	{
		XingeApp xinge = LinkageXingeApp.getInstance();
		JSONObject ret = xinge.pushSingleAccount(XingeApp.DEVICE_IOS, tokenid, message,XingeApp.IOSENV_DEV);
		
		XingeApp xingeqiye = LinkageXingeQiye.getInstance();
		JSONObject ret1 = xingeqiye.pushSingleAccount(XingeApp.DEVICE_IOS, tokenid, message,XingeApp.IOSENV_PROD);
		return (ret1);
	}

	public static JSONObject pushDeviceIOS(String deviceToken, MessageIOS message){
		XingeApp xinge = LinkageXingeApp.getInstance();
		JSONObject ret = xinge.pushSingleDevice(deviceToken, message, XingeApp.IOSENV_DEV);
		
		
		return (ret);
	}
	
	//下发所有设备ANDROID
	public static JSONObject pushAllDeviceAndroid(Message message)
	{
		XingeApp xinge = LinkageXingeAndroid.getInstance();
		JSONObject ret = xinge.pushAllDevice(XingeApp.DEVICE_ANDROID, message);
		return (ret);
	}
	
	//下发所有设备IOS
	public static JSONObject pushAllDeviceIOS(MessageIOS message)
	{
		/*XingeApp xinge = LinkageXingeApp.getInstance();
		JSONObject ret = xinge.pushAllDevice(XingeApp.DEVICE_IOS, message,XingeApp.IOSENV_PROD);*/
		
		XingeApp xingeqiye = LinkageXingeQiye.getInstance();
		JSONObject ret1 = xingeqiye.pushAllDevice(XingeApp.DEVICE_IOS, message,XingeApp.IOSENV_PROD);
		return (ret1);
	}
	
	public static Message constructMessageAndroid (int type,int nid,int actionType,String title,String content,String intent,Map<String, Object> custom)
	{
		Message message = new Message();
		message.setType(type);
		Style style = new Style(3,1,1,1,nid);
		ClickAction action = new ClickAction();
		action.setActionType(actionType);
		//设置intent消息
		if(actionType == ClickAction.TYPE_INTENT)
			action.setIntent(intent);
		message.setTitle(title); //
		message.setContent(content);
		message.setStyle(style);
		message.setAction(action);
		message.setCustom(custom);
		TimeInterval acceptTime = new TimeInterval(0,0,23,59);
		message.addAcceptTime(acceptTime);
		return message;
	}
	
	public static MessageIOS constructMessageIOS (String title,String content,Map<String, Object> custom)
	{
		MessageIOS message = new MessageIOS();
		String alert = content;
		message.setAlert(alert);
		message.setBadge(1);
		message.setSound("default");
		message.setCustom(custom);
		TimeInterval acceptTime = new TimeInterval(0,0,23,59);
		message.addAcceptTime(acceptTime);
		
		return message;
	}
	
	public static void main(String[] args)
    {
		/*Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("type", "1");
		custom.put("user_id", 1);
		String intent = "intent:#Intent;action=com.linkage.community.notify;end";
		Message message = constructMessageAndroid(Message.TYPE_NOTIFICATION, 1, ClickAction.TYPE_INTENT, "宅里宅外[测试]", "测试。。。",intent, custom);
		JSONObject jsonObject = pushAllDeviceAndroid(message);
		try {
			System.out.println(jsonObject.get("ret_code").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("type", "1");
		//custom.put("id", 1);
		Message message1 = XingeAppPushUtil.constructMessageAndroid(Message.TYPE_NOTIFICATION, 1, ClickAction.TYPE_INTENT, "宅里宅外系统消息", "北风吹来，气温大降，你的冷暖，小宅记心上。你要加衣裳，莫要再着凉。南京明日晴天，最低温度-4度，西北风5～6级。","intent:#Intent;component=com.linkage.community/.ucenter.NotifyActivity;S.type=1;end", custom);
		//JSONObject jsonObject = pushSingleAccountAndriod(message1, "18351448461");
		MessageIOS message = constructMessageIOS(null,"北风吹来,气温大降,你的冷暖,小宅记心上。你要加衣裳，莫要再着凉。南京明日晴天,最低温度-4度,西北风5～6级。", custom);
		/*for(int i=0;i<str.length;i++)
			pushSingleAccountIOS(message,str[i]);*///13851952580
		//JSONObject jsonObject1 = pushSingleAccountIOS(message,"18351448461");
		
		//JSONObject jsonObject = pushAllDeviceAndroid(message1);
		JSONObject jsonObject1 = pushAllDeviceIOS(message);
		//System.out.println(jsonObject1);
		//System.out.println(jsonObject);
		try {
			//System.out.println(jsonObject.get("ret_code").toString());
			System.out.println(jsonObject1.get("ret_code").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
