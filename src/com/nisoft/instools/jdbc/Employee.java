package com.nisoft.instools.jdbc;

import java.util.ArrayList;

public class Employee {
	private String mName;
	private String mPhone;
	private String mWorkNum;
	private String mOrgId;
	private ArrayList<String> mPositionsId;
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public String getPhone() {
		return mPhone;
	}
	public void setPhone(String number) {
		mPhone = number;
	}
	public String getWorkNum() {
		return mWorkNum;
	}
	public void setWorkNum(String workNum) {
		mWorkNum = workNum;
	}
	public String getOrgId() {
		return mOrgId;
	}
	public void setOrgId(String orgId) {
		mOrgId = orgId;
	}
	public ArrayList<String> getPositionsId() {
		return mPositionsId;
	}
	public void setPositionsId(ArrayList<String> positionsId) {
		mPositionsId = positionsId;
	}
	
}
