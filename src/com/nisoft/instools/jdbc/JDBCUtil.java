package com.nisoft.instools.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
	public static final String MYSQL_USER = "root";
	public static final String MYSQL_PASSWORD = "nuaayt";
	public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/job_recode_tools";
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			System.out.println("加载驱动失败");
			e.printStackTrace();
		}
		try{
			conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);		
		} catch (Exception e) {
			System.out.println("获取链接失败");
		}
		return conn;
	}
	
	
}
