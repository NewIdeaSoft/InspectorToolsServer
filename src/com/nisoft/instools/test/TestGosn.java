package com.nisoft.instools.test;

import java.util.ArrayList;

import com.google.gson.Gson;

public class TestGosn {

	public static void main(String[] args) {
		ArrayList<String> arraylist = new ArrayList<>();
		arraylist.add("1");
		arraylist.add("2");
		arraylist.add("3");
		arraylist.add("4");
		Gson gson = new Gson();
//		String s = gson.toJson(arraylist);
		String s = arraylist.toString();
		System.out.println(s);
//		ArrayList<String> a = (Arraylist <String>) gson.fromJson(s, arraylist);
	}

}
