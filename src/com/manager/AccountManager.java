/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manager;

import com.conf.MyDatabase;
import com.manager.UserDao;
import com.model.UserInfo;
import com.manager.AccountManagerFrame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author tranngocdien
 */
public class AccountManager {

    AccountManagerFrame accountMngFrm;
    MyDatabase db;
    UserDao userDao;
    
    public AccountManager(AccountManagerFrame frm, MyDatabase db) {
        this.accountMngFrm = frm;
        this.db = db;
        userDao = new UserDao();
    }
    
    public boolean isExits(String username) throws Exception{
        String sql = "select count(*) from users where username='"+ username +"'";
        db.select(sql);
        
        while(db.rs.next()){
            return db.rs.getInt(1) > 0 ;
        }
        
        return true;
    }
    
    public int add(String username, String password, String employee, String date
                        , String employeeName, String publicKey) throws SQLException {
        String sql = "insert into " + UserDao.tableName
                + "(username, password, employee, date, employee_name, public_key)"
                + " values ("
                    + "'" + username +"',"
                    + "'" + password +"',"
                    + "'" + employee +"',"
                    + "'" + date +"',"
                    + "'" + employeeName +"',"
                    + "'" + publicKey +"'"
                + ")";
        
        int id = db.insert(sql);
        System.out.println(id);
        return id;
    }
    
    public int add(UserInfo user) throws SQLException {
         String sql = "insert into " + UserDao.tableName
                + "(username, password, employee, date, employee_name, public_key)"
                + " values ("
                    + "'" + user.username +"',"
                    + "'" + user.password +"',"
                    + "'" + user.employee +"',"
                    + "'" + user.date +"',"
                    + "'" + user.employeeName +"',"
                    + "'" + user.publicKey +"'"
                + ")";
        
        int id = db.insert(sql);
        System.out.println(id);
        return id;
    }
    
     public void update(int id, String username, String password, String employee, String date
                        , String employeeName, String publicKey) throws SQLException {
        String sql = "update " + UserDao.tableName
                    + "username=" + "'" + username +"',"
                    + "password=" + "'" + password +"',"
                    + "employee=" + "'" + employee +"',"
                    + "date=" + "'" + date +"',"
                    + "employee_name=" + "'" + employeeName +"',"
                    + "public_key=" + "'" + publicKey + "'"
                + " where id="+id;
        
        db.update(sql);
    }
     
     public void update(UserInfo user) throws SQLException {
        String sql = "update " + UserDao.tableName + " set "
                    + "username=" + "'" + user.username +"',"
                    + "password=" + "'" + user.password +"',"
                    + "employee=" + "'" + user.employee +"',"
                    + "date=" + "'" + user.date +"',"
                    + "employee_name=" + "'" + user.employeeName +"',"
                    + "public_key=" + "'" + user.publicKey + "'"
                + " where id="+ user.id;
        
        db.update(sql);
    }
     
    public void delete(int id) throws SQLException{
         String sql = "update " + UserDao.tableName 
                 + " set deleted=1 where id="+id;
         db.update(sql);
    }
    
    public void delete(String ids) throws SQLException{
         String sql = "update " + UserDao.tableName 
                 + " set deleted=1 where id in (" + ids + ")";
         db.update(sql);
    }
    
     
    public ResultSet selectAll () throws SQLException {
        String sql = "select * from users where deleted <> 1";
        return db.select(sql);
    }
    
    public void fillList () throws SQLException {
        this.selectAll();
        fillByResultSet(this.db.rs);
    }
    
    public void fillByResultSet(ResultSet rs) throws SQLException {
        if(rs == null) return;
        
        DefaultTableModel model = (DefaultTableModel) this.accountMngFrm.tblAcc.getModel();
        
        while(rs.next()){
         //Retrieve by column name
        // int id  = rs.getInt("id");
         String id = rs.getString("id");
         String username = rs.getString("username");
         String password = rs.getString("password");
         String date = rs.getString("date");
         String employee = rs.getString("employee");
         String employeeName = rs.getString("employee_name");
         String publicKey = rs.getString("public_key");

         model.addRow(new Object[]{id, username, password, employee, date, employeeName, publicKey});
      }
        
    }
    
    public void clearAccountTable() {
        DefaultTableModel model = (DefaultTableModel)  this.accountMngFrm.tblAcc.getModel();
        int rowCount = model.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
    
    public void addRowToTable(int id, String username, String password, String employee, String date
                        , String employeeName, String publicKey) {
        DefaultTableModel model = (DefaultTableModel) this.accountMngFrm.tblAcc.getModel();
        model.addRow(new Object[]{username, password, employee, date, employeeName, publicKey});
    }
    
    public void addRowToTable(UserInfo user) {
        DefaultTableModel model = (DefaultTableModel) this.accountMngFrm.tblAcc.getModel();
        model.addRow(new Object[]{""+user.id, user.username, user.password, user.employee, user.date, user.employeeName, user.publicKey});
    }
    
    
    public void updateSelectedRow(UserInfo user) {
      //   this.accountMngFrm.tblAcc.setValueAt("aa", 0, 0); 
         
         int row = this.accountMngFrm.tblAcc.getSelectedRow();
         this.accountMngFrm.tblAcc.setValueAt(user.username, row, 1);
         this.accountMngFrm.tblAcc.setValueAt(user.password, row, 2);
         this.accountMngFrm.tblAcc.setValueAt(user.employee, row, 3);
         this.accountMngFrm.tblAcc.setValueAt(user.date, row, 4);
         this.accountMngFrm.tblAcc.setValueAt(user.employeeName, row, 5);
         this.accountMngFrm.tblAcc.setValueAt(user.publicKey, row, 6);
    }
    
    public Object[] getRowAt(int row) {
        Object[] result = new String[7];

        for (int i = 0; i < 7; i++) {
            result[i] = this.accountMngFrm.tblAcc.getModel().getValueAt(row, i);
        }

        return result;
    }
    
    public void find() throws SQLException, InterruptedException {
        String findTxt = this.accountMngFrm.txtSearch.getText();
        String like = " like '%"+ findTxt +"%' ";
        String sql = "select * from users where "
                + "username " + like 
                + " or " + "employee_name " + like;
        
        db.select(sql);
      
        this.clearAccountTable();
        this.fillByResultSet(db.rs);
    }
}
