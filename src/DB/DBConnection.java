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
	String insertPath;
	String retrievePath = "c:\\faceList\\";
	PreparedStatement pstm, pstm2 = null;
	FileInputStream fin = null;
	ResultSet rs = null;
	
	public DBConnection() {
		
	}
	
	public static Connection dbConn;
		public static Connection getConnection() {
			Connection conn = null;
			try {
				String user = "<your-db-username>";
				String pw = "<your-db-password>";
				String url = "jdbc:oracle:thin:@localhost:1521:orcl";
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, user, pw);
				
				System.out.println("connect DB.\n");
			} catch(ClassNotFoundException cnfe) {
				System.out.println("fail DB driver loading :" + cnfe.toString());
			} catch(SQLException sqle) {
				System.out.println("DB 접속 실패 : " + sqle.toString());
			} catch(Exception e) {
				System.out.println("unknown error");
				e.printStackTrace();
			}
			return conn;
		}
		
		public void insertImg(int id, int fileNo, int faceNo) {
			Connection conn = getConnection();
			
			String query = "INSERT INTO USERTABLE(USERNO, PHOTO) VALUES(?,?)";
			String path = "c:\\imgTest" + id + "\\";
			String filename="cropFace" + fileNo + ".jpg";
			
			
			
			try {
				pstm = conn.prepareStatement(query);
				fin = new FileInputStream(path+filename);
				
				
				pstm.setInt(1, faceNo);
				pstm.setBinaryStream(2,fin, fin.available());
				pstm.executeUpdate();
				
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch(FileNotFoundException e) {
				System.out.println("file X");
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("insert!");
			
		}
		
		public void retrieveImg() {
			Connection conn = getConnection();
			
			
			try {
				pstm2 = conn.prepareStatement("SELECT * FROM USERTABLE");
				rs=pstm2.executeQuery();
				
				while(rs.next()) {
					int userNo = rs.getInt(1);
					Blob b = rs.getBlob(2);
					byte barr[]=b.getBytes(1, (int)b.length());
					
					String filename = userNo + ".jpg";
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
			