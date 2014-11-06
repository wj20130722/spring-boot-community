package com.linkage.community.schedule.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 服务端以http方式调用微信接口通用类的封装
 * 1.通过httpclient插件实现http客户端的访问(GET与POST方式)
 * 2.微信相关接口必须的凭证获取的接口调用
 * @author wangjie
 *
 */
public class CommonHttpUtil
{
    public static final String BAIDU_LBS_URL = "http://api.map.baidu.com/place/v2/search?ak=CyiYlB7AfTo7sa6ojNTxRoS2&output=xml&query=QUERY&page_size=PAGESIZE&page_num=PAGENUM&scope=2&region=REGION";
    
	private static final Log log = LogFactory.getLog(CommonHttpUtil.class);
	
	public static CloseableHttpClient getHttpClientInstance()
	{
		return HttpClients.createDefault();
	}
	
	public static <T extends Header> void addHeader(List<T> headers,T header)
	{
		if(null == headers)
		{
			headers = new ArrayList<T>();
		}
		headers.add(header);
	}
	
	public static  void initCommonHeader(List<BasicHeader> headers)
	{
		if(null == headers)
		{
			headers = new ArrayList<BasicHeader>();
		}
		addHeader(headers,new BasicHeader("Accept", "application/xml, application/xhtml+xml, text/html,*/*"));
		addHeader(headers,new BasicHeader("Accept-Encoding", "gzip, deflate"));
		addHeader(headers,new BasicHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"));
		addHeader(headers,new BasicHeader("Connection", "keep-alive"));
		addHeader(headers,new BasicHeader("Cache-Control", "no-cache"));
		addHeader(headers,new BasicHeader("Pragma", "no-cache"));
//		addHeader(headers,new BasicHeader("Host", "api.map.baidu.com"));
		addHeader(headers,new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0"));
	}
	
	public static HttpGet getGetMethod(String uri,List<BasicHeader> headers)
	{
		initCommonHeader(headers);
		HttpGet httpget = new HttpGet(uri);
		RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpget.setConfig(config);
		for (final BasicHeader header : headers)
    {
			httpget.addHeader(header);
    }
		return httpget;
	}
	
	
	public static HttpPost getPostMethod(String uri,List<BasicHeader> headers,HttpEntity httpEntity)
	{
		initCommonHeader(headers);
		HttpPost httppost = new HttpPost(uri);
		for (final Header header : headers)
    {
			httppost.addHeader(header);
    }
		httppost.setEntity(httpEntity);
		return httppost;
	}
	
	
	public static HttpPut getPutMethod(String uri,List<BasicHeader> headers,HttpEntity httpEntity)
	{
		initCommonHeader(headers);
		HttpPut httpput = new HttpPut(uri);
		for (final Header header : headers)
    {
			httpput.addHeader(header);
    }
		httpput.setEntity(httpEntity);
		return httpput;
	}
	
	public static JSONArray callHttpClient4(CloseableHttpClient httpClient,HttpUriRequest request)
	{
		CloseableHttpResponse response = null;
		JSONArray result = null;
		try
    {
	    response = httpClient.execute(request);
	    String jsonStr = Conver.convertNull(EntityUtils.toString(response.getEntity(), "utf-8"));
	    
	    result = JSON.parseArray(jsonStr);
    }
    catch (ClientProtocolException e)
    {
	    e.printStackTrace();
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
      {
	      response.close();
	      //httpClient.close();
      }
      catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
		return result;
	}
	
	public static String callHttpClient3(CloseableHttpClient httpClient,HttpUriRequest request)
	{
		CloseableHttpResponse response = null;
		String result = null;
		try
    {
	    response = httpClient.execute(request);
	    result = EntityUtils.toString(response.getEntity(), "utf-8");
    }
    catch (ClientProtocolException e)
    {
	    e.printStackTrace();
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
      {
	      response.close();
	      //httpClient.close();
      }
      catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
		return result;
	}
	
	public static JSONObject callHttpClient2(CloseableHttpClient httpClient,HttpUriRequest request)
	{
		CloseableHttpResponse response = null;
		JSONObject result = null;
		try
    {
	    response = httpClient.execute(request);
	    String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
	    result = JSON.parseObject(jsonStr);
    }
    catch (ClientProtocolException e)
    {
	    e.printStackTrace();
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
      {
	      response.close();
	      //httpClient.close();
      }
      catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
		return result;
	}
	
	public static Document callHttpClient(CloseableHttpClient httpClient,HttpUriRequest request)
	{
		CloseableHttpResponse response = null;
		Document result = null;
		try
    {
	    response = httpClient.execute(request);
	    String xml = EntityUtils.toString(response.getEntity(), "utf-8");
	    //log.info("返回的xml�?"+xml);
	    result = DocumentHelper.parseText(xml);
    }
    catch (ClientProtocolException e)
    {
	    e.printStackTrace();
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
      {
	      response.close();
	     // httpClient.close();
      }
      catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
		return result;
	}
	
}
