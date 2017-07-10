package com.nisoft.instools.bean;

import java.util.ArrayList;
import java.util.Date;

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
    
    public ProblemDataPackage(String problemId){
        mProblem = new ProblemRecode(problemId);
        mAnalysis = new Recode(problemId);
        mProgram = new Recode(problemId);
        mResultRecode = new ImageRecode(problemId);
    }

    public ProblemDataPackage(String problemId, String problemImagesDirPath, String resultImagesDirPath){
    	Date date = new Date();
        mProblem = new ProblemRecode(problemId);
        mProblem.setDate(date);
        ArrayList<String> problemImagesName = FileUtils.getAllImagesName(problemImagesDirPath);
        mProblem.setImagesNameOnserver(problemImagesName);
        mAnalysis = new Recode(problemId);
        mAnalysis.setDate(date);
        mProgram = new Recode(problemId);
        mProgram.setDate(date);
        mResultRecode = new ImageRecode(problemId);
        mResultRecode.setDate(date);
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
