package com.nisoft.instools.bean;

import java.util.ArrayList;

import com.nisoft.instools.utils.FileUtils;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ProblemDataPackage {
    private ProblemRecode mProblem;
    private Recode mAnalysis;
    private Recode mProgram;
    private ImageRecode mResultRecode;

    public ProblemDataPackage() {
    }

    public ProblemDataPackage(String problemId, String problemImagesDirPath, String resultImagesDirPath){
        mProblem = new ProblemRecode(problemId);
        ArrayList<String> problemImagesName = FileUtils.getAllImagesName(problemImagesDirPath);
        mProblem.setImagesNameOnserver(problemImagesName);
        mAnalysis = new Recode(problemId);
        mProgram = new Recode(problemId);
        mResultRecode = new ImageRecode(problemId);
        ArrayList<String> resultImagesName = FileUtils.getAllImagesName(resultImagesDirPath);
        mResultRecode.setImagesNameOnserver(resultImagesName);
    }

    public ProblemRecode getProblem() {
        return mProblem;
    }

    public void setProblem(ProblemRecode problem) {
        mProblem = problem;
    }

    public Recode getAnalysis() {
        return mAnalysis;
    }

    public void setAnalysis(Recode analysis) {
        mAnalysis = analysis;
    }

    public Recode getProgram() {
        return mProgram;
    }

    public void setProgram(Recode program) {
        mProgram = program;
    }

    public ImageRecode getResultRecode() {
        return mResultRecode;
    }

    public void setResultRecode(ImageRecode resultRecode) {
        mResultRecode = resultRecode;
    }
}
