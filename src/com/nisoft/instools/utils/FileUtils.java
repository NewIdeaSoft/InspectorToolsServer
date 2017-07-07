package com.nisoft.instools.utils;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {
	public static String getFileType(String fileName) {
		String[] strs = fileName.split("\\.");
		String fileType = strs[strs.length - 1];
		return fileType;
	}

	public static String getFileType(File file) {
		String fileName = file.getName();
		return getFileType(fileName);
	}

	public static ArrayList<String> getAllImagesName(String imagesDirPath) {

		File dir = new File(imagesDirPath);
		ArrayList<String> names = new ArrayList<>();
		if (dir.exists()) {
			String[] filesName = dir.list();
			for (String name : filesName) {
				String fileType = FileUtils.getFileType(name);
				if (fileType.equals("jpg") || fileType.equals("bmp")) {
					names.add(name);
				}
			}
		}
		return names;
	}
}
