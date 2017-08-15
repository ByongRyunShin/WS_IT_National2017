import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class Register extends JFrame{
	public Register() {
		setTitle("회원가입");
		setSize(450, 525);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(new SignUp());
		setVisible(true);
	}
	private class SignUp extends JFXPanel implements EventHandler<ActionEvent>{
		BorderPane border;
		Label lb[] = new Label[7];
		TextField fd[] = new TextField[7]; 
		Label title = new Label("회원가입");
		Button btn[] = {new Button("중복 확인"),new Button("회원가입"),new Button("돌아가기")};
		boolean check = false;
		
		public SignUp() {
			GridPane gp1 = new GridPane();
			gp1.setAlignment(Pos.CENTER);
			gp1.setHgap(10);
			gp1.setVgap(20);
			
			border = new BorderPane(gp1);
			
			String grid1[] = {"이름","아이디","비밀번호","비밀번호 확인","전화번호","생년월일","주소"};
			String grid2[] = {"이름을 입력해주세요.","아이디를 입력해주세요.","비밀번호를 입력해주세요.","비밀번호를 입력해주세요.","전화번호를 입력해주세요.","생년월일을 입력해주세요.","주소를 입력해주세요."};
			
			for(int i = 0; i<lb.length; i++) {
				lb[i] = new Label(grid1[i]); fd[i] = new TextField();
				fd[i].setPromptText(grid2[i]);
				fd[i].setPrefWidth(200);
				gp1.add(lb[i], 0, i); gp1.add(fd[i], 1, i);
			}
			
			for(Button bt : btn) 
				bt.setOnAction(this);
			
			fd[1].setOnKeyPressed(new EventHandler<Event>() {
			
				@Override
				public void handle(Event event) {
					// TODO Auto-generated method stub
					check=false;
				}
			
			});
			
			gp1.add(btn[0],2,1);
			
			GridPane gp2 = new GridPane();
			gp2.add(btn[1], 0, 0); gp2.add(btn[2], 1, 0);
			gp2.setHgap(30);
			gp2.setPadding(new Insets(0, 0, 0, -6));
			
			gp1.add(gp2, 1, 7);
			 
			BorderPane.setAlignment(title, Pos.TOP_CENTER);
			title.setFont(new Font(31));
			title.setPadding(new Insets(40, 0, 0, 0));
			
			border.setTop(title);
			setScene(new Scene(border));
		}

		
		private boolean isCorrectBirth(String getbirth) {
			// TODO Auto-generated method stub
			boolean result = true;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			
			try {
				format.parse(getbirth);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				MsgBox.ErrorBox("올바르지 못한 생년월일 입니다.");
				result = false;
			}
			return result;
		}
		
		private boolean isPhoneNum(String getNum) {
			String patern = "^(010)-(\\d{4})-(\\d{4})$";
			
			if(Pattern.matches(patern, getNum))
				return true;
			else {
				MsgBox.ErrorBox("전화번호는 010-0000-0000 형식으로 입력해주세요.");
				return false;
			}
		}
		
		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			Button bt = (Button)e.getSource();
			
			if(btn[0]==bt) {
				if(fd[1].getText().equals("")) MsgBox.ErrorBox("아이디를 입력해주세요.");
				else {
					try {
						ResultSet rs = DB.stmt.executeQuery("select * from customer where id='" + fd[1].getText() + "'");
					
						if(rs.next())
							MsgBox.ErrorBox("중복된 아이디 입니다.");
						else {
							MsgBox.InfoBox("사용가능한 아이디 입니다.");
							check = true;
						}
						rs.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if(btn[1]==bt) {
				for(TextField field : fd) {
					if(field.getText().trim().equals("")) {
						MsgBox.ErrorBox("공백 없이 입력해주세요.");
						return;
					}
				}
				
				if(check) {
					if(fd[2].getText().equals(fd[3].getText())) {
						if(isPhoneNum(fd[4].getText()))
							if(isCorrectBirth(fd[5].getText())) {
								try {
									ResultSet rs = DB.stmt.executeQuery("select (count(c_num)+1) from customer;");
									
									if(rs.next()) {
										DB.stmt.executeUpdate("insert into customer(c_num,c_name,id,pw,phone,birth,address) values('" + rs.getString(1)  + "','" + fd[0].getText() + "','" + fd[1].getText() + "','" + fd[2].getText() + "','" + fd[4].getText() + "','" + fd[5].getText() + "','" + fd[6].getText() + "');");
										MsgBox.InfoBox("회원가입이 완료되었습니다.");
									}
									
									rs.close();
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
					} else
						MsgBox.ErrorBox("비밀번호가 일치하지 않습니다.");
				} else
					MsgBox.ErrorBox("아이디 중복 확인을 해주세요.");
				
			} else if(btn[2]==bt) {
				Register.this.dispose();
			}
		}
	}
	public static void main(String[] args) {
		new Register();
	}
}
