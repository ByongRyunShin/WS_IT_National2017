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
			stmt.execute("CREATE SCHEMA `hoteldb` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;");
			
			//테이블 생성
			stmt.execute("CREATE TABLE `hoteldb`.`chain` (  `cn_index` INT NOT NULL AUTO_INCREMENT,  `name` VARCHAR(2) NULL,  `phonenumber` VARCHAR(12) NULL,  PRIMARY KEY (`cn_index`));");
			stmt.execute("CREATE TABLE `hoteldb`.`floor` (  `fl_index` INT NOT NULL AUTO_INCREMENT,  `class` INT NULL,  `grade` VARCHAR(15) NULL,  `cost` INT NULL,  PRIMARY KEY (`fl_index`));");
			stmt.execute("CREATE TABLE `hoteldb`.`user` (  `us_index` INT NOT NULL AUTO_INCREMENT,  `id` VARCHAR(10) NULL,  `pw` VARCHAR(10) NULL,  `name` VARCHAR(10) NULL,PRIMARY KEY (`us_index`));");
			stmt.execute("CREATE TABLE `hoteldb`.`notice` (  `nc_index` INT NOT NULL AUTO_INCREMENT,  `content` VARCHAR(200) NULL,  `enabled` TINYINT(1) NULL,  PRIMARY KEY (`nc_index`));");
			stmt.execute("CREATE TABLE `hoteldb`.`room` (  `rm_index` INT NOT NULL AUTO_INCREMENT,  `fl_index` INT NULL,  `cn_index` INT NULL,  `number` INT NULL,  `pax` INT NULL,  PRIMARY KEY (`rm_index`),  INDEX `flidx_idx` (`fl_index` ASC),  INDEX `cnidx_idx` (`cn_index` ASC),  CONSTRAINT `flidx`    FOREIGN KEY (`fl_index`)    REFERENCES `hoteldb`.`floor` (`fl_index`)    ON DELETE CASCADE    ON UPDATE CASCADE,  CONSTRAINT `cnidx`    FOREIGN KEY (`cn_index`)    REFERENCES `hoteldb`.`chain` (`cn_index`)    ON DELETE CASCADE    ON UPDATE CASCADE);");
			stmt.execute("CREATE TABLE `hoteldb`.`reserve` (  `rv_index` INT NOT NULL AUTO_INCREMENT,  `us_index` INT NULL,  `rm_index` INT NULL,  `checkin` DATETIME NULL,  `checkout` DATETIME NULL,  `amount` INT NULL,  PRIMARY KEY (`rv_index`),  INDEX `usidx_idx` (`us_index` ASC),  INDEX `rmidx_idx` (`rm_index` ASC),  CONSTRAINT `usidx`    FOREIGN KEY (`us_index`)    REFERENCES `hoteldb`.`user` (`us_index`)    ON DELETE NO ACTION    ON UPDATE NO ACTION,  CONSTRAINT `rmidx`    FOREIGN KEY (`rm_index`)    REFERENCES `hoteldb`.`room` (`rm_index`)    ON DELETE CASCADE ON UPDATE CASCADE);");
			
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
