package com.goldenratio.commonweal.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * user informarion table
 * Created by lvxue on 2016/6/7 0007.
 */
public class User extends BmobObject {
    private String User_NO;//用户注册顺序
    private String User_ID;//用户ID随机产生，唯一（User+随机码）
    private String User_Name;//用户昵称
    private String User_Autograph;//用户个性签名
    private String User_Phone;//用户手机号
    private String User_Password;//用户密码
    private String User_Address;//用户地址
    private List User_Receive_Address;//用户收货地址
    private String User_Integral;//用户积分
    private String User_Money;//用户余额
    private boolean User_Is_V;//是否是大V用户
    private boolean User_Is_Real_Name;//是否实名认证
    private boolean User_Is_CW_Org;//是否是公益组织

    public void setUser_Is_V(boolean user_Is_V) {
        User_Is_V = user_Is_V;
    }

    public void setUser_Is_Real_Name(boolean user_Is_Real_Name) {
        User_Is_Real_Name = user_Is_Real_Name;
    }

    public String getUser_Autograph() {
        return User_Autograph;
    }

    public void setUser_Autograph(String user_Autograph) {
        User_Autograph = user_Autograph;
    }

    public void setUser_Is_CW_Org(boolean user_Is_CW_Org) {
        User_Is_CW_Org = user_Is_CW_Org;
    }

    public boolean isUser_Is_V() {

        return User_Is_V;
    }

    public boolean isUser_Is_Real_Name() {
        return User_Is_Real_Name;
    }

    public boolean isUser_Is_CW_Org() {
        return User_Is_CW_Org;
    }

    public String getUser_Money() {
        return User_Money;
    }

    public void setUser_Money(String user_Money) {
        User_Money = user_Money;
    }

    public void setUser_Address(String user_Address) {
        User_Address = user_Address;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public void setUser_Integral(String user_Integral) {
        User_Integral = user_Integral;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public void setUser_NO(String user_NO) {
        User_NO = user_NO;
    }

    public void setUser_Password(String user_Password) {
        User_Password = user_Password;
    }

    public void setUser_Phone(String user_Phone) {
        User_Phone = user_Phone;
    }

    public void setUser_Receive_Address(List user_Receive_Address) {
        User_Receive_Address = user_Receive_Address;
    }

    public String getUser_Address() {
        return User_Address;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public String getUser_Integral() {
        return User_Integral;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getUser_NO() {
        return User_NO;
    }

    public String getUser_Password() {
        return User_Password;
    }

    public String getUser_Phone() {
        return User_Phone;
    }

    public List getUser_Receive_Address() {
        return User_Receive_Address;
    }
}
