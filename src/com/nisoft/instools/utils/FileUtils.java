package com.nisoft.instools.utils;

import java.io.File;

public class FileUtils {
	public static String getFileType(String fileName){
		String [] strs = fileName.split("\\.");
        String fileType = strs[strs.length-1];
        return fileType;
	}
	public static String getFileType(File file){
		String fileName = file.getName();
		return getFileType(fileName);
	}
}
