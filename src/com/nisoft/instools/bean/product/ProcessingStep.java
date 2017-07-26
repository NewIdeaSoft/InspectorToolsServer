package com.nisoft.instools.bean.product;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ProcessingStep {
    private String mStepId;
    private String mLastStepId;
    private String mName;
    private String mComponentsId;
    private ArrayList<String> mSkillLabels;

    public String getStepId() {
        return mStepId;
    }

    public void setStepId(String stepId) {
        mStepId = stepId;
    }

    public String getLastStepId() {
        return mLastStepId;
    }

    public void setLastStepId(String lastStepId) {
        mLastStepId = lastStepId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getComponentsId() {
        return mComponentsId;
    }

    public void setComponentsId(String componentsId) {
        mComponentsId = componentsId;
    }

    public ArrayList<String> getSkillLabels() {
        return mSkillLabels;
    }

    public void setSkillLabels(ArrayList<String> skillLabels) {
        mSkillLabels = skillLabels;
    }
}
