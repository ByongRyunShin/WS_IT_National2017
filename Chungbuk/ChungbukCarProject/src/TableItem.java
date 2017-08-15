

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class TableItem extends TableView<Object> implements ChangeListener<Object>,EventHandler<MouseEvent> {
	private ObservableList<ObservableList<String>> recode;
	//이게 ArrayList비슷한건데 만약 SEX클래스면 생성자 ObservableList<SEX> 변수들을 컬렉션으로 만든다 
	private String colName[];
	private ResultSet rs = null;
	
	public TableItem(String col[]) {
		this.colName = col; 
		TableSet();
		getSelectionModel().selectedItemProperty().addListener(this);
		setOnMouseClicked(this);
	}
	
	public TableItem(String col[],String sql) {
		// TODO Auto-generated constructor stub
		this(col);
		QueryShow(sql);
	}
	
	@SuppressWarnings("unchecked")
	private void TableSet() {
		//fx콜렉션으로 실제로 사용가능하게 만들어 줘야한다
		recode = FXCollections.observableArrayList();
		//{(String형식){"S","E,"X"},(String[]){"K","I,"N","G"}};
		for(int i = 0; i<colName.length; i++) { //== colName.length
			  final int j = i;                
				@SuppressWarnings("rawtypes")
				TableColumn col = new TableColumn(colName[i]);
				col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>,String>,ObservableValue<String>>(){
					//테이블셀렌더러였다                     
					@Override
					public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> arg0) {
						// TODO Auto-generated method stub
						return new SimpleStringProperty(arg0.getValue().get(j).toString());
					}                    
					
	            });
          getColumns().add(col);
          
		}
		getColumns().remove(0);
		setEditable(false);
	}
	
	public void QueryShow(String sql) {
		try {
			recode.clear();
			rs = DB.stmt.executeQuery(sql);
			
			ObservableList<String> newRow;
			while(rs.next()) {
				newRow = FXCollections.observableArrayList();
				for(int i = 0; i<colName.length; i++)
					newRow.add(rs.getString(i+1));
				recode.add(newRow);
			}
			getItems().setAll(recode);
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
	}
	
	@SuppressWarnings("unchecked")
	public Object getValue(int row, int col) {
		Object ob = getItems().get(row); //Object[0]
		Object obj = ((ObservableList<Object>)ob).get(col);// Object[0][0]
		return obj;
	}
	
	public ObservableList<ObservableList<String>> getRecode(){
		return this.recode;
	}

	public void handle(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	};
	
}