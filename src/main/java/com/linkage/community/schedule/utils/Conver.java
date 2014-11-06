package com.linkage.community.schedule.utils;

import java.sql.Clob;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conver {

	public static Calendar calendar = Calendar.getInstance();

	public static String convertGBK(String str) {
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO8859_1");
			String temp = new String(temp_t, "GB2312");
			return temp;
		} catch (Exception e) {
			return str;
		}
	}

	// 过滤空值
	public static String convertNull(Object objData) {
		String returnValue = "";
		if (objData == null)
			return returnValue;
		try {
			returnValue = objData.toString();
			returnValue = (returnValue.toLowerCase().equals("null")) ? ""
					: returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "";
		}
		return returnValue;
	}

	/**
	 * 类的方法定义:convertToNull(); 方法的说明: 将 String 对象转换 如果String长度为0 转换为null
	 * 
	 * @param source
	 *            被处理的字符串
	 * @return 处理后的字符串
	 */
	public static String convertToNull(String source) {
		if (source != null)
			if (source.trim().length() == 0)
				source = null;
		return source;
	}

	public static Integer convertInteger(Object objData) {
		Integer returnValue = null;
		try {
			returnValue = Integer.valueOf(objData.toString());
		} catch (Exception e) {
			returnValue = null;
		}
		return returnValue;
	}

	public static int convertInt(Object objData) {
		int returnValue = 0;
		try {
			returnValue = Integer.parseInt(objData.toString());
		} catch (Exception e) {
			returnValue = 0;
		}
		return returnValue;
	}

	public static Long convertLong(Object objData) {
		Long returnValue = null;
		try {
			returnValue = Long.valueOf(objData.toString());
		} catch (Exception e) {
			returnValue = null;
		}
		return returnValue;
	}

	public static double getFormatedDouble(double dbl) {
		double result = 0.00;
		try {
			DecimalFormat df = new DecimalFormat("##0.00"); // 保留两位小数点
			result = new Double(df.format(dbl)).doubleValue();
		} catch (Exception e) {
			result = dbl;
		}
		return result;
	}

	public static int getYear() // 获得年
	{
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth() // 获得月
	{
		return 1 + calendar.get(Calendar.MONTH);
	}

	public static int getLastMonth() // 获得上一个月
	{
		return calendar.get(Calendar.MONTH);
	}

	public static int getWeek() { // 获得周
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getDay() // 获得日
	{
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour() // 获得时
	{
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() // 获得分
	{
		return calendar.get(Calendar.MINUTE);
	}

	public static int getSecond() // 获得秒
	{
		return calendar.get(Calendar.SECOND);
	}

	public static int getMilliSecond() // 获得毫秒
	{
		return calendar.get(Calendar.MILLISECOND);
	}

	public static String getYearMonthDayNull() // 获得年月日 20010101
	{
		String yyyy = "0000", mm = "00", dd = "00";
		yyyy = yyyy + getYear();
		mm = mm + getMonth();
		dd = dd + getDay();
		yyyy = yyyy.substring(yyyy.length() - 4);
		mm = mm.substring(mm.length() - 2);
		dd = dd.substring(dd.length() - 2);
		return yyyy + "" + mm + "" + dd;
	}

	public static String getHourMinuteSecondNull() // 获得时分秒 010101
	{
		String hh = "00", mm = "00", ss = "00";
		hh = hh + getHour();
		mm = mm + getMinute();
		ss = ss + getSecond();
		hh = hh.substring(hh.length() - 2, hh.length());
		mm = mm.substring(mm.length() - 2, mm.length());
		ss = ss.substring(ss.length() - 2, ss.length());
		return hh + mm + ss;
	}

	/**
	 * 获取14位的时间值
	 * 
	 * @return
	 */
	public static String getChar14Time() // 获得年月日 20010101
	{
		return getYearMonthDayNull() + getHourMinuteSecondNull();
	}

	public static String getChar14TimeFormat() {
		return getChar14TimeFormat(System.currentTimeMillis());
	}
	
	public static String getChar14TimeFormat(long timestamp) {
		Date date = new Date(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static void main(String[] args) {
		// System.out.println(getFormatedFloat(5.00,2));
		// int ss = (int)58.01;
		// System.out.println(ss);
		System.out.println(getChar14TimeFormat()); 
	}

	public static String getFormatedFloat(double dbl, int type) {
		try {
			String decimal = "##0.00";
			switch (type) {
			case 0:
				decimal = "##0";
				break;
			case 1:
				decimal = "##0.0";
				break;
			case 2:
				decimal = "##0.00";
				break;
			case 3:
				decimal = "##0.000";
				break;
			case 4:
				decimal = "##0.0000";
				break;
			case 6:
				decimal = "##0.000000";
				break;
			case 7:
				decimal = "##0.0000000";
				break;
			}
			java.text.DecimalFormat df = new java.text.DecimalFormat(decimal);
			String str = df.format(dbl);
			return str;
		} catch (Exception e) {
			try {
				return new Double(dbl).toString();
			} catch (Exception se) {
				return "";
			}
		}
	}

	public static long convertLng(Object objData) {
		long returnValue = 0;
		try {
			returnValue = Long.parseLong(objData.toString());
		} catch (Exception e) {
			returnValue = 0;
		}
		return returnValue;
	}

	public static Double convertDouble(Object objData) {
		Double returnValue = null;
		try {
			returnValue = Double.valueOf(objData.toString());
		} catch (Exception e) {
			returnValue = new Double(0.0);
		}
		return returnValue;
	}

	public static double convertDbl(Object objData) {
		double returnValue = 0;
		try {
			returnValue = Double.parseDouble(objData.toString());
		} catch (Exception e) {
			returnValue = 0;
		}
		return returnValue;
	}

	public static String convertDate(Object objDate) {
		String returnValue = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			returnValue = df.format(objDate);
		} catch (Exception e) {
			returnValue = "";
		}
		return returnValue;
	}

	public static String convertDateHMS(Object objDate) {
		String returnValue = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			returnValue = df.format(objDate);
		} catch (Exception e) {
			returnValue = "";
		}
		return returnValue;
	}

	public static String convertUniteDate(Object objDate) {
		String returnValue = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			returnValue = df.format(objDate);
		} catch (Exception e) {
			returnValue = "";
		}
		return returnValue;
	}

	public static String convertUniteDateHMS(Object objDate) {
		String returnValue = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			returnValue = df.format(objDate);
		} catch (Exception e) {
			returnValue = "";
		}
		return returnValue;
	}

	public static Timestamp convertTimestamp(String strDate) {
		Timestamp returnValue = null;
		try {
			returnValue = Timestamp.valueOf(strDate);
		} catch (Exception e) {
		}
		return returnValue;
	}

	/**
	 * 将clob转换成String
	 */
	public static String getClobString(Object objData) {
		String returnString = "";
		try {
			Clob clobData = (java.sql.Clob) objData;
			returnString = clobData.getSubString((long) 1, (int) clobData
					.length());
		} catch (Exception e) {
			returnString = "";
		}
		return returnString;
	}

	

	public static Long createId() {
		Long returnString = new Long(0);
		java.util.Date now = new java.util.Date();
		long sj = now.getTime();
		sj = sj / 1000;
		int random = (int) (Math.random() * 1000);
		if (random < 10) {
			random *= 100;
		} else if (random < 100) {
			random *= 10;
		}
		String str = sj + String.valueOf(random);
		returnString = Conver.convertLong(str);
		return returnString;
	}

	/**
	 * @param strDate
	 * @param type
	 * @return
	 */
	public static Date getDate(String strDate, String type) {
		Date returnValue = null;
		SimpleDateFormat df = null;
		if (strDate == null || strDate.equalsIgnoreCase("") || type == null)
			return returnValue;
		String dateType = type.toLowerCase();

		if (dateType.equals("yyyy-mm-dd"))
			df = new SimpleDateFormat("yyyy-MM-dd");
		else if (dateType.equals("yyyy-mm-dd hh"))
			df = new SimpleDateFormat("yyyy-MM-dd hh");
		else if (dateType.equals("yyyy-mm-dd hh:mm"))
			df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		else if (dateType.equals("yyyy-mm-dd hh:mm:ss"))
			df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		else
			df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			returnValue = df.parse(strDate);
		} catch (Exception e) {
			System.out.println("getDate() in Conver.java error!!" + strDate
					+ type);
			returnValue = null;
		}
		return returnValue;
	}

	/**
	 * 截取字符串
	 * 
	 * @param str
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public static String getSubString(String str, int beginIndex, int endIndex) {
		String retValue = "";
		if (str == null || "".equals(str))
			return retValue;
		if (str.length() < beginIndex || endIndex < beginIndex)
			return retValue;
		if (str.length() > endIndex) {
			retValue = str.substring(beginIndex, endIndex);
			retValue += "…";
		} else
			retValue = str.substring(beginIndex);

		return retValue;
	}

	/**
	 * 故障工单状态
	 * 
	 * @param state
	 * @return
	 */
	public static String returnState(String state) {
		String retValue = "";
		if (state == null || "".equals(state))
			return retValue;
		if (state.equals("E0P"))
			retValue = "待审";
		else if (state.equals("E0C"))
			retValue = "创建";
		else if (state.equals("E0D"))
			retValue = "处理中";
		else if (state.equals("E0T"))
			retValue = "<font color='red'>超时</font>";
		else if (state.equals("E0X"))
			retValue = "废单";
		else if (state.equals("E0H"))
			retValue = "归档";
		else if (state.equals("E0R"))
			retValue = "异常";
		else if (state.equals("E0L"))
			retValue = "已申请入库";

		return retValue;
	}

	// 输入精确的位数，四舍五入处理
	public static String convertDblNum(Object objData, int num) {
		double returnValue = 0;
		String retVal = "0";
		String simple = "#.";
		if (num == 0)
			simple = "#";
		for (int i = 0; i < num; i++) {
			simple += "0";
		}
		try {
			returnValue = Double.parseDouble(objData.toString());
			if (returnValue != 0) {
				DecimalFormat df = new DecimalFormat(simple);
				retVal = df.format(returnValue);
			}
		} catch (Exception e) {
			returnValue = 0;
		}
		if (0 < Double.parseDouble(retVal) && Double.parseDouble(retVal) < 1)
			retVal = retVal.replaceAll("\\.", "0\\.").replaceAll("-", "");
		return retVal;
	}

	// 脚本转换
	public static String convertEscape(String regex, String replacement) {
		if ("".equals(replacement) || "".equals(regex)) {
			return replacement;
		}
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(replacement);
		Set set = new HashSet();
		while (mt.find()) {
			set.add(mt.group());
		}
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String obj = (String) iterator.next();
			replacement = replacement.replaceAll(obj, "&#"
					+ (int) obj.charAt(0) + ";");
		}
		return replacement;
	}

	public static int IsRN(String strDateY1, int strDateM) {
		int m = 0;
		int strDateY = Integer.parseInt(strDateY1);
		if (strDateM == 2) {
			if (strDateY % 4 == 0) {
				if (strDateY % 100 != 0) {
					m = 29;
				} else if (strDateY % 400 == 0) {
					m = 29;
				} else
					m = 28;
			} else {
				m = 28;
			}
		}
		return m;
	}

	/**
	 * param1 Object strObj param2 int index1 表示截取的开始的位置 param3 int index2
	 * 表示截取的结束位置，若为0表示到整个字符串的结尾 param4 boolean flag
	 * 值为true表示截取的字符串后加省略号，为false则不加 return String author zhaoxf time 2008-11-26
	 * comments 字符串截取函数
	 */
	public static String subStr(Object strObj, int index1, int index2,
			boolean flag) {
		String retStr = "";
		if (strObj != null) {
			String str = strObj.toString();
			int len = str.length();
			if (index1 < len && index2 == 0) {
				retStr = str.substring(index1);
				if (flag)
					retStr += "...";
			} else if (index1 < len && index2 < len && index1 < index2) {
				retStr = str.substring(index1, index2);
				if (flag)
					retStr += "...";
			} else {
				retStr = str;
			}
		}
		return retStr;
	}

	public static String get(java.util.Date dtDate, String strType) {

		if (null == dtDate)
			return "";

		int iYear = dtDate.getYear() + 1900;
		String sMonth = (dtDate.getMonth() >= 9) ? ("" + (dtDate.getMonth() + 1))
				: ("0" + (dtDate.getMonth() + 1));
		String sDate = (dtDate.getDate() > 9) ? ("" + dtDate.getDate())
				: ("0" + dtDate.getDate());

		String strValue = "";
		try {
			if (strType.equals("yyyy/mm/dd")) {
				strValue = iYear + "/" + sMonth + "/" + sDate;
			} else if (strType.equals("yyyy-mm-dd")) {
				strValue = iYear + "-" + sMonth + "-" + sDate;
			} else if (strType.equals("yyyymmdd")) {
				strValue = "" + iYear + sMonth + sDate;
			} else if (strType.equals("yyyy-mm")) {
				strValue = iYear + "-" + sMonth;
			} else if (strType.equals("yyyy年mm月dd日")) {
				strValue = iYear + "年" + sMonth + "月" + sDate + "日";
			}

			else if (strType.equals("yyyy/mm/dd hh:mm:ss")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				String strSeconds = (dtDate.getSeconds() > 9) ? ("" + dtDate
						.getSeconds()) : ("0" + dtDate.getSeconds());
				strValue = iYear + "/" + sMonth + "/" + sDate + " " + strHours
						+ ":" + strMinutes + ":" + strSeconds;
			}

			else if (strType.equals("yyyy-mm-dd hh:mm")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				strValue = iYear + "-" + sMonth + "-" + sDate + " " + strHours
						+ ":" + strMinutes;
			}

			else if (strType.equals("yyyy-mm-dd hh:mm:ss")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				String strSeconds = (dtDate.getSeconds() > 9) ? ("" + dtDate
						.getSeconds()) : ("0" + dtDate.getSeconds());
				strValue = iYear + "-" + sMonth + "-" + sDate + " " + strHours
						+ ":" + strMinutes + ":" + strSeconds;
			}

			else if (strType.equals("yyyy年mm月dd日hh时mm分ss秒")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				String strSeconds = (dtDate.getSeconds() > 9) ? ("" + dtDate
						.getSeconds()) : ("0" + dtDate.getSeconds());
				strValue = iYear + "年" + sMonth + "月" + sDate + "日" + strHours
						+ "时" + strMinutes + "分" + strSeconds + "秒";
			}

			else if (strType.equals("yyyy年mm月dd日hh时mm分")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				String strSeconds = (dtDate.getSeconds() > 9) ? ("" + dtDate
						.getSeconds()) : ("0" + dtDate.getSeconds());
				strValue = iYear + "年" + sMonth + "月" + sDate + "日" + strHours
						+ "时" + strMinutes + "分";
			} else if (strType.equals("yyyy年mm月dd日  hh:mm")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				String strSeconds = (dtDate.getSeconds() > 9) ? ("" + dtDate
						.getSeconds()) : ("0" + dtDate.getSeconds());
				strValue = iYear + "年" + sMonth + "月" + sDate + "日  "
						+ strHours + ":" + strMinutes;
			} else if (strType.equals("dd日hh时mm分ss秒")) {
				String strHours = (dtDate.getHours() > 9) ? ("" + dtDate
						.getHours()) : ("0" + dtDate.getHours());
				String strMinutes = (dtDate.getMinutes() > 9) ? ("" + dtDate
						.getMinutes()) : ("0" + dtDate.getMinutes());
				String strSeconds = (dtDate.getSeconds() > 9) ? ("" + dtDate
						.getSeconds()) : ("0" + dtDate.getSeconds());
				strValue = sDate + "日" + strHours + "时" + strMinutes + "分"
						+ strSeconds + "秒";

			}
		} catch (Exception e) {
			strValue = "";
		}
		return strValue;
	}
}
