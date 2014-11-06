package com.linkage.community.schedule.qiniu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.linkage.community.schedule.utils.Conver;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.fop.ExifRet;
import com.qiniu.api.fop.ImageExif;
import com.qiniu.api.fop.ImageInfo;
import com.qiniu.api.fop.ImageInfoRet;
import com.qiniu.api.fop.ImageView;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.rs.PutPolicy;

public class QiniuFileUtils {
	static {
		Config.ACCESS_KEY = "6ifKr52_2r1jXzD-daEjE_1miSFnJLa1HokwaNYH";
		Config.SECRET_KEY = "PLsOaVeCbfbrdxm2cfRVHtGmFSprmjSgwrrh--9H";
	}
	
	private static Object lock = new Object();
	
	private static String[] buckets = {"zlzwimg"};

	private static Map tokens = new HashMap();
	
	// 获取上传凭证
	public static String getUpToken(String bucketName) {
//		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
//		PutPolicy putPolicy = new PutPolicy(bucketName);
//		// 指定上传策略 。。。。。
//		String uptoken = putPolicy.token(mac);
//		return uptoken;
		synchronized(lock) {
			if(!tokens.containsKey(bucketName))
				return null;
			return Conver.convertNull(tokens.get(bucketName));
		}
	}
	
	public static void generateTokens() throws Exception {
		synchronized(lock) {
			tokens.clear();
			Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
			for(int i = 0; i < buckets.length; i++) {
				PutPolicy putPolicy = new PutPolicy(buckets[i]);
				String uptoken = putPolicy.token(mac);
				tokens.put(buckets[i], uptoken);
			}
		}
	}

	// 上传文件
	public static String uploadFile(String uptoken, String key, File file,
			PutExtra extra) {
		String fileName = "";
		PutRet ret = IoApi.putFile(uptoken, key, file, extra);
		if (ret.ok()) // 文件上传成功
			fileName = ret.getKey();
		else
			fileName = "";
		return fileName;
	}

	public static void main(String[] args) throws Exception {
		// String uptoken = getUpToken("zlzwimg");
		// System.out.println(uptoken);
		/*String uptoken = "6ifKr52_2r1jXzD-daEjE_1miSFnJLa1HokwaNYH:nE71Wh_2ohMLy98-c2Cn19Ky5qU=:eyJzY29wZSI6InpsendpbWciLCJkZWFkbGluZSI6MTQwNjc3OTYyNX0=";
		File file = new File(
				"C:\\Users\\Public\\Pictures\\Sample Pictures\\8.jpg");
		PutExtra extra = new PutExtra();
		System.out.println(uploadFile(uptoken, "personimage/", file, extra));*/
		String url = "http://zlzwimg.qiniudn.com/FgHGloZ7sYhVZDOearSLFjwrp4vO";
        ImageInfoRet ret = ImageInfo.call(url);
        ExifRet ret2 = ImageExif.call(url);
		System.out.println(ret.height);
		System.out.println(ret.width);
		
		ImageView iv = new ImageView();
        iv.mode = 1 ;
        iv.width = 100 ;
        iv.height = 200 ;
        iv.quality = 1 ;
        iv.format = "jpg" ;
        CallRet ret3 = iv.call(url);
	}

}
