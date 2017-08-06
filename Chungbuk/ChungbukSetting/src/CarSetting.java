import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CarSetting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DB연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost","root","1234");		
			Statement stmt = con.createStatement();
					
			//기존 DB삭제
			ResultSet rs = con.getMetaData().getCatalogs();
			while (rs.next()) {
				String s = rs.getString("table_cat");
				if (!s.equals("sys") && !s.equals("information_schema") && !s.equals("mysql") && !s.equals("performance_schema")) {
					stmt.execute("drop database `" + s + "`");
				}
			}
			rs.close();
			//새로운 DB 생성
			stmt.execute("CREATE SCHEMA `carproject` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;");
			
			//테이블 생성
			stmt.execute("CREATE TABLE `carproject`.`cartype` (  `Id` INT(11) NOT NULL AUTO_INCREMENT,  `Name` VARCHAR(20) NULL,  PRIMARY KEY (`Id`));");
			stmt.execute("CREATE TABLE `carproject`.`dealer` (  `Id` INT(11) NOT NULL AUTO_INCREMENT,  `Name` VARCHAR(20) NULL,  PRIMARY KEY (`Id`));");
			stmt.execute("CREATE TABLE `carproject`.`manufacture` (`Id` INT(11) NOT NULL AUTO_INCREMENT,  `Name` VARCHAR(20) NULL,  PRIMARY KEY (`Id`));");
			stmt.execute("CREATE TABLE `carproject`.`opt` (  `Id` INT(11) NOT NULL AUTO_INCREMENT,  `Name` VARCHAR(20) NULL,  `Cost` INT(10) NULL,  PRIMARY KEY (`Id`));");
			stmt.execute("CREATE TABLE `carproject`.`customer` (  `c_num` INT(11) NOT NULL AUTO_INCREMENT,  `c_name` VARCHAR(20) NULL,  `Id` VARCHAR(20) NULL,  `Pw` VARCHAR(20) NULL,  `Phone` VARCHAR(20) NULL,  `Birth` DATE NULL,  `Address` VARCHAR(20) NULL,  PRIMARY KEY (`c_num`));  ");
			stmt.execute("CREATE TABLE `carproject`.`car` (  `Id` INT(11) NOT NULL AUTO_INCREMENT,  `ca_name` VARCHAR(20) NULL,  `Brand` INT(10) NULL,  `Kind` INT(10) NULL,  `Cost` INT(10) NULL,  `Yunbe` VARCHAR(20) NULL,  PRIMARY KEY (`Id`));");
			stmt.execute("CREATE TABLE `carproject`.`salelist` (  `s_num` INT(11) NOT NULL AUTO_INCREMENT,  `Buydate` DATE NULL,  `cid` INT(11) NULL,  `Caid` INT(11) NULL,  `Opt` VARCHAR(20) NULL,  `Color` VARCHAR(20) NULL,  `Optcost` INT(11) NULL,  `Carcost` INT(11) NULL,  `Halbu` VARCHAR(20) NULL,  `Eyyul` VARCHAR(20) NULL,  `Monthcost` INT(11) NULL,  `Dealer` INT(11) NULL,  PRIMARY KEY (`s_num`));");
			
			String cur=System.getProperty("user.dir"); //현재 프로젝트 경로
			String[] flist= {"Chain", "Floor", "User", "Notice", "Room", "Reserve"};
			String line=null;
			BufferedReader br;
			for(int i=0; i<6; i++) {
				br=new BufferedReader(new InputStreamReader(new FileInputStream(cur+"\\DataFiles\\"+flist[i]+".txt"), "UTF-8"));
				while((line=br.readLine())!=null) {
					String word[]=line.trim().split("\t");
					if (word[0].startsWith("\uFEFF")) word[0] = word[0].substring(1);
					if(i==0 || i==3) stmt.execute("insert into `hoteldb`.`"+flist[i]+"` values('" + word[0] +"','" + word[1] +"','" + word[2] +"')");
					else if(i==1 || i==2) stmt.execute("insert into `hoteldb`.`"+flist[i]+"` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"')");
					else if(i==4) stmt.execute("insert into `hoteldb`.`Room` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"','" + word[4] +"')");
					else if(i==5) stmt.execute("insert into `hoteldb`.`Reserve` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"','" + word[4] +"','" + word[5] +"')");
				}
				br.close();
			}
			
			//사용자  user 생성(존재하면 생성안함)
			stmt.execute("create user IF NOT EXISTS user@localhost identified by '1234';");
			stmt.execute("grant select, insert, delete, update on `hoteldb`.* to user@localhost;");
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
