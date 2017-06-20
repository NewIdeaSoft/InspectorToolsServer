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
import java.util.ArrayList;

import com.google.gson.Gson;
import com.nisoft.instools.jdbc.Employee;
import com.nisoft.instools.jdbc.EmployeeDataPackage;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.jdbc.OrgInfo;

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
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String phone = request.getParameter("phone");
		String parent_id = request.getParameter("company_id");
		String intent = request.getParameter("intent");
		PrintWriter out = response.getWriter();
		
		try {
			if(intent.equals("query")){
				EmployeeDataPackage dataPackage = new EmployeeDataPackage();
				Employee employee = new Employee();
				ArrayList<OrgInfo> detailedOrgs = new ArrayList<>();
				ArrayList<OrgInfo> childOrgs = JDBCUtil.queryChildOrgs(parent_id);
				ArrayList<ArrayList<OrgInfo>> orgsInfoForchoose = new ArrayList<>();
				ResultSet result = JDBCUtil.query("employee", "phone", phone);
				if(JDBCUtil.queryResult(result).size()==1){
					employee = JDBCUtil.queryResult(result).get(0);
					detailedOrgs = JDBCUtil.queryDetailedOrg(employee.getOrgId());
					for(int i = 0;i<detailedOrgs.size();i++){
						orgsInfoForchoose.add(JDBCUtil.queryChildOrgs(detailedOrgs.get(i).getParentOrgId()));
					}
				}else{
					orgsInfoForchoose.add(childOrgs);
				}
				dataPackage.setEmployee(employee);
				dataPackage.setOrgInfo(detailedOrgs);
				dataPackage.setOrgsInfoForSelect(orgsInfoForchoose);
				Gson gson = new Gson();
				String json = gson.toJson(dataPackage);
				out.write(json);		
			}else if(intent.equals("update")){
				conn = JDBCUtil.getConnection();
				st = conn.createStatement();
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
			out.write("false");
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
