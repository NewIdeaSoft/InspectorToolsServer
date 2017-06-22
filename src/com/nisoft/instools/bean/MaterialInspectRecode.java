package com.nisoft.instools.bean;

import java.util.ArrayList;
import java.util.Date;

import com.nisoft.instools.utils.StringsUtil;

public class MaterialInspectRecode {
	private String mJobNum;
    private ArrayList<String> mPicPath;
    private Date mDate;
    private String mType;
    private String mDescription;

    public String getJobNum() {
        return mJobNum;
    }

    public void setJobNum(String jobNum) {
        mJobNum = jobNum;
    }

    public ArrayList<String> getPicPath() {
        return mPicPath;
    }

    public void setPicPath(ArrayList<String> picPath) {
        mPicPath = picPath;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        String data = "";
        data = "检验编号：" + mJobNum + separator;
        data = data + "检验时间：" + StringsUtil.dateFormat(mDate) + separator;
        data = data + "工作描述：" + mDescription + separator;
        return data;
    }
}
