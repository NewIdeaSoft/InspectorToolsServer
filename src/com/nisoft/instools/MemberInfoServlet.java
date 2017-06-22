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
import com.nisoft.instools.bean.Employee;
import com.nisoft.instools.bean.OrgInfo;
import com.nisoft.instools.gson.EmployeeDataPackage;
import com.nisoft.instools.gson.OrgListPackage;
import com.nisoft.instools.jdbc.JDBCUtil;

/**
 * Servlet implementation class MemberInfoServlet
 */
// @WebServlet("/MemberInfoServlet")
public class MemberInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberInfoServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String intent = request.getParameter("intent");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		if (intent.equals("query")) {
			
			String company_id = request.getParameter("company_id");
			int structure_levels = Integer.parseInt(request.getParameter("structure_levels"));
			try {
				EmployeeDataPackage dataPackage = new EmployeeDataPackage();
				Employee employee = new Employee();
				ArrayList<OrgInfo> detailedOrgs = new ArrayList<>();

				ArrayList<OrgInfo> childOrgs = JDBCUtil.queryChildOrgs(company_id);
				ArrayList<ArrayList<OrgInfo>> orgsInfoForchoose = new ArrayList<>();
				ArrayList<Employee> employees = JDBCUtil.queryEmployeeResult("phone", phone);

				if (employees.size() == 1) {
					employee = employees.get(0);
					detailedOrgs = JDBCUtil.queryDetailedOrg(employee.getOrgId(),structure_levels);
					for (int i = 0; i < detailedOrgs.size(); i++) {
						orgsInfoForchoose.add(JDBCUtil.queryChildOrgs(detailedOrgs.get(i).getParentOrgId()));
					}
				} else {
					orgsInfoForchoose.add(childOrgs);
				}
				for (int j= detailedOrgs.size();j<structure_levels;j++){
					detailedOrgs.add(null);
				}
				for (int i = orgsInfoForchoose.size(); i < structure_levels; i++) {
					orgsInfoForchoose.add(null);
				}
				dataPackage.setEmployee(employee);
				dataPackage.setOrgInfo(detailedOrgs);
				dataPackage.setOrgsInfoForSelect(orgsInfoForchoose);
				Gson gson = new Gson();
				String json = gson.toJson(dataPackage);
				System.out.println(json);
				out.write(json);
			} catch (Exception e) {
				e.printStackTrace();
				out.write("false");
				
			}
		} else if (intent.equals("update")) {
			System.out.println("开始更新！");
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtil.getConnection();
				st = conn.createStatement();
				String json = request.getParameter("employee");
				System.out.println(json);
				Gson gson = new Gson();
				Employee employee = gson.fromJson(json, Employee.class);
				String name = employee.getName();
				String employee_id = employee.getWorkNum();
				String org_id = employee.getOrgId();
				String stations_code = "";
				if(employee.getPositionsId()!=null){
					stations_code = employee.getPositionsId().toString();
				}
				System.out.println("stations_code"+stations_code);
				String insertSql = "insert into employee" + "(phone,name,work_num,org_code,stations_code)values('"
						+ employee.getPhone() + "','" + name + "','" + employee_id + "','" + org_id + "','" + stations_code
						+ "') on duplicate key update name = values(name),work_num=values(work_num),"
						+ "org_code = values(org_code),stations_code = values(stations_code)";
				System.out.println(insertSql);
				int i = st.executeUpdate(insertSql);
				if (i == 1) {
					out.write("true");
				} else {
					out.write("用户信息提交失败！");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				out.write("错误：用户信息提交失败！");
			} finally {
				try {
					if (rs != null) {
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
			}

		}else if(intent.equals("secondary")){
			String parent_id = request.getParameter("parent_id");
			ArrayList<OrgInfo> childOrgs = JDBCUtil.queryChildOrgs(parent_id);
			OrgListPackage package1 = new OrgListPackage();
			package1.setOrgInfos(childOrgs);
			Gson gson = new Gson();
			String json = gson.toJson(package1);
			out.write(json);
		}
		out.close();

	}
}
