package com.nisoft.instools.gson;

import java.util.ArrayList;

import com.nisoft.instools.bean.Company;
import com.nisoft.instools.bean.Employee;
import com.nisoft.instools.bean.OrgInfo;

public class EmployeeDataPackage {
	private Employee mEmployee;
    private ArrayList<OrgInfo> mOrgInfo;
    private ArrayList<ArrayList<OrgInfo>> mOrgsInfoForSelect;
    public Employee getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Employee employee) {
        mEmployee = employee;
    }

    public ArrayList<OrgInfo> getOrgInfo() {
        return mOrgInfo;
    }

    public void setOrgInfo(ArrayList<OrgInfo> orgInfo) {
        mOrgInfo = orgInfo;
    }

    public ArrayList<ArrayList<OrgInfo>> getOrgsInfoForSelect() {
        return mOrgsInfoForSelect;
    }

    public void setOrgsInfoForSelect(ArrayList<ArrayList<OrgInfo>> orgsInfoForSelect) {
        mOrgsInfoForSelect = orgsInfoForSelect;
    }
}
