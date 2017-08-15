import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class Login extends JFrame{
	
	DB db=new DB();
	static String userNo;
	public Login() {
		setTitle("로그인");
		setSize(425, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); 

		add(new myFX());
		setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Login();
	}
	class myFX extends JFXPanel implements EventHandler<ActionEvent>{
		Label title = new Label("온라인 자동차 판매");
		Label id = new Label("아이디"), pw = new Label("비밀번호");
		TextField idfd = new TextField();
		PasswordField pwfd = new PasswordField();
		String bName[] = {"로그인","회원가입","종료하기"};
		Button[] btn = new Button[3];
		
		public myFX() {
			
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setPadding(new Insets(0, 0, 25, 0));
			grid.setHgap(5);
			grid.setVgap(10);
			
			BorderPane border = new BorderPane(grid);
			
			title.setFont(new Font(30));
			title.setPadding(new Insets(30, 0, 0, 0));
			
			border.setTop(title);
			BorderPane.setAlignment(title, Pos.TOP_CENTER);
			
			pwfd.setFont(new Font("Arial", 8));
			pwfd.setAlignment(Pos.TOP_LEFT);
			pwfd.setPrefHeight(22);
			
			grid.add(id, 0,	0); grid.add(idfd, 1, 0);
			grid.add(pw, 0,	1); grid.add(pwfd, 1, 1);
			
			
			for(int i = 0; i<bName.length; i++) {
				btn[i] = new Button(bName[i]);
				grid.add(btn[i], 2, i);
				btn[i].setOnAction(this);
			}
			
			setScene(new Scene(border));
		}
		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			Button bt = (Button)e.getSource();
			if(btn[0]==bt) {
				if(idfd.getText().trim().equals("")&&pwfd.getText().trim().equals(""))
					MsgBox.ErrorBox("아이디와 비밀번호를 입력해주세요.");
				else {
					if(idfd.getText().equals("admin")&&pwfd.getText().equals("1234")) {
						MsgBox.InfoBox("관리자로 로그인 되었습니다");
						userNo = "admin";
						
						Login.this.dispose();
						new AdminMenu();
					} else {
						try {
							ResultSet rs = DB.stmt.executeQuery("select c_num from customer where id='" + idfd.getText() + "';");
							
							if(rs.next()) {
								rs = DB.stmt.executeQuery("select c_num,c_name from customer where pw='" + pwfd.getText() + "';");
								
								if(rs.next()) {
									userNo = rs.getString(1);
									MsgBox.InfoBox(rs.getString(2) + "님 로그인 되었습니다.");
									Login.this.dispose();
									new UserMain();
								} else
									MsgBox.ErrorBox("비밀번호가 일치하지 않습니다.");
							} else
								MsgBox.ErrorBox("존재하지 않는 아이디 입니다.");
							
							rs.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block 
						}
					}
				}
			} else if(btn[1]==bt) {
				new Register();
			} else if(btn[2]==bt){
				System.exit(0);
			}
		}
	}
}
