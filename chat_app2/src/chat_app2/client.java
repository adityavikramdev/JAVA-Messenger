package chat_app2;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class client extends JFrame{
	private JTextField usertext;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message="";
	private String serverIp;
	private Socket connection;
	
	//constructor
	
	public client(String host){
		super("Chat App Client side");
		serverIp=host;
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
						connecttoServer();
						setupStreams();
						chatting();
					}
					catch(EOFException eofException){
						showMessage("\n Server ended the connection");
					}
					catch(IOException ioException){
						ioException.printStackTrace();
					}
					finally {
						closeCrap();
					}
		}
	//connect to server
		private void connecttoServer() throws IOException{
			showMessage("\ntrying to connect to server\n");
			connection = new Socket(InetAddress.getByName(serverIp), 6789);
			showMessage("connected to: "+ connection.getInetAddress().getHostName());
			
		}
	//set up streams
		private void setupStreams() throws IOException{
			output= new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input= new ObjectInputStream(connection.getInputStream());
			showMessage("\n Streams are set up\n");
			
		}
	//chatting
		private void chatting() throws IOException{
			
			abletoType(true);
			do {
				try {
					message= (String) input.readObject();
					showMessage("\n"+ message);
				}catch(ClassNotFoundException classNotFoundException){
					showMessage("\nError creeped in");
				}
			}
			while(!message.equals("Server - END"));
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
				output.writeObject("Client - "+message);
				output.flush();
				showMessage("\nClient - "+message);
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
