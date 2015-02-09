/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conf;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @see http://www.tutorialspoint.com/jdbc/jdbc-sorting-data.htm
 * @author tranngocdien
 */
public class MyDatabase {
    Connection con = null;
    Statement st = null;
    public ResultSet rs = null;

    String url = "jdbc:mysql://localhost:3306/chat";
    String user = "root";
    String password = "";
    
    
    public MyDatabase(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    
    public boolean connect() {
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from users limit 1");

            if (rs.next()) {
                System.out.println("check select from users table " + rs.getString(5));
            }
        } catch (Exception e) {
            return false;
        }
         return true;
    }
    
    public void close() {
        try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

        } catch (Exception ex) {
        }
    }
    
    public boolean query (String query) {
      //String query = "ALTER TABLE employees DROP COLUMN last_name";
        try {
          return st.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(MyDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public int insert(String sql) throws SQLException {
       // st = con.createStatement();
        st.executeUpdate(sql);
        
        select("select last_insert_id()");
        rs.next();
        return rs.getInt(1);

    }
    
    public int update(String sql) throws SQLException {
       // st = con.createStatement();
        //String sql = "UPDATE Registration " + "SET age = 30 WHERE id in (100, 101)";
        return st.executeUpdate(sql);
    }
    
    public ResultSet select(String sql) throws SQLException {
         //sql = "SELECT id, first, last, age FROM Registration";
         rs = st.executeQuery(sql);
         return rs;
    }
    
    public int delete(String sql) throws SQLException {
      //String sql = "DELETE FROM Registration " + "WHERE id = 101";
       return st.executeUpdate(sql);
    }
    
    public void testIterator() throws SQLException {
        String sql = "SELECT id, first, last, age FROM Registration";
        ResultSet rs = st.executeQuery(sql);

        while(rs.next()){
         //Retrieve by column name
         int id  = rs.getInt("id");
         int age = rs.getInt("age");
         String first = rs.getString("first");
         String last = rs.getString("last");

         //Display values
         System.out.print("ID: " + id);
         System.out.print(", Age: " + age);
         System.out.print(", First: " + first);
         System.out.println(", Last: " + last);
      }
      rs.close();
    }
}
