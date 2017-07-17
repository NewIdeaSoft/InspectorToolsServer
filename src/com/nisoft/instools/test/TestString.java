package com.nisoft.instools.test;

import com.nisoft.instools.utils.StringsUtil;

public class TestString {

	public static void main(String[] args) {
		System.out.println(StringsUtil.compare("0001", "0002"));
		System.out.println(true);
		
		String num = "2017-05-11";
		int numInt = Integer.parseInt(num.replaceAll("-", ""));
		System.out.println(numInt);
	}

}
