import javax.swing.JFrame;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CarEdit extends JFrame{
	public CarEdit() {
		
		setSize(700, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); 

		add(new CarEditFX());
		setVisible(true);
	}
	class CarEditFX extends JFXPanel implements EventHandler<ActionEvent>{
		 Button btn = new Button("돌아가기");
		 String col[] = {"id","차 이름","브랜드","종류","금액","연비"};
		 CarInfo carinfo = new CarInfo("");
		 TableItem table = new TableItem(col,"SELECT car.id,ca_name,manufacture.name,cartype.name,concat(format(cost,'%,d'),'원'),yunbe FROM car join manufacture on brand=manufacture.id join cartype on kind=cartype.id;") {
			@Override
			public void handle(MouseEvent arg0) {
				if(!getSelectionModel().isEmpty()) {
					int row = getSelectionModel().getSelectedIndex() + 1;
					carinfo.ShowForm(row,arg0.getScreenX(),arg0.getScreenY());
				} else {
					carinfo.setVisible(false);
				}
			}
		};
		
		
		public CarEditFX() {
			BorderPane border = new BorderPane(table);
			
			VBox box = new VBox();
			btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			btn.setOnAction(this);
			box.getChildren().add(btn);
			box.setFillWidth(true);
			
			border.setBottom(box);
			setScene(new Scene(border));
		}
		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			CarEdit.this.dispose();
			carinfo.dispose();
		}
		
	}
}
