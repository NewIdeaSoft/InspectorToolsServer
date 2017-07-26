package com.nisoft.instools.bean.product;

/**
 * Created by Administrator on 2017/7/26.
 */

public class Product {
    private String mName;
    private String mProductId;
    private String mCompanyId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }
}
