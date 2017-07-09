import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame implements ActionListener{
	JLabel idlabel, pwlabel;
	JTextField idfield;
	JPasswordField pwfield;
	JButton lbt;
	DB db=new DB();
	
	public Login() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("로그인");
		
		idlabel=new JLabel("ID :");
		pwlabel=new JLabel("PW :");
		
		idfield=new JTextField(10);
		pwfield=new JPasswordField(10);
		
		JPanel p1=new JPanel(new GridLayout(0,2, -65, 5));
		JPanel p2=new JPanel(new BorderLayout());
		
		p1.add(idlabel);
		p1.add(idfield);
		p1.add(pwlabel);
		p1.add(pwfield);
		
		lbt=new JButton("Login");
		lbt.addActionListener(this);
		
		p2.add(p1);
		p2.add(lbt, BorderLayout.LINE_END);
		
		p1.setPreferredSize(new Dimension(168, 55));
		p1.setBorder(new EmptyBorder(0, 0, 0, 5));
		p2.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(p2);
		pack();
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==lbt) {
			String id=idfield.getText();
			String pw=pwfield.getText();
			if(id.equals("admin") && pw.equals("admin")) System.out.println("마즘");
			else {
				ResultSet rs;
				try {
					rs = DB.stmt.executeQuery("SELECT * FROM hoteldb.user");
					while(rs.next()) {
						if(id.equals(rs.getString("id"))){
							if(pw.equals(rs.getString("pw"))){
								JOptionPane.showMessageDialog(this, rs.getString("name")+"님 환영합니다!");
								
								this.dispose();
								return;
							}
						}
					}
					JOptionPane.showMessageDialog(this, "아이디 혹은 비밀번호를 확인해주세요.");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Login();
	}
}
