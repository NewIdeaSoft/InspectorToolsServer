package com.nisoft.instools.gson;

import com.nisoft.instools.bean.MaterialInspectRecode;

public class RecodeDataPackage {
	private String mName;
	private MaterialInspectRecode mRecode;
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public MaterialInspectRecode getRecode() {
		return mRecode;
	}
	public void setRecode(MaterialInspectRecode recode) {
		mRecode = recode;
	}
	
}
