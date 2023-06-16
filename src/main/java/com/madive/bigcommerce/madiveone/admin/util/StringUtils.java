package com.madive.bigcommerce.madiveone.admin.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class StringUtils extends org.apache.commons.lang.StringUtils {
	public static boolean isNotEmpty(Object o) {
		return (isEmpty((String) o) == Boolean.FALSE);
	}

	public static boolean isNull(Object o) {
		return (o == null);
	}

	public static boolean isNotNull(Object o) {
		return (isNull(o) == Boolean.FALSE);
	}

	/**
     * 문자열이 있는지 확인한다.<br>
     * Oracle nvl() 과 비슷한 기능을 한다.
     * null 일 경우, "" (빈스트링)을 리턴한다.
     *
     * @param String val 비교대상 문자열
     * @return String
     */
	public static String nvl(String val) {
		return isNull(val) ? "" : val.trim();
    }

    /**
     * 비교대상 문자열이 null 또는 empty 일 경우 대체값을 리턴한다.
     *
     * @param String val 비교대상 문자열
     * @param String rep 대체값
     * @return String
     */
	public static String nvl( String val, String rep ) {
        return isEmpty(val) ? rep : val.trim();
    }
	
	public static int ivl(String str) {
		return ivl(str, 0);
	}
	
	public static int ivl(String str1, int rtn)	{
		return isEmpty(str1) ? rtn : Integer.parseInt(str1);
	}
	
	public static int length(String[] o){
		return o == null ? 0 : o.length;
	}
	
	public static String arrayJoin(String glue, String array[]) {
		String result = "";

		for (int i = 0; i < array.length; i++) {
			result += array[i];
			if (i < array.length - 1) result += glue;
		}
		return result;
	}
	
	public static boolean isStringArray(Object obj) {
		if(obj instanceof String[] ){
			return true;
		}
		return false;
	}
	
	public static int countChar(String str, char ch) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch) {
				count++;
			}
		}
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public static Boolean empty(Object obj) {
		if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List) return obj == null || ((List)obj).isEmpty();
		else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
		else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
		else return obj == null;
	}
	
	public static Boolean notEmpty(Object obj) {
		return !empty(obj);
	}
}