import java.io.File;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class UserMain extends JFrame{
	public UserMain() {
		setTitle("메인");
		setSize(420, 375);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(new UserMainFX());
		setVisible(true);
	}
	
	class UserMainFX extends JFXPanel implements EventHandler<ActionEvent>{
		 RadioButton rad[] = {new RadioButton("세단"),new RadioButton("친환경"),new RadioButton("RV")};
		 Label left = new Label(""); 
		 Label right = new Label("");
		 ImageView img = new ImageView();
		 Label lb[] = new Label[6];
		 Label infolb[] = new Label[5];
		 ComboBox<String> dealer = new ComboBox<String>();
		 Button btn1 = new Button("구매하기");
		 Button btn2 = new Button("종료하기");
		 ToggleGroup group = new ToggleGroup();
		 BorderPane box = new BorderPane();
		 ArrayList<Integer> idList = new ArrayList<Integer>();
		 Vector<String> carName = new Vector<String>();
		 Vector<GridPane> gridvec = new Vector<GridPane>();
		 int index;
		
		public UserMainFX() {
			box.setStyle("-fx-background-color: white");
			
			//Top
			GridPane gp1 = new GridPane();
			gp1.setAlignment(Pos.CENTER);
			gp1.setHgap(30);
			for(int i = 0; i<rad.length; i++) {
				gp1.add(rad[i], i, 0);
				rad[i].setToggleGroup(group);
				rad[i].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						RadioSet();	
					}
				});;
			}
			
			box.setTop(gp1);
			rad[0].setSelected(true);
			
			//Center
			img.setFitHeight(190);
			img.setFitWidth(380);
			BorderPane border = new BorderPane(img);
			
			left.setStyle("-fx-font-family: Webdings;"
						+ "-fx-font-size: 70");
			
			right.setStyle("-fx-font-family: Webdings;"
					+ "-fx-font-size: 70");
			
			left.setPadding(new Insets(0, -50, 0, -15));
			right.setPadding(new Insets(0, -20, 0, -50));
			
			border.setLeft(left); border.setRight(right);
			
			left.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					if(index!=0)
					// --index == index -= 1;
						ResultView(--index);
					// index-- == index -= 1;
					else {
						index = idList.size()-1;
						ResultView(index);
					}
					showAnimation(index);
				}
			});
			
			right.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					if(index!=idList.size()-1) //ex)사이즈 4일때, 3일경우 0으로 스킵
						ResultView(++index); // 대입하기전에 미리 더함
					else {
						index = 0;
						ResultView(index);
					}
					showAnimation(index);
				}
				
			});
			
			BorderPane.setAlignment(left, Pos.CENTER_LEFT);
			BorderPane.setAlignment(right, Pos.CENTER_RIGHT);			
			
			box.setCenter(border);
			
			//Bottom
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.TOP_LEFT);
			grid.setVgap(10);
			
			String grl[] = {"차  명 : ","종  류 : ","연  비 : ","브랜드 : ","가  격 : ","딜러 :"};
			
			int count = 0;
			int column = 0;
			
			for(int i =0; i<grl.length; i++) {
				lb[i] = new Label(grl[i]);
			
				gridvec.add(new GridPane());			
				gridvec.get(i).add(lb[i],0,0);
				
				grid.add(gridvec.get(i), column, count);
				
				count++;
				if(count==3) {
					count = 0;
					column = 1;
				}
					
			}
			
			for(int i=0; i<infolb.length; i++) {
				infolb[i] = new Label();
				gridvec.get(i).add(infolb[i], 1, 0);
				gridvec.get(i).setPrefWidth(235);
			}
			
			gridvec.get(5).add(dealer, 1, 0);
			dealer.setPrefWidth(80);
			
			try {
				ResultSet rs = DB.stmt.executeQuery("select name from dealer;");
				while(rs.next()) {
					dealer.getItems().add(rs.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			grid.setPadding(new Insets(0, 0, 10, 0));
			
			GridPane bottomgrid = new GridPane();
			
			bottomgrid.setAlignment(Pos.CENTER);
			bottomgrid.setHgap(5);
			bottomgrid.add(btn1, 0, 0); bottomgrid.add(btn2, 1, 0);
			
			btn1.setPrefWidth(200); btn2.setPrefWidth(200);
			btn1.setOnAction(this); btn2.setOnAction(this);
			
			BorderPane pane = new BorderPane(grid);
			pane.setBottom(bottomgrid);
			
			box.setBottom(pane);
			
			RadioSet();
			setScene(new Scene(box));
		}

		
		 void RadioSet() { //라디오 버튼 추가
				RadioButton radio = (RadioButton) group.getSelectedToggle();
				String str = radio.getText();
				
				CartypeEdit(str);
				
				int cartype[] = new int[3];
				switch(str) {
				case "세단" :
					cartype[0] = 1;
					cartype[1] = 4;
					cartype[2] = 6;
					break;
				case "친환경" :
					cartype[0] = 2;
					cartype[1] = 3;
					cartype[2] = 6;
					break;
				case "RV" :
					cartype[0] = 2;
					cartype[1] = 4;
					cartype[2] = 5;
					break;
				}
				
				for(int i = 0; i<cartype.length; i++) {
					
					try {
						File file = new File(System.getProperty("user.dir") + "\\DataFiles\\" + cartype[i] + ".PNG");
						Image imgitem  = new Image(file.toURI().toURL().toExternalForm());
						rad[i].setGraphic(new ImageView(imgitem));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
					
			}
		 private void CartypeEdit(String type) {
				ResultSet rs;
				try {
					rs = DB.stmt.executeQuery("select car.id,ca_name,manufacture.name,cartype.name,format(cost, '%,d'),yunbe from car join manufacture on manufacture.id=brand join cartype on cartype.id=kind where cartype.name='" + type + "';");
					
					idList.clear();
					carName.clear();
					
					while(rs.next()) {
						idList.add(rs.getInt(1));
						carName.add(rs.getString(2));
					}
					
					index = 0;
					ResultView(index);
					ImageShow(index);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 
		private void ResultView(int index) {
			
			try {
				ResultSet rs = DB.stmt.executeQuery("select ca_name,cartype.name,concat(yunbe,'km'),manufacture.name,concat(format(cost, '%,d'),'원') from car join manufacture on manufacture.id=brand join cartype on cartype.id=kind where car.id='" + idList.get(index) + "';");

				if(rs.next()) {
					for(int i= 0; i<5; i++)
						infolb[i].setText(rs.getString(i+1));
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		private void ImageShow(int index) {
			File file = new File(System.getProperty("user.dir") + "\\DataFiles\\" + carName.get(index) + ".jpg");
			try {
				Image imgitem = new Image(file.toURI().toURL().toExternalForm());
				img.setImage(imgitem);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void showAnimation(int index) {
			SequentialTransition all = new SequentialTransition(); //애니메이션 적립

	        FadeTransition fadeIn = fade(img, 1.0, 0.0, 250); // 깜빡 (사라짐) 애니메이션
	        fadeIn.setOnFinished(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					ImageShow(index); //이미지 불러오기
				}
			});
	        PauseTransition pause = new PauseTransition(Duration.millis(100)); //잠깐 아주잠깐 멈춰 주어야 부드럽게 돌아감
	        FadeTransition fadeOut = fade(img, 0.0, 1.0, 250); // 깜빡(등장) 애니메이션

	        all.getChildren().addAll(fadeIn, pause, fadeOut);
	        all.play();
		}
		
		public FadeTransition fade(ImageView imageView, double fromValue, double toValue, int seconds) {
		    FadeTransition ft = new FadeTransition(Duration.millis(seconds), imageView);
		    ft.setFromValue(fromValue); //출발시간(to보다 클수도잇다)
		    ft.setToValue(toValue); // 도착

		    return ft;

		}


		@Override
		public void handle(ActionEvent e) {
			// TODO Auto-generated method stub
			if(btn1==e.getSource()) {
				if(dealer.getSelectionModel().isEmpty())
					MsgBox.ErrorBox("딜러를 선택해주세요.");
				else {
					UserMain.this.dispose();
					new Purchase(idList.get(index), dealer.getSelectionModel().getSelectedItem());
				}
			} else {
				UserMain.this.dispose();
			}
		}
		
	}
}
