package com.nisoft.instools.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.nisoft.instools.utils.StringsUtil;

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
	
	
	public static ResultSet query(String table,String column,String value) {
		String sql = "select *from "+table+" where "+column+"='"+value+"'";
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	
	public static ArrayList<Employee> queryResult(ResultSet rs){
		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			rs.beforeFirst();
			while(rs.next()){
				Employee member = new Employee();
				String phone = rs.getString("phone");
				String name = rs.getString("name");
				String org_id = rs.getString("org_id");
				String stations_code = rs.getString("stations_code");
				String employee_id = rs.getString("employee_id");
				member.setPhone(phone);
				member.setName(name);
				member.setWorkNum(employee_id);
				member.setOrgId(org_id);
				member.setPositionsId(StringsUtil.getStrings(stations_code));
				employees.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return employees;
	}
	
	public static OrgInfo queryOrg(String org_code){
		OrgInfo org = new OrgInfo();
		ResultSet rs = query("org","org_id",org_code);
		try {
			rs.first();
			org.setOrgId(rs.getString("org_id"));
			org.setOrgName(rs.getString("org_name"));
			org.setParentOrgId(rs.getString("parent_id"));
			org.setOrgLevel(rs.getInt("org_level"));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return org;
	}
	
	public static ArrayList<OrgInfo> queryDetailedOrg(String org_id){
		ArrayList<OrgInfo> orgInfos = new ArrayList<>();
		OrgInfo org = queryOrg(org_id);
		orgInfos.add(org);
		while(org.getOrgLevel()>1){
			org = queryOrg(org.getParentOrgId());
			orgInfos.add(0,org);
		}
		return orgInfos;
	}
	
	public static ArrayList<OrgInfo> queryChildOrgs(String parent_id){
		ArrayList<OrgInfo> childOrgs = new ArrayList<>();
		ResultSet rs = query("org","parent_id",parent_id);
		try {
			rs.beforeFirst();
			while(rs.next()){
				OrgInfo org = new OrgInfo();
				org.setOrgId(rs.getString("org_id"));
				org.setOrgName(rs.getString("org_name"));
				org.setParentOrgId(rs.getString("parent_id"));
				org.setOrgLevel(rs.getInt("org_level"));
				childOrgs.add(org);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return childOrgs;
	}
}
