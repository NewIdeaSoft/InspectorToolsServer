package com.nisoft.instools.bean.product;

/**
 * Created by Administrator on 2017/7/26.
 */

public class WorkType {
    private String mBranch;
    private String mTypeId;
    private String mName;
    private String mCompanyId;

    public String getBranch() {
        return mBranch;
    }

    public void setBranch(String branch) {
        mBranch = branch;
    }

    public String getTypeId() {
        return mTypeId;
    }

    public void setTypeId(String typeId) {
        mTypeId = typeId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }
}
