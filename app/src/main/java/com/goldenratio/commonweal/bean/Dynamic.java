package com.goldenratio.commonweal.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 冰封承諾Andy on 2016/8/6 0006.
 * 动态页面的信息
 */
public class Dynamic extends BmobObject{
    private String Dynamics_title;
    private List Dynamics_pic;
    private String Dynamics_location;
    private String Dynamics_time;
    private String Dynamics_name;
    private String Dynamics_uid;
    private String Dynamics_u_pic;

    public String getDynamics_u_pic() {
        return Dynamics_u_pic;
    }

    public void setDynamics_u_pic(String dynamics_u_pic) {
        Dynamics_u_pic = dynamics_u_pic;
    }

    public String getDynamics_name() {
        return Dynamics_name;
    }

    public void setDynamics_name(String dynamics_name) {
        Dynamics_name = dynamics_name;
    }

    public String getDynamics_time() {
        return Dynamics_time;
    }

    public void setDynamics_time(String dynamics_time) {
        Dynamics_time = dynamics_time;
    }

    public String getDynamics_uid() {
        return Dynamics_uid;
    }

    public void setDynamics_uid(String dynamics_uid) {
        Dynamics_uid = dynamics_uid;
    }

    public String getDynamics_location() {
        return Dynamics_location;
    }

    public void setDynamics_location(String dynamics_location) {
        Dynamics_location = dynamics_location;
    }

    public List getDynamics_pic() {
        return Dynamics_pic;
    }

    public void setDynamics_pic(List dynamics_pic) {
        Dynamics_pic = dynamics_pic;
    }

    public String getDynamics_title() {
        return Dynamics_title;
    }

    public void setDynamics_title(String dynamics_title) {
        Dynamics_title = dynamics_title;
    }
}