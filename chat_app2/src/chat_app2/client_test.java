package chat_app2;
import javax.swing.JFrame;
public class client_test {
	public static void main(String[] args) {
	client chat2 = new client("127.0.0.1"); 
	chat2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	chat2.startRunning();
	}
}
