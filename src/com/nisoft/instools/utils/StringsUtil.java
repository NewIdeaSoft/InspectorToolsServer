package com.nisoft.instools.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StringsUtil {

	public static ArrayList<String> getStrings(String s) {
		ArrayList<String> strings = new ArrayList<>();
		if (s != null) {
			if (s.startsWith("[")) {
				s = s.substring(1, s.length() - 1);
			}
			String[] ss = s.split(",");
			for (String s1 : ss) {
				s1 = s1.trim();
				System.out.println(s1);
				strings.add(s1);
			}
		}
		return strings;
	}
	
	public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = format.format(date);
        return dateString;
    }
	
	public static String dateFormatForNum(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String dateString = format.format(date);
        return dateString;
	}
	
	public static boolean compare(String s1,String s2){
		int i1 = Integer.parseInt(s1);
		int i2 = Integer.parseInt(s2);
		if(i1>i2){
			return true;
		}
		return false;
	}
}
