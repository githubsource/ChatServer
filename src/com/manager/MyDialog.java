/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manager;

import com.logic.FileUtil;
import com.manager.AccountManager;
import com.model.UserInfo;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author tranngocdien
 */
class MyDialog extends JDialog implements ActionListener {

    public static int DIALOG_ADD = 1;
    public static int DIALOG_UPDATE = 2;
    
    AccountManagerFrame accountManagerFrm;
    public MyJPanel inputPanel;
    public int type;
    public int id = 0;

    MyDialog(AccountManagerFrame accountManagerFrm, int type) {
        this.accountManagerFrm = accountManagerFrm;
        this.type = type;
        setSize(500, 500);
        //setLayout(new FlowLayout());
        
        inputPanel = new MyJPanel();
        add(inputPanel);
        setModal(true);

        inputPanel.btnAdd.addActionListener(this);

        inputPanel.msgAlert.setText("");
        this.inputPanel.btnAdd.setText(type == DIALOG_ADD ? "Thêm" : "Cập nhật");
        
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inputPanel.btnAdd) {
           // inputPanel.labelAddTk.setText(inputPanel.txtUsername.getText());
            // validate
            String username = this.inputPanel.txtUsername.getText();
            String password = this.inputPanel.txtPassword.getText();
            String date = this.inputPanel.txtDate.getText();
            String employee = this.inputPanel.txtEmployee.getText();
            String employeeName = this.inputPanel.txtEmployeeName.getText();
            String saveKey = "";
            try {
                 saveKey = FileUtil.readFile(inputPanel.file);
            } catch (IOException ex) {
                Logger.getLogger(MyDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
           // String publicKey = this.inputPanel.txtPublicKey.getText();
            
            UserInfo user = new UserInfo(id, username, password, employee, date, employeeName, saveKey);
            
            if(this.type == DIALOG_ADD) {
                this.addUser(user);
            } else {
                try {
                    this.updateUser(user);
                } catch (SQLException ex) {
                    Logger.getLogger(MyDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void initFields(UserInfo user) {
        this.inputPanel.txtUsername.setText(user.username);
        this.inputPanel.txtPassword.setText(user.password);
        this.inputPanel.txtEmployee.setText(user.employee);
        this.inputPanel.txtDate.setText(user.date);
        this.inputPanel.txtEmployeeName.setText(user.employeeName);
        this.inputPanel.txtPublicKey.setText(user.publicKey);
        
        this.id = user.id;
    }
    
    public void addUser (UserInfo user) {
         try {
                if(!this.accountManagerFrm.accountMng.isExits(user.username)){
                    int id = this.accountManagerFrm.accountMng.add(user);
                    user.id = id;
                    this.inputPanel.msgAlert.setText("Đã thêm người dùng " + user.username + "!");
                    accountManagerFrm.accountMng.addRowToTable(user);
                } else {
                    this.inputPanel.msgAlert.setText(user.username + " exist !! please, select other username!");
                }
            } catch (Exception ex) {
            }
        
    }
    
    public void updateUser(UserInfo user) throws SQLException {
        this.accountManagerFrm.accountMng.update(user);
        this.inputPanel.msgAlert.setText("Đã cập nhật thông tin người dùng " + user.username + "!");
        this.accountManagerFrm.accountMng.updateSelectedRow(user);
    }
}
