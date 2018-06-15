/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;


import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** 
 *
 * @author Sab
 */
public class DBConnection {
    
   /*
    to connect  between the Application and the database 
    */
    public static Connection getConnection() throws SQLException {
		String userName = "root";
		String password = "root";
		String url = "jdbc:mysql://localhost:3306/library_assistant";
		
		DriverManager.registerDriver(new Driver());
		 Connection conn =  (Connection) DriverManager.getConnection(url, userName, password);
		
		
		conn.setAutoCommit(false);
		return conn;
    }

 
    
    
   
   
}
