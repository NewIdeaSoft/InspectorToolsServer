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
		int row = -1;
		Connection conn = JDBCUtil.getConnection();
		String sql = "SELECT *FROM employee WHERE phone = '"+phone+"'";
		Statement st = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			row = rs.getRow();
			if(intent.equals("update")&&row==1){
				rs.first();
				int id = rs.getInt("id");
				String name = request.getParameter("name");
				String work_num = request.getParameter("work_num");
				String org_code = request.getParameter("org_id");
				String stations_code = request.getParameter("stations_code");
				String updateSql = "update employee set name = '"+name+"','"
						+"work_num = '"+work_num+"','"
						+"org_code = '"+org_code+"','"
						+"stations_code = '"+stations_code
						+"where id = "+id;
				int j = st.executeUpdate(updateSql);
				if(j==1){
					out.write("更新用户信息成功！");
				}else{
					out.write("更新用户信息失败！");
				}
			}else if(intent.equals("query")&&row ==1){
				rs.first();
				String name = rs.getString("name");
				String work_num = rs.getString("work_num");
				String org_code = rs.getString("org_id");
				String stations_code = rs.getString("stations_code");
				Employee member = new Employee();
				member.setPhone(phone);
				member.setName(name);
				member.setWorkNum(work_num);
				member.setOrgId(org_code);
				member.setPositionsId(StringsUtil.stringToList(stations_code));
				Gson gson = new Gson();
				String json = gson.toJson(member);
				out.write(json);	
			}else if(intent.equals("insert")&&row==0){
				String name = request.getParameter("name");
				String work_num = request.getParameter("work_num");
				String org_code = request.getParameter("org_id");
				String stations_code = request.getParameter("stations_code");
				String insertSql = "insert into employee"
						+ "(phone,name,work_num,org_code,stations_code)values('"
						+phone+"','" +name+"','"+work_num+"','"+org_code+"','"+stations_code+"')";
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
			out.close();
		}
		
	}
	
}
