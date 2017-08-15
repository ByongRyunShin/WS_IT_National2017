import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;

public class FXChart extends GridPane {
	private BarChart<String, Number> manu;
	private BarChart<String, Number> dealer;
	private BarChart<String, Number> opt = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());
	
	public FXChart(String date) {
		// TODO Auto-generated constructor stub
		setAlignment(Pos.CENTER);
		manu = setChart("select name,(select count(buydate) from salelist join car on caid=car.id where buydate='" + date + "' and car.brand=manufacture.id) from manufacture;","제조사");
		dealer = setChart("select name,(select count(buydate) from salelist join car on caid=car.id where buydate='" + date + "' and dealer=dealer.id) from dealer;","딜러");
		
		manu.setTitle("제조사별 판매량");
		dealer.setTitle("딜러별 실적");
		opt.setTitle("옵션별 판매량");
		
		add(manu,0,0);
		add(dealer,1,0);
		add(setOptChart(date),2,0);
	}
	
	private BarChart<String, Number> setChart(String sql,String cate) {
		Series<String, Number> series = new Series<String, Number>();
		CategoryAxis xAxis = new CategoryAxis(); //이게 하나로 될줄 알았는데 각기 다른걸 넣어줘야 작동을 하더라
		NumberAxis yAxis = new NumberAxis();
		
		BarChart<String, Number> chart = new BarChart<String, Number>(xAxis, yAxis);
		xAxis.setLabel(cate);
		yAxis.setLabel("판매량");
		try {
			ResultSet rs = DB.stmt.executeQuery(sql);
			
			while(rs.next()) {//ㅈㄴ불편
				Data<String,Number> dat = new Data<String, Number>(rs.getString(1), rs.getInt(2));
				series.getData().add(dat); //씨발 컬러 여기서 주면 또 오류나노
			}
			
			chart.setPrefWidth(525);
			chart.setPrefHeight(200);
			chart.getData().add(series); 
			RandomColorset(series); //꼭 여기서 써줘야한다  series를 갖기전엔 임마가 무조건 null임
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return chart;
	}
	
	private BarChart<String, Number> setOptChart(String date) {
		Series<String, Number> series = new Series<String,Number>();
		
		opt.getXAxis().setLabel("옵션");
		opt.getYAxis().setLabel("판매량");
		opt.setAnimated(false);
		ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i<7; i++) 
			array.add(new ArrayList<Integer>());
			
		try {
			ResultSet rs = DB.stmt.executeQuery("select opt from salelist where buydate='" + date + "'");
			
			while(rs.next()) {
				String split[] = rs.getString(1).split(",");
				for(String sp : split) {
					switch(sp) {
					case "1":
						array.get(0).add(Integer.parseInt(sp));
						break;
					case "2":
						array.get(1).add(Integer.parseInt(sp));
						break;
					case "3":
						array.get(2).add(Integer.parseInt(sp));
						break;
					case "4":
						array.get(3).add(Integer.parseInt(sp));
						break;
					case "5":
						array.get(4).add(Integer.parseInt(sp));
						break;
					case "6":
						array.get(5).add(Integer.parseInt(sp));
						break;
					case "7":
						array.get(6).add(Integer.parseInt(sp));
						break;
					}
				}
			}
			
			rs = DB.stmt.executeQuery("select name from opt");
			int cnt = 0;
			while(rs.next()){
				Data<String, Number> dat = new Data<String, Number>(rs.getString(1), array.get(cnt).size());
				series.getData().add(dat);
				cnt ++;
			}
			opt.setPrefHeight(200);
			opt.getData().add(series);
			RandomColorset(series);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.opt;
	}
	
	private void RandomColorset(Series<String, Number> ilidan) {
		for(Data<String,Number> data : ilidan.getData()) {
			Random rand = new Random();
			
			int red = rand.nextInt(256);
			int green = rand.nextInt(256);
			int blue = rand.nextInt(256);
			int alpha = rand.nextInt(256);
			data.getNode().setStyle("-fx-bar-fill: rgba("+ red +"," +green + "," + blue + "," + alpha + ")" );
		}
	}
}
