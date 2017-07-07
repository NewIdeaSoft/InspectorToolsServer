package com.nisoft.instools.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ImageRecode extends Recode {
    private ArrayList<String> mImagesNameOnserver;

    public ImageRecode() {
    }
    
    public ImageRecode(String recodeId, String type, String author, Date date, String description, long updateTime,ArrayList<String> imagesName) {
		super(recodeId, type, author, date, description, updateTime);
		mImagesNameOnserver = imagesName;
	}

	public ImageRecode(String recodeId) {
        super(recodeId);
    }

	public ArrayList<String> getImagesNameOnserver() {
		return mImagesNameOnserver;
	}

	public void setImagesNameOnserver(ArrayList<String> imagesNameOnserver) {
		mImagesNameOnserver = imagesNameOnserver;
	}

    
}
