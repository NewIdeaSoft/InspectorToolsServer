package com.nisoft.instools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static Gson getDateFormatGson(){
		return new GsonBuilder()
				.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>())
				.setDateFormat("yyyy-MM-dd HH:mm")
				.create();
	}
}
