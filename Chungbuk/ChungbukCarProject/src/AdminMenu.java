import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class AdminMenu extends JFrame implements WindowListener{
	private Button btn[] = {new Button("차량관리"),new Button("구매목록"),new Button("돌아가기")};
	
	public AdminMenu() {
		setTitle("관리자 메뉴");
		setSize(210, 250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(new AdminMenuFX());
		setVisible(true);
	}
	class AdminMenuFX extends JFXPanel implements EventHandler<ActionEvent>{
		public AdminMenuFX() {
			GridPane p1 = new GridPane();
			p1.setAlignment(Pos.CENTER);
			p1.setVgap(10);
			p1.setPadding(new Insets(10, 25, 15, 20));
			
			for(int i = 0; i<btn.length; i++) {
				btn[i].setPrefSize(175, 50);
				btn[i].setOnAction(this);
				p1.add(btn[i], 0, i);
			}
			setScene(new Scene(p1));
		}
		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			Button bt = (Button)e.getSource();
			
			if(btn[0]==bt) {
				AdminMenu.this.dispose();
				new CarEdit();
			} else if(btn[1]==bt) {
				AdminMenu.this.dispose();
				new ListFrame();
			} else if(btn[2]==bt) {
				AdminMenu.this.dispose();
			}
		}
		
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		new Login();
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
