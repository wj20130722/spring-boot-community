package com.linkage.community.schedule.utils;

import java.net.UnknownHostException;
import java.util.ArrayList;



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;

public class WeimiSms {
	
	    public static String postHTML(String url, String[][] data, int retry,String charset) {
	        String r = "";
	        HttpClient httpClient = null;
	        PostMethod postMethod = null;
	
	        try {
	            httpClient = new HttpClient();
	            postMethod = new PostMethod(url);
	            postMethod.getParams().setParameter(
	                    HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
	
	            if (data != null) {
	                NameValuePair[] values = new NameValuePair[data.length];
	                for (int i = 0; i < data.length; i++) {
	                    String[] d = data[i];
	                    if (d == null || d.length < 2)
	                        continue;
	                    values[i] = new NameValuePair(d[0], d[1]);
	                }
	                postMethod.setRequestBody(values);
	            }
	
	            int retval = httpClient.executeMethod(postMethod);
	
	            if (retval == HttpStatus.SC_OK) {
	                r = postMethod.getResponseBodyAsString();
	                if (r != null)
	                    r = r.trim();
	            } else if (retval == HttpStatus.SC_REQUEST_TIMEOUT) {
	                r = null;
	            } else {
	                r = null;
	            }
	
	        } catch (UnknownHostException e) {
	            if (retry >= 10) {
	                return null;
	            }
	            try {
	                Thread.sleep(500); // 休息0.5秒，10次共5秒
	            } catch (InterruptedException e1) {
	            }
	            return postHTML(url, data, retry + 1, charset);
	        } catch (Exception ex) {
	            // 有异常返回
	            r = null;
	        } finally {
	            try {
	                if (null != postMethod && postMethod.hasBeenUsed()) {
	                    postMethod.releaseConnection();
	                }
	            } catch (Exception ex) {
	                // logger.error("释放HTTP连接失败");
	            }
	        }
	        return r;
	
	    }
	
	    public static void main(String[] a) {
	
	        String[][] data = null;
	        ArrayList<String[]> para = new ArrayList<String[]>();
	        para.add(new String[] { "cid", "8FVqSlzvKXFF" });
	        para.add(new String[] { "mob", "18351448461" });
	        para.add(new String[] { "uid", "8xHNZ03Rd447" });
	        para.add(new String[] { "pas", "cmnk9z2v" });
	        para.add(new String[] { "p1", "8888" });
	        para.add(new String[] { "type", "json" });
	        data = new String[para.size()][];
	        para.toArray(data);
	        JSONObject json=JSONObject.parseObject(WeimiSms.postHTML("http://api.weimi.cc/2/sms/send.html",data, 3, "UTF-8"));
			int code = json.getIntValue("code");
			System.out.println(code);
			System.out.println(json.toJSONString());
			
	    }

}
