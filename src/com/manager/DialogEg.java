/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manager;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author tranngocdien
 */
public class DialogEg {
   private static void createAndShowUI() {
      JFrame frame = new JFrame("Dialog Eg");

      MainPanel mainPanel = new MainPanel();
      MainControl mainControl = new MainControl(frame);
      mainControl.setMainPanel(mainPanel);
      mainPanel.setControl(mainControl);

      frame.getContentPane().add(mainPanel.getMainPanel());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            createAndShowUI();
         }
      });
   }
}

class MainPanel {
   private JPanel mainPanel = new JPanel();
   private JButton showDlgBtn = new JButton("Show Dialog");
   private JTextField field = new JTextField(10);
   private MainControl mainControl;

   public MainPanel() {
      mainPanel.add(showDlgBtn);
      mainPanel.add(field);
      field.setEditable(false);

      showDlgBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (mainControl != null) {
               mainControl.showDialog();
            }
         }
      }); 
   }

   public void setControl(MainControl mainControl) {
      this.mainControl = mainControl;
   }

   public void setFieldText(String text) {
      field.setText(text);
   }

   public JPanel getMainPanel() {
      return mainPanel;
   }
}

class DialogPanel {
   private static final String[] COMBO_DATA = {"", "one", "two", "three", "four"};
   private JPanel dialogPanel = new JPanel();
   private JComboBox combo = new JComboBox(COMBO_DATA);

   public DialogPanel() {
      dialogPanel.add(combo);
      combo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Window window = SwingUtilities.getWindowAncestor(dialogPanel);
            window.dispose();
         }
      });
   }

   public JPanel getDialogPanel() {
      return dialogPanel;
   }

   public String getSelectedText() {
      return combo.getSelectedItem() == null ? "" : combo.getSelectedItem().toString();
   }
}

class MainControl {
   private DialogPanel dlgPanel = new DialogPanel();
   private MainPanel mainPanel;
   private JFrame frame;

   public MainControl(JFrame frame) {
      this.frame = frame;
   }

   public void setMainPanel(MainPanel mainPanel) {
      this.mainPanel = mainPanel;
   }

   public void showDialog() {
      if (mainPanel != null) {
         JDialog dialog = new JDialog(frame, "Dialog", Dialog.ModalityType.APPLICATION_MODAL);
         dialog.getContentPane().add(dlgPanel.getDialogPanel());
         dialog.pack();
         dialog.setLocationRelativeTo(null);
         dialog.setVisible(true);

         String text = dlgPanel.getSelectedText();
         if (text != null) {
            mainPanel.setFieldText(text);
         }
      }
   }

}
