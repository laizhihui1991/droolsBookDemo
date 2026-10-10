package com.test.drools.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 学生类
 *
 * @author laizhihui
 */
public class Students implements Serializable {
    /**
     * 学生名
     */
    private String name;
    /**
     * 分数
     */
    private int score;
    /**
     * 可以去的学校
     */
    private List<String> schoolList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(String school) {
        List<String> schoolList = this.getSchoolList();
        schoolList.add(school);
    }
    public void setSchoolList(List<String> schoolList) {
        this.schoolList=schoolList;
    }

    public Students() {
    }

    public Students(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public Students(String name, int score, List<String> schoolList) {
        this.name = name;
        this.score = score;
        this.schoolList = schoolList;
    }
}
