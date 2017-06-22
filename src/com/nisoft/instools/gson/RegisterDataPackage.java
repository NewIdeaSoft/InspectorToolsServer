package com.nisoft.instools.gson;

import java.util.ArrayList;

import com.nisoft.instools.bean.Company;

public class RegisterDataPackage {
	ArrayList<Company> mCompanies;

    public ArrayList<Company> getCompanies() {
        return mCompanies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        mCompanies = companies;
    }
}
