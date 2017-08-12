import javax.swing.JFrame;

import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Register extends JFrame{
	public Register() {
		setTitle("회원가입");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		myFX p=new myFX();
		add(p);
		pack();
		setVisible(true);
	}
	private class myFX extends JFXPanel{
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
			
			list.addAll(bp, grid, );
			Scene scene = new Scene(vBox);
			setScene(scene);
		}
	}
	public static void main(String[] args) {
		new Register();
	}
}
