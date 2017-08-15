import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class ListFrame extends JFrame{
	public ListFrame() {
		setTitle("구매목록");
		setSize(1550, 785);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(new ListFX());
		setVisible(true);
	}
	class ListFX extends JFXPanel implements EventHandler<ActionEvent>{
		 String col[] = {"","구매일자","이름","아이디","차량이름","옵션","색상","옵션가격","차량가격","총가격","할부기간","할부여율","월상환액","딜러"};
		 ResultSet rs = null;
		 FXChart chart;
		 BorderPane border;
		 TableItem table = new TableItem(col) {
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
				chart = new FXChart(table.getValue(table.getSelectionModel().getSelectedIndex(), 1).toString());
				border.setBottom(chart);
			};
		};
		
		public ListFX() {
			try {
				rs = DB.stmt.executeQuery("select count(s_num) from salelist");
				if(rs.next()) {
					String opt[] = new String[rs.getInt(1)];
					int index[] = new int[rs.getInt(1)];
					rs = DB.stmt.executeQuery("select s_num,opt from salelist");
					int cnt = 0;
					while(rs.next()) {
						index[cnt] = rs.getInt(1);
						opt[cnt] = rs.getString(2);
						cnt++;
					}
					
					String split[];
					for(int i=0; i<opt.length; i++) {
						split = opt[i].split(",");
						for(int j = 0; j<split.length; j++) {
							rs = DB.stmt.executeQuery("select name from opt where id='" + split[j] + "'");
							if(rs.next())
								split[j] = rs.getString(1);
						}
						opt[i] = String.join(",", split);
					}
					
					ObservableList<String> newRow;
					for(int i =0; i<opt.length; i++) {
						newRow = FXCollections.observableArrayList(); //arraylist로 변환해서 add기능을 쓸수잇게
						rs = DB.stmt.executeQuery("SELECT s_num,buydate,customer.c_name,customer.id,car.ca_name,'" + opt[i] + "',color,concat(format(optcost,'%,d'),'원'),concat(format(carcost,'%,d'),'원'),concat(format(optcost+carcost,'%,d'),'원'),halbu,eyyul,concat(format(monthcost,'%,d'),'원'),dealer.name FROM carproject.salelist join customer on cid=customer.c_num join car on caid=car.id join dealer on salelist.dealer=dealer.id where s_num='" + index[i] + "' order by buydate;");
						if(rs.next())
							for(int j=0; j<col.length; j++) {
								if(j==6)
									newRow.add("#"+ rs.getString(7).substring(2).toUpperCase());
								else
									newRow.add(rs.getString(j+1));
							}
						table.getRecode().add(newRow);
					}
					table.getItems().setAll(table.getRecode());
					
					TableColumn column = table.getColumns().get(5);
					
					column.setCellFactory(new Callback<TableColumn, TableCell>() {
						@Override
						public TableCell call(TableColumn param) {
							// TODO Auto-generated method stub
							return new TableCell<Object, String>() {
								@Override
								protected void updateItem(String item, boolean empty) {
									// TODO Auto-generated method stub
									super.updateItem(item, empty); //컬러 클래스 생성이 안되는데 테이블 넣을때 바꿀게
									setStyle("-fx-background-color: " + item +";");
								}
							};
						}
						
					});
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(rs!=null)
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			border = new BorderPane(table);
			
			setScene(new Scene(border));
		}

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			
		}
	}
}
