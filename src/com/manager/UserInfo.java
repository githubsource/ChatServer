/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manager;

/**
 *
 * @author tranngocdien
 */
public class UserInfo {
    public int id = 0;
    public String username;
    public String password;
    public String employee;
    public String date;
    public String employeeName;
    public String publicKey;
    
    
   public UserInfo(String username, String password, String employee, String date, String employeeName, String publicKey) {
        this.username = username;
        this.password = password;
        this.employee = employee;
        this.date = date;
        this.employeeName = employeeName;
        this.publicKey = publicKey;
        
        this.id = 0;
    }

    public UserInfo(int id, String username, String password, String employee, String date, String employeeName, String publicKey) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.employee = employee;
        this.date = date;
        this.employeeName = employeeName;
        this.publicKey = publicKey;
    }
}
