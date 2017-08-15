import java.awt.BorderLayout;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class CarInfo extends JDialog{
private CarInfoFX fx = new CarInfoFX();
	
	public CarInfo(String title) {
		// TODO Auto-generated constructor stub
		setTitle(title);
		setModal(false);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
		add(fx, BorderLayout.CENTER);
		
		setSize(700, 200);
	}
	
	public void ShowForm (int index,double x, double y) {
		setLocation((int)x, (int)y);
		fx.getQuery(index);
		setVisible(true);
	}
	class CarInfoFX extends JFXPanel implements EventHandler<ActionEvent>{
		private ImageView imgv;
		private Label lb[] = new Label[5];
		private TextField fd1 = new TextField();
		private TextField fd2 = new TextField();
		private TextField fd3 = new TextField();
		private ComboBox<String> comb1 = new ComboBox<String>();
		private ComboBox<String> comb2 = new ComboBox<String>();
		private Button btn1 = new Button("수정");
		private Button btn2 = new Button("삭제");
		private Node comp[] = {fd1, comb1, comb2, fd2, fd3};
		private int carID;
		private String carName;
		
		public CarInfoFX() {
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.TOP_CENTER);
			grid.setHgap(10);
			grid.setVgap(20);
			
			String col[] = {"차량이름","브랜드","종류","가격","연비"};
			for(int i = 0; i<lb.length; i++) {
				lb[i] = new Label(col[i]);
				grid.add(lb[i], 0, i);
				grid.add(comp[i], 1, i);
			}
			
			ResultSet rs;
			try {
				rs = DB.stmt.executeQuery("select name from manufacture;");
				while(rs.next())
					comb1.getItems().add(rs.getString(1));
				
				rs = DB.stmt.executeQuery("select name from cartype;");
				
				while(rs.next())
					comb2.getItems().add(rs.getString(1));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			fd1.setEditable(false);
			fd2.setEditable(false);
			fd3.setEditable(false);
			
			comb1.setDisable(true);
			comb2.setDisable(true);
			
			grid.add(btn1, 2, 0);
			grid.add(btn2, 2, 1);
			
			btn1.setPrefWidth(100);
			btn2.setPrefWidth(100);
			
			btn1.setOnAction(this); btn2.setOnAction(this);
			
			imgv = new ImageView();
			imgv.setFitWidth(375);
			imgv.setFitHeight(200);
			
			BorderPane border = new BorderPane(imgv);
			border.setRight(grid);
			
			setScene(new Scene(border));
			
		}

		public void getQuery(int id) {
			try {
				
				ResultSet rs = DB.stmt.executeQuery("select car.id,ca_name,manufacture.name,cartype.name,cost,yunbe FROM car join manufacture on brand=manufacture.id join cartype on kind=cartype.id where car.id='" + id + "';");
				
				if(rs.next()) {
					carID = rs.getInt(1);
					carName = rs.getString(2);
					
					fd1.setText(rs.getString(2));
					comb1.getSelectionModel().select(rs.getString(3));
					comb2.getSelectionModel().select(rs.getString(4));
					fd2.setText(rs.getString(5));
					fd3.setText(rs.getString(6));
					
					File file = new File(System.getProperty("user.dir")+"\\DataFiles\\" + rs.getString(2) + ".jpg");
					try {
						Image img = new Image(file.toURI().toURL().toExternalForm());
						imgv.setImage(img);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void CompSet(boolean bool) {
			fd1.setEditable(bool);
			fd2.setEditable(bool);
			fd3.setEditable(bool);
			
			comb1.setDisable(!bool);
			comb2.setDisable(!bool);
		}
		
		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			Button btn = (Button)e.getSource();
			
			if(btn1==btn) {
				if(btn1.getText().equals("수정")) {
					CompSet(true);
					
					btn1.setText("수정완료");
					btn2.setText("수정취소");
				} else {
					if(fd1.getText().trim().equals("")||fd2.getText().trim().equals("")||fd3.getText().trim().equals(""))
						MsgBox.ErrorBox("빈칸없이 입력해주세요.");
					else {
						if(Pattern.matches("^[0-9]$", fd2.getText())) {
							try {
								DB.stmt.executeUpdate("update car set ca_name='" + fd1.getText() + "',brand=(select id from manufacture where name='" + comb1.getSelectionModel().getSelectedItem() + "'),kind=(select id from manufacture where name='" + comb2.getSelectionModel().getSelectedItem() + "'),cost='" + fd2.getText() + "',yunbe='" + fd3.getText() + "' where id='';");
								CompSet(false);
								btn1.setText("수정");
								btn2.setText("삭제");
								MsgBox.InfoBox("수정이 완료되었습니다.");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else
							MsgBox.ErrorBox("올바른 정보를 입력해주세요.");
					}
				}
			} else if(btn2==btn) {
				if(btn2.getText().equals("삭제")) {
					int res = JOptionPane.showConfirmDialog(null, carName + "을(를) 정말 삭제하시겠습니까?");
					
					if(res == JOptionPane.YES_OPTION) {
						try {
							DB.stmt.executeUpdate("delete from car where id = '" + carID + "';");
							DB.stmt.executeUpdate("set @val=0;");
							DB.stmt.executeUpdate("update car set id=@val:=@val+1;");
							
							MsgBox.InfoBox("삭제되었습니다.");
							JDialog dialog = (JDialog)SwingUtilities.getWindowAncestor(this);
							dialog.dispose();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					CompSet(false);
					btn1.setText("수정");
					btn2.setText("삭제");
				}
				
			}
		}
	}
}
