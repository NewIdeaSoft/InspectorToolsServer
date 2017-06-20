package com.nisoft.instools.utils;

import java.util.ArrayList;

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
		System.out.println("structure"+strings.size());
		return strings;
	}

}
