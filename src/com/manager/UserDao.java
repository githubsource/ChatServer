/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manager;

import com.conf.MyDatabase;

/**
 *
 * @author tranngocdien
 */
public class UserDao {
    
    public static String tableName = "users";
    
    public static String COL_USERNAME = "username";
    public static String COL_PASSWORD = "password";
    public static String COL_DATE = "date";
    public static String COL_EMPLOYEE = "employee";
    public static String COL_EMPLOYEE_NAME = "employee_name";
    public static String COL_PUBLIC_KEY = "public_key";
    
    public UserDao () {
        
    }
    
}
