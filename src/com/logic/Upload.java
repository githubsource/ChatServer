package com.logic;

import com.socket.ServerFrame;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Upload implements Runnable{

    public String addr;
    public int port;
    public Socket socket;
    public FileInputStream In;
    public OutputStream Out;
    public File file;
    public ServerFrame ui;
    byte[] bytes;
    
    public Upload(String addr, int port, File filepath, ServerFrame frame){
        super();
        try {
            file = filepath; ui = frame;
            socket = new Socket(InetAddress.getByName(addr), port);
            Out = socket.getOutputStream();
            In = new FileInputStream(filepath);
        } 
        catch (Exception ex) {
            System.out.println("Exception [Upload : Upload(...)]");
        }
    }
    
    public Upload(String addr, int port, byte[] bytes, ServerFrame frame){
        super();
        try {
             ui = frame;
            socket = new Socket(InetAddress.getByName(addr), port);
            Out = socket.getOutputStream();
            this.bytes = bytes;
            //In = new FileInputStream(filepath);
        } 
        catch (Exception ex) {
            System.out.println("Exception [Upload : Upload(...)]");
        }
    }
    
    @Override
    public void run() {
        try {       
            byte[] buffer = new byte[1024];
            int count;
            
            //while((count = In.read(buffer)) >= 0){
            //    Out.write(buffer, 0, count);
            //}
            Out.write(bytes, 0, bytes.length);
            Out.flush();
            
            ui.jTextArea1.append("[Applcation > SERVER] : File upload complete\n");
            ui.btnFileSelect.setEnabled(true); ui.btnSendFile.setEnabled(true);
            ui.txtFilePath.setVisible(true);
            
            if(In != null){ In.close(); }
            if(Out != null){ Out.close(); }
            if(socket != null){ socket.close(); }
        }
        catch (Exception ex) {
            System.out.println("Exception [Upload : run()]");
            ex.printStackTrace();
        }
    }

}