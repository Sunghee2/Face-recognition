package DB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.BLOB;

public class DBConnection{
	String insertPath = "c:/imgTest/";
	String retrievePath = "c:/imgTest2/";
	PreparedStatement pstm, pstm2 = null;
	FileInputStream fin = null;
	ResultSet rs = null;
	
	public static Connection dbConn;
		public static Connection getConnection() {
			Connection conn = null;
			try {
				String user = "scott";
				String pw = "1234";
				String url = "jdbc:oracle:thin:@localhost:1521:orcl";
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, user, pw);
				
				System.out.println("Database에 연결되었습니다.\n");
			} catch(ClassNotFoundException cnfe) {
				System.out.println("DB 드라이버 로딩 실패 :" + cnfe.toString());
			} catch(SQLException sqle) {
				System.out.println("DB 접속실패 : " + sqle.toString());
			} catch(Exception e) {
				System.out.println("unknown error");
				e.printStackTrace();
			}
			return conn;
		}
		
		public void insertImg(int i) {
			Connection conn = getConnection();
			
			String quary = "INSERT INTO imgTable(PHOTO) VALUES(?)";
			String filename="cropFace"+i+".jpg";
			
			try {
				pstm = conn.prepareStatement(quary);
				fin = new FileInputStream(insertPath+filename);
				
				pstm.setBinaryStream(1,fin, fin.available());
				pstm.executeUpdate();
				
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch(FileNotFoundException e) {
				System.out.println("파일 x");
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("insert!");
			
		}
		
		public void retrieveImg(int i) {
			Connection conn = getConnection();
			
			String filename = "retrieveFace" + i +".jpg";
			
			try {
				pstm2 = conn.prepareStatement("SELECT * FROM imgTable");
				rs=pstm2.executeQuery();
				
				while(rs.next()) {
					Blob b = rs.getBlob(1);
					byte barr[]=b.getBytes(1, (int)b.length());
					
					FileOutputStream fout = new FileOutputStream(retrievePath+filename);
					fout.write(barr);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs!=null) rs.close();
					if(pstm2!=null) pstm2.close();
					if(conn!=null) conn.close();
				} catch(Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
			
		}
}
			