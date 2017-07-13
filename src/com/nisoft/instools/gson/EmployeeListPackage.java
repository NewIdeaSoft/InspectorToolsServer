package com.nisoft.instools.gson;

import java.util.ArrayList;

import com.nisoft.instools.bean.Employee;
import com.nisoft.instools.bean.OrgInfo;
import com.nisoft.instools.bean.PositionInfo;

public class EmployeeListPackage {
	private ArrayList<Employee> mEmployees;
	private ArrayList<OrgInfo> mOrgList;
	private ArrayList<PositionInfo> mPositionList;
	public ArrayList<Employee> getEmployees() {
		return mEmployees;
	}

	public void setEmployees(ArrayList<Employee> employees) {
		mEmployees = employees;
	}

	public ArrayList<OrgInfo> getOrgList() {
		return mOrgList;
	}

	public void setOrgList(ArrayList<OrgInfo> orgList) {
		mOrgList = orgList;
	}

	public ArrayList<PositionInfo> getPositionList() {
		return mPositionList;
	}

	public void setPositionList(ArrayList<PositionInfo> positionList) {
		mPositionList = positionList;
	}
	
}
