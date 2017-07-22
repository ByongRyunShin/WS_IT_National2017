import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;

public class CAP_Setting extends JFrame{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CAP_Setting();
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
			stmt.execute("CREATE SCHEMA `cap_project` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;");
			
			//테이블 생성
			stmt.execute("CREATE TABLE `cap_project`.`user` (  `id` INT NOT NULL AUTO_INCREMENT,  `uID` VARCHAR(20) NULL,  `uPW` TEXT NULL,  `uName` VARCHAR(20) NULL,  `uEmail` VARCHAR(20) NULL,  PRIMARY KEY (`id`));");
			stmt.execute("CREATE TABLE `cap_project`.`widget` (  `id` INT NOT NULL AUTO_INCREMENT,  `uid` INT NULL,  `wX` INT NULL,  `wY` INT NULL,  `wAlpha` INT NULL,  `autosave` TINYINT(1) NULL,  PRIMARY KEY (`id`),  INDEX `userid_idx` (`uid` ASC),  CONSTRAINT `userid`    FOREIGN KEY (`uid`)    REFERENCES `cap_project`.`user` (`id`)    ON DELETE CASCADE    ON UPDATE CASCADE);");
			stmt.execute("CREATE TABLE `cap_project`.`photodata` (  `id` INT NOT NULL AUTO_INCREMENT,  `uid` INT NULL,  `pdName` VARCHAR(255) NULL,  `pdData` LONGBLOB NULL,  PRIMARY KEY (`id`),  INDEX `userid_idx` (`uid` ASC),  CONSTRAINT `puserid`    FOREIGN KEY (`uid`)    REFERENCES `cap_project`.`user` (`id`)    ON DELETE CASCADE    ON UPDATE CASCADE);");
			
			
			String cur=System.getProperty("user.dir"); //현재 프로젝트 경로
			String[] flist= {"user", "widget"};
			String line=null;
			BufferedReader br;
			for(int i=0; i<2; i++) {
				br=new BufferedReader(new InputStreamReader(new FileInputStream(cur+"\\DataFiles\\Text\\"+flist[i]+".txt"), "UTF-8"));
				int c=0;
				while((line=br.readLine())!=null) {
					String word[]=line.trim().split("\t");
					c++;
					if(c==1) continue;
					if(i==0) stmt.execute("insert into `cap_project`.`"+flist[i]+"` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"','" + word[4] +"')");
					else if(i==1) stmt.execute("insert into `cap_project`.`"+flist[i]+"` values('" + word[0] +"','" + word[1] +"','" + word[2] +"','" + word[3] +"','" + word[4] +"','" + word[5] +"')");
				}
				br.close();
			}
			
			//사용자  user 생성(존재하면 생성안함)
			stmt.execute("create user IF NOT EXISTS user@localhost identified by '1234';");
			stmt.execute("grant select, insert, delete, update on `cap_project`.* to user@localhost;");
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
