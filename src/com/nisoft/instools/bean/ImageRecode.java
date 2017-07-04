package com.nisoft.instools.bean;

import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ImageRecode extends Recode {
    private String mImagesFolderPath;

    public ImageRecode() {
    }
    
    public ImageRecode(String recodeId, String type, String author, Date date, String description, long updateTime) {
		super(recodeId, type, author, date, description, updateTime);
	}

	public ImageRecode(String recodeId) {
        super(recodeId);
    }

    public String getImagesFolderPath() {
        return mImagesFolderPath;
    }

    public void setImagesFolderPath(String imagesFolderPath) {
        mImagesFolderPath = imagesFolderPath;
    }
}
