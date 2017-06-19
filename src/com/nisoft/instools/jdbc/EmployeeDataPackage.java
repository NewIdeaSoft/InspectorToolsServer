package com.nisoft.instools.jdbc;

import java.util.ArrayList;

public class EmployeeDataPackage {
	private Employee mEmployee;
    private ArrayList<OrgInfo> mOrgInfo;
    private ArrayList<String> mStations;
    private ArrayList<ArrayList<OrgInfo>> mOrgsInfoForSelect;
    private int mOrgLevels;

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

    public ArrayList<String> getStations() {
        return mStations;
    }

    public void setStations(ArrayList<String> stations) {
        mStations = stations;
    }

    public int getOrgLevels() {
        return mOrgLevels;
    }

    public void setOrgLevels(int orgLevels) {
        mOrgLevels = orgLevels;
    }

    public ArrayList<ArrayList<OrgInfo>> getOrgsInfoForSelect() {
        return mOrgsInfoForSelect;
    }

    public void setOrgsInfoForSelect(ArrayList<ArrayList<OrgInfo>> orgsInfoForSelect) {
        mOrgsInfoForSelect = orgsInfoForSelect;
    }
}
