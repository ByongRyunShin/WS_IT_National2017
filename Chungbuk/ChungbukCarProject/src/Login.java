import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Login extends JFrame{
	
	DB db=new DB();
	public Login() {
		setTitle("로그인");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); 
		myFX p=new myFX();
		add(p);
		pack();
		setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Login();
	}
	class myFX extends JFXPanel{
		public myFX() {
			
			GridPane grid = new GridPane();
			VBox vBox = new VBox(); 
			BorderPane bp = new BorderPane();

			ObservableList list = vBox.getChildren();
			
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 50, 25, 50));
			
			Text scenetitle = new Text("온라인 자동차 판매");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
			bp.setCenter(scenetitle);

			Label userName = new Label("아이디");
			grid.add(userName, 0, 1);

			TextField idField = new TextField();
			grid.add(idField, 1, 1);
			

			Label pw = new Label("비밀번호");
			grid.add(pw, 0, 2);

			PasswordField pwBox = new PasswordField();
			grid.add(pwBox, 1, 2);
			
			Button bt1 = new Button("로그인");
			bt1.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					if(idField.getText().equals("admin") && pwBox.getText().equals("1234")) {
						JOptionPane.showMessageDialog(null, "관리자 로그인 되었습니다", "관리자 로그인", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						if(idField.getText().equals("") || pwBox.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
						}
						else {
							try {
								ResultSet rs = DB.stmt.executeQuery("SELECT * FROM carproject.customer;");
								while(rs.next()) {
									if(rs.getString("Id").equals(idField.getText())){
										if(rs.getString("Pw").equals(pwBox.getText())) {
											JOptionPane.showMessageDialog(null, rs.getString("c_name")+"님 로그인 되었습니다.");
										}
										else JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다", "오류", JOptionPane.ERROR_MESSAGE);
									}
									JOptionPane.showMessageDialog(null, "존재하지 않는 아이디 입니다.", "오류", JOptionPane.ERROR_MESSAGE);
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			});
			grid.add(bt1, 2, 1);
			
			Button bt2 = new Button("회원가입");
			bt2.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					
				}
			});
			grid.add(bt2, 2, 2);
			
			Button bt3 = new Button("종료하기");
			bt3.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					
				}
			});
			grid.add(bt3, 2, 3);
			
			list.addAll(bp, grid);
			Scene scene=new Scene(vBox);
			vBox.setPadding(new Insets(30, 25, 50, 25));
			
			setScene(scene);
		}
	}
}
