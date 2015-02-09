package com.socket;

import com.conf.DatabaseManager;
import com.conf.MyDatabase;
import com.logic.EncryptionUtil;
import com.logic.Upload;
import java.io.*;
import java.net.*;

class ServerThread extends Thread { 
	
    public SocketServer server = null;
    public Socket socket = null;
    public int ID = -1;
    public String username = "";
    public ObjectInputStream streamIn  =  null;
    public ObjectOutputStream streamOut = null;
    public ServerFrame ui;

    public ServerThread(SocketServer _server, Socket _socket){  
    	super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
        ui = _server.ui;
    }
    
    public void send(Message msg){
        try {
            /*
           if(!(msg.type.equals("upload_req") || msg.type.equals("signup") || msg.type.equals("signout") ||
                       msg.type.equals("test") || msg.type.equals("login") || msg.type.equals("newuser")) ) {
                String txt = EncryptionUtil.plainTxt2CipherTxt(msg.content, this.ui.server.dbManager.getPublicKey(msg.recipient));
                msg.content = txt;
            }
            */
            streamOut.writeObject(msg);
            streamOut.flush();
        } 
        catch (Exception ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
    
    
    public void sendTest(Message msg, String user){
        // encrypt content
      
        try {
            String txt = EncryptionUtil.plainTxt2CipherTxt(msg.content, this.ui.server.dbManager.getPublicKey(msg.recipient));
            msg.content = txt;
            streamOut.writeObject(msg);
            streamOut.flush();
        } 
        catch (Exception ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
    
    public int getID(){  
	    return ID;
    }
   
    @SuppressWarnings("deprecation")
	public void run(){  
    	ui.jTextArea1.append("\nServer Thread " + ID + " running.");
        while (true){  
    	    try{  
                Message msg = (Message) streamIn.readObject();
    	    	server.handle(ID, msg);
            }
            catch(Exception ioe){  
            	System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }
    
    public void open() throws IOException {  
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }
    
    public void close() throws IOException {  
    	if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}





public class SocketServer implements Runnable {
    
    public ServerThread clients[];
    public ServerSocket server = null;
    public Thread       thread = null;
    public int clientCount = 0, port = 13000;
    public ServerFrame ui;
//    public Database db;
    public DatabaseManager dbManager;
    

    public SocketServer(ServerFrame frame){
       
        clients = new ServerThread[50];
        ui = frame;
    //    db = new Database(ui.filePath);
        
	try{  
	    server = new ServerSocket(port);
            port = server.getLocalPort();
	    ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
	    start(); 
        }
	catch(IOException ioe){  
            ui.jTextArea1.append("Can not bind to port : " + port + "\nRetrying"); 
            ui.RetryStart(0);
	}
        
        // my db       
        String address = ui.txtServerAddress.getText();
        MyDatabase mydb = new MyDatabase("jdbc:mysql://" + address + ":3306/chat", "root", "");
        
        
        if(!mydb.connect()) {
            ui.jTextArea1.append("Can not connect to database !!\n");
        }
        this.dbManager = new DatabaseManager(mydb);
        // my db
    }
    
    public SocketServer(ServerFrame frame, int Port){
       
        clients = new ServerThread[50];
        ui = frame;
        port = Port;
    //    db = new Database(ui.filePath);
        
        // my db       
        String address = ui.txtServerAddress.getText();
        MyDatabase mydb = new MyDatabase("jdbc:mysql://" + address + ":3306/chat", "root", "");
        
        
        if(!mydb.connect()) {
            ui.jTextArea1.append("Can not connect to database !!\n");
        }
        this.dbManager = new DatabaseManager(mydb);
        
	try{  
	    server = new ServerSocket(port);
            port = server.getLocalPort();
	    ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
	    start(); 
        }
	catch(IOException ioe){  
            ui.jTextArea1.append("\nCan not bind to port " + port + ": " + ioe.getMessage()); 
	}
    }
	
    public void run(){  
	while (thread != null){  
            try{  
		ui.jTextArea1.append("\nWaiting for a client ..."); 
	        addThread(server.accept()); 
	    }
	    catch(Exception ioe){ 
                ui.jTextArea1.append("\nServer accept error: \n");
                ui.RetryStart(0);
	    }
        }
    }
	
    public void start(){  
    	if (thread == null){  
            thread = new Thread(this); 
	    thread.start();
	}
    }
    
    @SuppressWarnings("deprecation")
    public void stop(){  
        if (thread != null){  
            thread.stop(); 
	    thread = null;
	}
    }
    
    private int findClient(int ID){  
    	for (int i = 0; i < clientCount; i++){
        	if (clients[i].getID() == ID){
                    return i;
                }
	}
	return -1;
    }
	
    public synchronized void handle(int ID, Message msg){  
	if (msg.content.equals(".bye")){
            Announce("signout", "SERVER", msg.sender);
            remove(ID); 
	}
	else{
            if(msg.type.equals("login")){
                if(findUserThread(msg.sender) == null){
                    //if(db.checkLogin(msg.sender, msg.content)){
                    if(dbManager.checkLogin(msg.sender, msg.content)){
                        clients[findClient(ID)].username = msg.sender;
                        clients[findClient(ID)].send(new Message("login", "SERVER", "TRUE", msg.sender));
                        
                        
                        // add newuser
                        ui.model.addElement(msg.content);
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    }
                    else{
                        clients[findClient(ID)].send(new Message("login", "SERVER", "FALSE", msg.sender));
                    } 
                }
                else{
                    clients[findClient(ID)].send(new Message("login", "SERVER", "FALSE", msg.sender));
                }
            }
            else if(msg.type.equals("message")){
                if(msg.recipient.equals("All")){
                    Announce("message", msg.sender, msg.content);
                }
                else{
                    findUserThread(msg.recipient).send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                    clients[findClient(ID)].send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                }
            }
            else if(msg.type.equals("test")){
                clients[findClient(ID)].send(new Message("test", "SERVER", "OK", msg.sender));
            }
           /* else if(msg.type.equals("signup")){
                if(findUserThread(msg.sender) == null){
                    //if(!db.userExists(msg.sender)){
                    if(!dbManager.userExists(msg.sender)){
                        dbManager.addUser(msg.sender, msg.content);
                        clients[findClient(ID)].username = msg.sender;
                        clients[findClient(ID)].send(new Message("signup", "SERVER", "TRUE", msg.sender));
                        clients[findClient(ID)].send(new Message("login", "SERVER", "TRUE", msg.sender));
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    }
                    else{
                        clients[findClient(ID)].send(new Message("signup", "SERVER", "FALSE", msg.sender));
                    }
                }
                else{
                    clients[findClient(ID)].send(new Message("signup", "SERVER", "FALSE", msg.sender));
                }
            }
                   */
            else if(msg.type.equals("upload_req")){
                if(msg.recipient.equals("All")){
                    clients[findClient(ID)].send(new Message("message", "SERVER", "Uploading to 'All' forbidden", msg.sender));
                }
                else{
                    findUserThread(msg.recipient).send(new Message("upload_req", msg.sender, msg.content, msg.recipient));
                }
            }
            else if(msg.type.equals("upload_res")){
                if(!msg.content.equals("NO")){
                    String IP = findUserThread(msg.sender).socket.getInetAddress().getHostAddress();
                    
                    if(msg.recipient.equals("SERVER")) {
                        int port  = Integer.parseInt(msg.content);
                        String addr = msg.sender;
                        
                        ui.btnFileSelect.setEnabled(false); ui.btnSendFile.setEnabled(false);
                        //File f = EncryptionUtil.plainFileToCipherFile(ui.file, this.dbManager.getPublicKey(msg.sender));
                       
                        // temp
                       //ok
                        /*
                        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTLImn/Uikakz9PEiG0P1ou9fKBz4mCOIecx13" +
                        "bERuq0Rr55vl4cRjm0qteubUsXS3w+TtE1Frktf17E3XWANSWk+fA/M36RUusxQjpxmn5Ak6T9jL" +
                        "SOJWXPs7rniCakmVRq15/H6xPmpKFx69zdnLKLpbzSC/oHI+9441SMWyfwIDAQAB";
                        Upload upl = new Upload(IP, port, EncryptionUtil.plainFileToCipherBy(ui.file, pubKey), ui);
                        */
                        Upload upl = new Upload(IP, port, EncryptionUtil.plainFileToCipherBy(ui.file, this.dbManager.getPublicKey(msg.sender)), ui);
                       

// Upload upl = new Upload(IP, port, ui.file, ui);
                        Thread t = new Thread(upl);
                        t.start();
                    }
                    else {
                        findUserThread(msg.recipient).send(new Message("upload_res", IP, msg.content, msg.recipient));
                    }
                }
                else {
                    if(msg.recipient.equals("SERVER")) {
                        ui.jTextArea1.append("\n"+msg.sender+" rejected file request\n");
                    } else {
                        findUserThread(msg.recipient).send(new Message("upload_res", msg.sender, msg.content, msg.recipient));
                    }
                }
            }
	}
    }
    
    public void Announce(String type, String sender, String content){
        Message msg = new Message(type, sender, content, "All");
        for(int i = 0; i < clientCount; i++){
            clients[i].send(msg);
        }
    }
    
    public void SendUserList(String toWhom){
        for(int i = 0; i < clientCount; i++){
            findUserThread(toWhom).send(new Message("newuser", "SERVER", clients[i].username, toWhom));
        }
    }
    
    public ServerThread findUserThread(String usr){
        for(int i = 0; i < clientCount; i++){
            if(clients[i].username.equals(usr)){
                return clients[i];
            }
        }
        return null;
    }
	
    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID){  
    int pos = findClient(ID);
        if (pos >= 0){
            //
            ui.model.removeElement(clients[pos].username);
            ServerThread toTerminate = clients[pos];
            ui.jTextArea1.append("\nRemoving client thread " + ID + " at " + pos);
	    if (pos < clientCount-1){
                for (int i = pos+1; i < clientCount; i++){
                    clients[i-1] = clients[i];
	        }
	    }
	    clientCount--;
	    try{  
	      	toTerminate.close(); 
	    }
	    catch(IOException ioe){  
	      	ui.jTextArea1.append("\nError closing thread: " + ioe); 
	    }
	    toTerminate.stop(); 
	}
    }
    
    private void addThread(Socket socket){  
	if (clientCount < clients.length){  
            ui.jTextArea1.append("\nClient accepted: " + socket);
	    clients[clientCount] = new ServerThread(this, socket);
	    try{  
	      	clients[clientCount].open(); 
	        clients[clientCount].start();  
	        clientCount++; 
	    }
	    catch(IOException ioe){  
	      	ui.jTextArea1.append("\nError opening thread: " + ioe); 
	    } 
	}
	else{
            ui.jTextArea1.append("\nClient refused: maximum " + clients.length + " reached.");
	}
    }
}
