package com.nisoft.instools;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.nisoft.instools.jdbc.Employee;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.utils.StringsUtil;

/**
 * Servlet implementation class MemberInfoServlet
 */
//@WebServlet("/MemberInfoServlet")
public class MemberInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberInfoServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String phone = request.getParameter("phone");
		String intent = request.getParameter("intent");
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		try {
			
//			if(intent.equals("update")&&row==1){
//				rs.first();
//				int id = rs.getInt("id");
//				String name = request.getParameter("name");
//				String employee_id = request.getParameter("employee_id");
//				String org_id = request.getParameter("org_id");
//				String stations_code = request.getParameter("stations_code");
//				String updateSql = "update employee set name = '"+name+"','"
//						+"employee_id = '"+employee_id+"','"
//						+"org_id = '"+org_id+"','"
//						+"stations_code = '"+stations_code
//						+"where id = "+id;
//				int j = st.executeUpdate(updateSql);
//				if(j==1){
//					out.write("更新用户信息成功！");
//				}else{
//					out.write("更新用户信息失败！");
//				}
//			}else 
			st = conn.createStatement();
			if(intent.equals("query")){
				String sql = "SELECT *FROM employee WHERE phone = '"+phone+"'";
				rs = st.executeQuery(sql);
//				rs.last();
//				int row = rs.getRow();
				rs.first();
				String name = request.getParameter("name");
				String employee_id = request.getParameter("employee_id");
				String org_id = request.getParameter("org_id");
				String stations_code = request.getParameter("stations_code");
				Employee member = new Employee();
				member.setPhone(phone);
				member.setName(name);
				member.setWorkNum(employee_id);
				member.setOrgId(org_id);
				member.setPositionsId(StringsUtil.getStrings(stations_code));
				Gson gson = new Gson();
				String json = gson.toJson(member);
				out.write(json);	
			}else if(intent.equals("update")){
				String json = request.getParameter("employee");
				Gson gson = new Gson();
				Employee employee = gson.fromJson(json, Employee.class);
				String name = employee.getName();
				String employee_id = employee.getWorkNum();
				String org_id = employee.getOrgId();
				String stations_code = employee.getPositionsId().toString();
				String insertSql = "insert into employee"
						+ "(phone,name,work_num,org_code,stations_code)values('"
						+phone+"','" +name+"','"+employee_id+"','"+org_id+"','"+stations_code
						+"') on duplicate key update name = values(name),employee_id=value(employee_id),"
						+"org_id = value(org_id),stations_code = values(stations_code)";
				
				int i = st.executeUpdate(insertSql);
				if(i==1){
					out.write("用户信息提交成功！");
				}else{
					out.write("用户信息提交失败！");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				
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
			out.close();
		}
		
	}
	
}
