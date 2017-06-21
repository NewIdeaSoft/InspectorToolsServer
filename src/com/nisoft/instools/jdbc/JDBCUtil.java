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
	
	public static ArrayList<Employee> queryEmployeeResult(String column,String value){
		String sql = "select *from employee where "+column+"='"+value+"'";
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
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
			System.out.println(employees.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		return employees;
	}
	
	public static OrgInfo queryOrg(String org_code){
		OrgInfo org = new OrgInfo();
		String sql = "select *from org where org_id='"+org_code+"'";
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.first();
			org.setOrgId(rs.getString("org_id"));
			org.setOrgName(rs.getString("name"));
			org.setParentOrgId(rs.getString("parent_id"));
			org.setOrgLevel(rs.getInt("level"));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		return org;
	}
	
	public static ArrayList<OrgInfo> queryDetailedOrg(String org_id,int size){
		ArrayList<OrgInfo> orgInfos = new ArrayList<>();
		OrgInfo org = queryOrg(org_id);
		orgInfos.add(org);
		while(org.getOrgLevel()>1){
			org = queryOrg(org.getParentOrgId());
			orgInfos.add(0,org);
		}
		for(int i = orgInfos.size();i<size;i++){
			orgInfos.add(new OrgInfo());
		}
		System.out.println("detailedOrgsInfo:"+orgInfos.size());
		return orgInfos;
	}
	
	public static ArrayList<OrgInfo> queryChildOrgs(String parent_id){
		String sql = "select *from org where parent_id='"+parent_id+"'";
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		ArrayList<OrgInfo> childOrgs = new ArrayList<>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.beforeFirst();
			while(rs.next()){
				OrgInfo org = new OrgInfo();
				org.setOrgId(rs.getString("org_id"));
				org.setOrgName(rs.getString("name"));
				org.setParentOrgId(rs.getString("parent_id"));
				org.setOrgLevel(rs.getInt("level"));
				childOrgs.add(org);
			}
			System.out.println("childOrgs:"+childOrgs.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		return childOrgs;
	}
}
