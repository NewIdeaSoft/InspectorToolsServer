package com.nisoft.instools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.nisoft.instools.bean.Company;
import com.nisoft.instools.bean.Employee;
import com.nisoft.instools.bean.OrgInfo;
import com.nisoft.instools.gson.EmployeeDataPackage;
import com.nisoft.instools.gson.EmployeeListPackage;
import com.nisoft.instools.gson.OrgListPackage;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.utils.StringsUtil;

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
				ArrayList<OrgInfo> detailedOrgs = new ArrayList<>();

				ArrayList<OrgInfo> childOrgs = JDBCUtil.queryChildOrgs(company_id);
				ArrayList<ArrayList<OrgInfo>> orgsInfoForchoose = new ArrayList<>();
				Employee employee = JDBCUtil.queryEmployeeWithPhone(phone);

				if (employee!=null) {
					detailedOrgs = JDBCUtil.queryDetailedOrg(employee.getOrgId(), structure_levels);
					for (int i = 0; i < detailedOrgs.size(); i++) {
						orgsInfoForchoose.add(JDBCUtil.queryChildOrgs(detailedOrgs.get(i).getParentOrgId()));
					}
				} else {
					orgsInfoForchoose.add(childOrgs);
				}
				for (int j = detailedOrgs.size(); j < structure_levels; j++) {
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
				if (employee.getPositionsId() != null) {
					stations_code = employee.getPositionsId().toString();
				}
				String companyId = employee.getCompanyId();
				System.out.println("stations_code" + stations_code);
				String insertSql = "insert into employee" + "(phone,name,work_num,org_code,stations_code,company_id)values('"
						+ employee.getPhone() + "','" + name + "','" + employee_id + "','" + org_id + "','"
						+ stations_code + "','" + companyId+ "') on duplicate key update name = values(name),work_num=values(work_num),"
						+ "org_code = values(org_code),stations_code = values(stations_code),company_id=values(company_id)";
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

		} else if (intent.equals("secondary")) {
			String parent_id = request.getParameter("parent_id");
			ArrayList<OrgInfo> childOrgs = JDBCUtil.queryChildOrgs(parent_id);
			OrgListPackage package1 = new OrgListPackage();
			package1.setOrgInfos(childOrgs);
			Gson gson = new Gson();
			String json = gson.toJson(package1);
			out.write(json);
		} else if (intent.equals("query_company")) {
			String companyId = getCompanyId(phone);
			Company company = new Company();
			if(companyId!=null){
				company = queryCompanyWithId(companyId);
				if(company!=null){
					Gson gson1 = new Gson();
					String json = gson1.toJson(company);
					out.write(json);
				}else{
					out.write("error!");
				}		
			}else{
				out.write("error!");
			}
		}else if(intent.equals("employees")){
			ArrayList<Employee> employees = queryAllEmployees();
			if(employees.size()>0){
				EmployeeListPackage listPackage = new EmployeeListPackage();
				listPackage.setEmployees(employees);
				Gson gson = new Gson();
				out.write(gson.toJson(listPackage));
			}else{
				out.write("zero");
			}
		}
		out.close();

	}
	private ArrayList<Employee> queryAllEmployees(){
		ArrayList<Employee> employees = new ArrayList<>();
		String sql = "select * from employee";
		Connection conn = JDBCUtil.getConnection();
		Statement st =null;
		ResultSet rs =null;
		try {
			st =conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			int row = rs.getRow();
			if(row>0){
				rs.beforeFirst();
				while(rs.next()){
					Employee employee = new Employee();
					String phone = rs.getString("phone");
					String name = rs.getString("name");
					String org_id = rs.getString("org_code");
					String work_num = rs.getString("work_num");
					String strings = rs.getString("stations_code");
					String companyId = rs.getString("company_id");
					if (strings != null) {
						ArrayList<String> stations_id = StringsUtil.getStrings(strings);
						employee.setPositionsId(stations_id);
					}
					employee.setWorkNum(work_num);
					employee.setPhone(phone);
					employee.setName(name);
					employee.setOrgId(org_id);
					employee.setCompanyId(companyId);
					employees.add(employee);
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
		}
		return employees;
	}

	private String getCompanyId(String phone) {
		String sql = "select *from user where phone = '" + phone + "'";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet result = null;
		try {
			st = conn.createStatement();
			result = st.executeQuery(sql);
			result.last();
			int row = result.getRow();
			System.out.println(row + "");
			result.first();
			if (row == 1) {
				String company_code = result.getString("companyId");
				return company_code;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
			} catch (Exception e2) {
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
		return null;
	}
	
	private Company queryCompanyWithId(String companyId){
		String sql = "select *from company where companyId = '" + companyId + "'";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet result = null;
		try {
			st = conn.createStatement();
			result = st.executeQuery(sql);
			result.last();
			int row = result.getRow();
			System.out.println(row + "");
			result.first();
			if (row == 1) {
				String company_name = result.getString("company_name");
				String structure = result.getString("structure");
				Company company = new Company();
				company.setOrgCode(companyId);
				company.setOrgName(company_name);
				company.setOrgStructure(StringsUtil.getStrings(structure));
				return company;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
			} catch (Exception e2) {
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
		return null;
	}
}
