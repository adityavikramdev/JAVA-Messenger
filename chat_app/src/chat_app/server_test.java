package chat_app;
import javax.swing.JFrame;
public class server_test {
	public static void main(String[] args) {
	server chat = new server();
	chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	chat.startRunning();
	}
}
