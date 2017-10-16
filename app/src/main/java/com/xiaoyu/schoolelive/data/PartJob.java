package com.xiaoyu.schoolelive.data;

import java.io.Serializable;

/**
 * Created by NeekChaw on 2017-08-02.
 */
public class PartJob implements Serializable{
    int workType;
    int wagesType;
    int wagesPay;
    int workManNeed;
    int workSexNeed;
    int hasJoinMan;
    String contactNum;
    String workWages;
    String workPlace;
    String workName;
    String workIntro;
    String workStartDate;
    String workEndDate;
    String workStartHours;
    String workEndHours;
    String workNeed;
    String pubWorkApartment;
    String infoPublishDate;
    String contactPerson;
    String job_id;//兼职的id
    String post_time;//发布时间

    public int getWorkManNeed() {
        return workManNeed;
    }

    public void setWorkManNeed(int workManNeed) {
        this.workManNeed = workManNeed;
    }

    public int getWorkSexNeed() {
        return workSexNeed;
    }

    public void setWorkSexNeed(int workSexNeed) {
        this.workSexNeed = workSexNeed;
    }

    public int getHasJoinMan() {
        return hasJoinMan;
    }

    public void setHasJoinMan(int hasJoinMan) {
        this.hasJoinMan = hasJoinMan;
    }

    public String getWorkStartHours() {
        return workStartHours;
    }

    public void setWorkStartHours(String workStartHours) {
        this.workStartHours = workStartHours;
    }

    public String getWorkEndHours() {
        return workEndHours;
    }

    public void setWorkEndHours(String workEndHours) {
        this.workEndHours = workEndHours;
    }

    public String getWorkNeed() {
        return workNeed;
    }

    public void setWorkNeed(String workNeed) {
        this.workNeed = workNeed;
    }

    public String getPubWorkApartment() {
        return pubWorkApartment;
    }

    public void setPubWorkApartment(String pubWorkApartment) {
        this.pubWorkApartment = pubWorkApartment;
    }

    public String getInfoPublishDate() {
        return infoPublishDate;
    }

    public void setInfoPublishDate(String infoPublishDate) {
        this.infoPublishDate = infoPublishDate;
    }

    public int getWagesPay() {
        return wagesPay;
    }

    public void setWagesPay(int wagesPay) {
        this.wagesPay = wagesPay;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public int getWagesType() {
        return wagesType;
    }

    public void setWagesType(int wagesType) {
        this.wagesType = wagesType;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkIntro() {
        return workIntro;
    }

    public void setWorkIntro(String workIntro) {
        this.workIntro = workIntro;
    }

    public String getWorkStartDate() {
        return workStartDate;
    }

    public void setWorkStartDate(String workStartDate) {
        this.workStartDate = workStartDate;
    }

    public String getWorkEndDate() {
        return workEndDate;
    }

    public void setWorkEndDate(String workEndDate) {
        this.workEndDate = workEndDate;
    }

    public String getWorkWages() {
        return workWages;
    }

    public void setWorkWages(String workWages) {
        this.workWages = workWages;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }
}
