import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Purchase extends JFrame implements WindowListener{
	public Purchase(int index, String deal) {
		setTitle("차량구매");
		setSize(750, 560);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(new PurchaseFX(index, deal));
		setVisible(true);
	}
	class PurchaseFX extends JFXPanel implements EventHandler<ActionEvent>{
		private ImageView img;
		private String lbname1[] = {"차량이름","브랜드","종류","가격","연비"};
		private String lbname2[] = {"색상선택","옵션선택","옵션가격","할부개월","월상환액","할부이율","총 합계"};
		private Label lb1[] = new Label[5];
		private TextField field1[] = new TextField[5];
		private Label lb2[] = new Label[7];
		private ToggleButton colorbt[] = new ToggleButton[5];
		private ToggleGroup group = new ToggleGroup();
		private Vector<CheckBox> box = new Vector<CheckBox>(7);
		private ComboBox<String> combo = new ComboBox<String>();
		private TextField field2[] = new TextField[4];
		private Label dealer = new Label("담당 딜러 : ");
		private Button btn1 = new Button("구매하기");
		private Button btn2 = new Button("견적서 이미지로 저장");
		private Button btn3 = new Button("돌아가기");
		private String colorst[] = {"white","skyblue","red","lightgray","black"};
		private VBox vbox = new VBox();

		private long carcost;
		private int optcost;
		private int monthcost;
		private double eyyul;
		private String dealername;
		private Vector<Integer> optprice = new Vector<Integer>(7);
		
		public PurchaseFX(int index, String deal) {
			//GET QUERY
			for(int i = 0; i<lb2.length; i++)
				lb2[i] = new Label(lbname2[i]);
			
			for(int i = 0; i<field2.length; i++) {
				field2[i] = new TextField();
				field2[i].setEditable(false);
				field2[i].setMaxWidth(140);	
			}
			
			ResultSet rs;
			try {
				rs = DB.stmt.executeQuery("select ca_name,manufacture.name,cartype.name,cost,concat(yunbe,'km') from car join manufacture on manufacture.id=brand join cartype on cartype.id=kind where car.id='" + index + "';");
				if(rs.next()) {
					File file = new File(System.getProperty("user.dir") + "\\DataFiles\\" + rs.getString(1) + ".jpg");
					Image imgitem = new Image(file.toURI().toURL().toExternalForm());
					img = new ImageView(imgitem);
					
					for(int i = 0; i<field1.length; i++) { 
						lb1[i] = new Label(lbname1[i]);
						field1[i] = new TextField(rs.getString(i+1));
						field1[i].setEditable(false);
					}
					carcost = rs.getLong(4);
					field1[3].setText(String.format("%,d",carcost) + "원");
					field2[3].setText(String.format("%,d",carcost) + "원");
				}
				
				dealername = deal;
				dealer.setText(dealer.getText()+ deal);
				
				rs = DB.stmt.executeQuery("select name,cost,id from opt");
				while(rs.next()) {
					box.add(new CheckBox(rs.getString(1) + "\n" + String.format("%,d원",rs.getInt(2))));
					optprice.add(rs.getInt(2));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vbox.setFillWidth(true);
			BorderPane base = new BorderPane(vbox);
			
			BorderPane p1 = new BorderPane(img);
			
			GridPane right = new GridPane();
			right.setAlignment(Pos.CENTER_RIGHT);
			right.setPadding(new Insets(0, 5, 0, 20));
			right.setHgap(20);
			right.setVgap(20);
			
			img.setFitWidth(400);
			img.setFitHeight(200);
			
			p1.setRight(right);
			
			for(int i = 0; i<lb1.length; i++) {
				right.add(lb1[i], 0, i);
				right.add(field1[i], 1, i);
				field1[i].setPrefWidth(240);
			}
			
			vbox.getChildren().add(p1);
			
			GridPane grid1 = new GridPane();
			grid1.setPadding(new Insets(20, 0, 30, 0));
			grid1.setVgap(20);
			grid1.setHgap(20);
			
			GridPane grid2 = new GridPane();
			for(int i = 0; i<colorbt.length; i++) {
				colorbt[i] = new ToggleButton();
				colorbt[i].setToggleGroup(group);
				colorbt[i].setPrefSize(80, 60);
				colorbt[i].setId(colorst[i]);
				colorbt[i].setStyle("-fx-background-color: " + colorst[i] + ";"
									+ "-fx-background-insets: 0, 5;"
									+ "-fx-effect: dropshadow(three-pass-box, rgb(0,0,0,0.8), 10, 0, 0, 0);"
									+ "-fx-border-color: white;"
									+ "-fx-border-width: 4.5px;");
				colorbt[i].setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						ToggleButton bt = (ToggleButton)event.getSource();
						for(int i = 0; i<colorbt.length;i++) {
							String style = "-fx-background-color: " + colorst[i] + ";"
									+ "-fx-background-insets: 0, 5;"
									+ "-fx-effect: dropshadow(three-pass-box, rgb(0,0,0,0.8), 10, 0, 0, 0);"
									+ "-fx-border-color: white;"
									+ "-fx-border-width: 4.5px;";
							colorbt[i].setStyle(style);
						}
						
						String redborder = "-fx-background-color: " + bt.getId() + ";"
								+ "-fx-background-insets: 0, 5;"
								+ "-fx-effect: dropshadow(three-pass-box, rgb(0,0,0,0.8), 10, 0, 0, 0);"
								+ "-fx-border-color: darkred;"
								+ "-fx-border-width: 4.5px;";
						bt.setStyle(redborder);
					}
				});
				grid2.add(colorbt[i], i, 0);
			}
			
			GridPane gp3 = new GridPane();
			gp3.setHgap(8.4);
			
			for(int i = 0; i<this.box.size(); i++) {
				gp3.add(this.box.get(i), i, 0);
				box.get(i).setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						Calculation();
					}
				});
			}
			
			for(int i =0; i<5;i ++)
				grid1.add(lb2[i], 0, i);
			
			grid1.add(grid2, 1, 0);
			grid1.add(gp3, 1, 1);
			grid1.add(field2[0], 1, 2);
			GridPane p = new GridPane();
			p.add(combo, 0, 0); p.add(lb2[5], 1, 0); p.add(field2[1], 2, 0);
			p.setHgap(20);
			grid1.add(p, 1, 3);
			
			GridPane p2= new GridPane();
			p2.add(field2[2], 0, 0); p2.add(lb2[6], 1, 0); p2.add(field2[3], 2, 0); p2.add(dealer, 3, 0);  
			p2.setHgap(20); lb2[5].setPadding(new Insets(0, -10, 0, 54));
			
			grid1.add(p2, 1, 4);
			
			vbox.getChildren().add(grid1);
			
			combo.setPrefWidth(85);
			combo.getItems().add("12개월");
			combo.getItems().add("24개월");
			combo.getItems().add("36개월");
			combo.getItems().add("60개월");
			
			combo.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					String str = combo.getSelectionModel().getSelectedItem();
					
					switch(str) {
					case "12개월" :
						eyyul = 0.03;
						field2[1].setText("3%");
						break;
					case "24개월" :
						eyyul = 0.035;
						field2[1].setText("3.5%");
						break;
					case "36개월" :
						eyyul = 0.04;
						field2[1].setText("4%");
						break;
					case "60개월" :
						eyyul = 0.045;
						field2[1].setText("4.5%");
						break;
					}
					Calculation();
				}
			});
			
			GridPane g4 = new GridPane();
			g4.setAlignment(Pos.CENTER_LEFT);
			g4.setHgap(15);
			
			g4.add(btn1, 0, 0); g4.add(btn2, 1,0); g4.add(btn3, 2, 0);
			g4.setPadding(new Insets(0, 40, 10, 10));
			
			btn1.setPrefSize(230, 25);
			btn2.setPrefSize(230, 25);
			btn3.setPrefSize(230, 25);
			
			btn1.setOnAction(this); btn2.setOnAction(this); btn3.setOnAction(this);
			
			base.setBottom(g4);
			
			setScene(new Scene(base));
			
		}

		private void Calculation() {
			optcost = 0;
			monthcost = 0;
			for(int i = 0; i<box.size(); i++) {
				if(box.get(i).isSelected()) {
					optcost += optprice.get(i);
				}
			}
			
			long sumcost = carcost + optcost;
			
			field2[0].setText(String.format("%,d", optcost) + "원");
			field2[3].setText(String.format("%,d", sumcost) + "원");
			
			if(!combo.getSelectionModel().isEmpty()) {
				int halbu = Integer.parseInt(combo.getSelectionModel().getSelectedItem().substring(0, 2));
				
				double interest = Math.pow((1 + eyyul/12), halbu); //연이자율을 할부개월만큼 제곱
				monthcost = (int)((double)(sumcost *(((eyyul/12)*interest)/(interest -1))));
				field2[2].setText(String.format("%,d", monthcost) + "원");
			}
		}
		
		private boolean isFull() {
			boolean rtb = true;
			if(group.getSelectedToggle()==null) {
				rtb = false;
				MsgBox.ErrorBox("색상을 선택해주세요.");
			} else {
				int count = 0;
				for(int i = 0; i<box.size(); i++) {
					if(box.get(i).isSelected())
						count++;
				}
				
				if(count==0 || combo.getSelectionModel().isEmpty()) {
					rtb = false;
					MsgBox.ErrorBox("옵션과 할부를 선택해주세요.");
				}
			}
			return rtb;
		}
		
		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			if(btn1==e.getSource()) {
				if(isFull()) {
					String optcheck = "";
					for(int i = 0; i<box.size(); i++){
						if(box.get(i).isSelected())
							optcheck += "," + (i+1);
					}
					ToggleButton toggle = (ToggleButton)group.getSelectedToggle();
					String colorcode = toggle.getBackground().getFills().get(0).getFill().toString();
					
					try {
						DB.stmt.executeUpdate("insert into salelist(buydate,cid,caid,opt,color,optcost,carcost,halbu,eyyul,monthcost,dealer) "
														+ "values('" + String.format("%tY-%<tm-%<td", new Date()) + "','" + Login.userNo 
														+ "',(select id from car where ca_name='" + field1[0].getText() + "'),'" + optcheck.substring(1) 
														+ "','" + colorcode + "','" + optcost + "','" + carcost + "','" + combo.getSelectionModel().getSelectedItem() 
														+ "','" + field2[1].getText() + "','" + monthcost + "',(select id from dealer where name='" + dealername + "'));");
						MsgBox.InfoBox("구매가 완료되었습니다.");
						Purchase.this.dispose();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if(btn2==e.getSource()) {
				if(isFull()) {
					JFrame frm = (JFrame)SwingUtilities.getWindowAncestor(this);
					FileDialog fd = new FileDialog(frm, "그림으로 저장", FileDialog.SAVE);
					fd.setVisible(true);
					
					if(fd.getDirectory()!=null) {
						WritableImage wimg = vbox.snapshot(null, null);
						BufferedImage img = SwingFXUtils.fromFXImage(wimg, null);
						
						BufferedImage nonpink = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.OPAQUE);
						Graphics g = nonpink.createGraphics();
						g.drawImage(img, 0, 0, null);
						
						File file = new File(fd.getDirectory() + fd.getFile() +".jpg");
						try {
							ImageIO.write(nonpink, "jpg", file);
						} catch (IOException ee) {
							// TODO Auto-generated catch block
							ee.printStackTrace();
						}
					}
				}
			} else if(btn3==e.getSource()) {
				Purchase.this.dispose();
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
		new UserMain();
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
