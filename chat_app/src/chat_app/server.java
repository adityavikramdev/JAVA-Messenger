package chat_app;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class server extends JFrame{
	private JTextField usertext;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public server(){
		super("Chat App");
		usertext = new JTextField();
		usertext.setEditable(false);
		usertext.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						usertext.setText("");
					}
				});
		add(usertext, BorderLayout.NORTH);
		chatwindow=new JTextArea();
		add(new JScrollPane(chatwindow));
		setSize(400,400);
		setVisible(true);
		
	}
	//set up
	public void startRunning() {
		try {
			server=new ServerSocket(6789, 100);
			while(true) {
				try {
					waitforConnection();
					setupStreams();
					chatting();
				}
				catch(EOFException eofException) {
					showMessage("\n Server ended the connection");
				}finally {
					closeCrap();
				}
			}
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//wait for connection
	private void waitforConnection() throws IOException{
		showMessage("waiting for connection..  \n");
		connection =server.accept();
		showMessage("connected to server..\n"+ connection.getInetAddress().getHostName());
	}
	// set streams
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are not setup \n");
		
	} 
	//chatting between server and client
	private void chatting() throws IOException{
		String message="You are now connected";
		sendMessage(message);
		abletoType(true);
		do {
			try {
				message= (String) input.readObject();
				showMessage("\n"+ message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\nError creeped in");
			}
		}
		while(!message.equals("Client - END"));
	}
	// close streams
	private void closeCrap(){
		showMessage("\n closing connection \n");
		abletoType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	// send message
	private void sendMessage(String message){
		try {
			output.writeObject("Server - "+message);
			output.flush();
			showMessage("\nServer - "+message);
		}catch(IOException ioException) {
			chatwindow.append("\n This message could not be sent..\n");
		}
	}
	//show messages
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
        new Runnable() {
        	public void run() {
        		chatwindow.append(text);
        	}
        }
        );
	}
    //able to type
	private void abletoType(final boolean tof){
		SwingUtilities.invokeLater(
		  new Runnable() {
		       public void run() {
		         usertext.setEditable(tof);
		       }
		 }
		 );
	}
}
