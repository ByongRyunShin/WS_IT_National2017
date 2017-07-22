import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class Intro extends JWindow{
	public Intro(){
		setSize(285, 227);
		setLocationRelativeTo(null);
		String cur=System.getProperty("user.dir");
		JLabel img=new JLabel(new ImageIcon(cur+"\\DataFiles\\img\\emblem.png"));
		add(img);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Intro();
	}
}
