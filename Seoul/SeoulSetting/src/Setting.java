import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Setting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//DB연결
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
			stmt.execute("CREATE SCHEMA `hoteldb` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;");
			
			//테이블 생성
			stmt.execute("CREATE TABLE `sw3_01`.`tbl_customer` (  `cID` VARCHAR(6) NOT NULL,  `cPW` VARCHAR(4) NULL,  `cName` VARCHAR(10) NULL,  `cHP` VARCHAR(13) NULL,  PRIMARY KEY (`cID`));");
			stmt.execute("CREATE TABLE `sw3_01`.`tbl_bus` (  `bNumber` VARCHAR(4) NOT NULL,  `bDeparture` VARCHAR(5) NULL,  `bArrival` VARCHAR(5) NULL,  `bTime` TIME NULL,  `bElapse` VARCHAR(10) NULL,  `bCount` VARCHAR(1) NULL,`bPrice` INT(6) NULL,PRIMARY KEY (`bNumber`));");
			stmt.execute("CREATE TABLE `sw3_01`.`tbl_ticket` (  `bDate` DATE NULL,  `bNumber` VARCHAR(4) NULL,  `bNumber2` VARCHAR(5) NULL,  `bSeat` INT(2) NULL,  `cID` VARCHAR(6) NULL,  `bPrice` INT(6) NULL,  `bState` VARCHAR(1) NULL);");
			
			//tbl_customer테이블 데이터삽입
			int c=0; //첫줄 넘기기위한 카운트
			String line=null;
			String cur=System.getProperty("user.dir"); //현재 프로젝트 경로
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(cur+"\\DataFiles\\TBL_CUSTOMER.txt"),"UTF8"));
			while((line=br.readLine())!=null){
				if(c!=0)
				{
					String word[]=line.trim().split("\t");
					stmt.execute("insert into `sw3_01`.`tbl_customer` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"')");
				}
				c++;
			}
			br.close();
			
			//tbl_bus 테이블 데이터삽입
			c=0;
			line=null;
			br=new BufferedReader(new InputStreamReader(new FileInputStream(cur+"\\DataFiles\\TBL_BUS.txt"),"UTF8"));
			while((line=br.readLine())!=null){
				if(c!=0)
				{
					String word[]=line.trim().split("\t");
					stmt.execute("insert into `sw3_01`.`tbl_bus` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"','" + word[4] +"','" + word[5] +"','" + word[6] +"')");
				}
				c++;
			}
			br.close();
			
			//tbl_ticket 테이블 데이터삽입
			c=0;
			line=null;
			br=new BufferedReader(new InputStreamReader(new FileInputStream(cur+"\\DataFiles\\TBL_TICKET.txt"),"UTF8"));
			while((line=br.readLine())!=null){
				if(c!=0)
				{
					String word[]=line.trim().split("\t");
					stmt.execute("insert into `sw3_01`.`tbl_ticket` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"','" + word[4] +"','" + word[5] +"','" + word[6] +"')");
				}
				c++;
			}
			br.close();
			
			//사용자  user 생성(존재하면 생성안함)
			stmt.execute("create user IF NOT EXISTS user@localhost identified by '1234';");
			stmt.execute("grant select, insert, delete, update on `sw3_01`.* to user@localhost;");
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
