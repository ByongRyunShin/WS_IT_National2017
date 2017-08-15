import javax.swing.JOptionPane;

public class MsgBox {
	public static void InfoBox(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public static void ErrorBox(String msg) {
		JOptionPane.showMessageDialog(null, msg, "오류", JOptionPane.ERROR_MESSAGE);
	}
}
