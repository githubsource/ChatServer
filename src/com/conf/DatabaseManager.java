/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conf;

import static com.socket.Database.getTagValue;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author tranngocdien
 */
public class DatabaseManager {
    public MyDatabase db;
    
    public DatabaseManager(MyDatabase db) {
        this.db = db;
    }
    
     public boolean checkLogin(String username, String password){
        
        if(!userExists(username)){ return false; }
        
        return this.userExists(username, password);
    }
     
     public boolean userExists(String username){
        String sql = "select count(*) from users where username='"+ username +"' ";
           try {
               db.select(sql);
               while(db.rs.next()){
                   return db.rs.getInt(1) > 0 ;
               }
           } catch (Exception e) {
           }
        return true;
     }
     
     public boolean userExists(String username, String password){
       String sql = "select count(*) from users where username='"+ username +"'"
               + " and password='"+password+"' ";
           try {
               db.select(sql);
               while(db.rs.next()){
                   return db.rs.getInt(1) > 0 ;
               }
           } catch (Exception e) {
               
           }
        return false;
     
     }
     
     public String getPublicKey (String username) {
         String sql = "select public_key from users where username='"+ username+"' ";
            //   + " and password='"+password+"'";
           try {
               db.select(sql);
               while(db.rs.next()){
                   return db.rs.getString(1) ;
               }
           } catch (Exception e) {
               
           }
           
           return "";
     }
    
}
