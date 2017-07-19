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
import com.nisoft.instools.gson.RegisterDataPackage;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.utils.GsonUtil;
import com.nisoft.instools.utils.StringsUtil;

//@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int i = 0;

	public LoginServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// query all companies which had already registered
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String sql = "select *from company";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet result = null;
		try {
			st = conn.createStatement();
			result = st.executeQuery(sql);
			result.last();
			int row = result.getRow();
			System.out.println(row + "");
			result.beforeFirst();
			ArrayList<Company> allCompanies = new ArrayList<>();
			while (result.next()) {
				Company company = new Company();
				company.setOrgCode(result.getString("companyId"));
				company.setOrgName(result.getString("company_name"));
				company.setOrgStructure(StringsUtil.getStrings((result.getString("structure"))));
				allCompanies.add(company);
			}
			System.out.println(allCompanies.size());
			RegisterDataPackage datapackage = new RegisterDataPackage();
			datapackage.setCompanies(allCompanies);
			Gson gson = GsonUtil.getDateFormatGson();
			out.write(gson.toJson(datapackage));

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
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		i++;
		System.out.println("第" + i + "名用户");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		String companyCode = req.getParameter("org_code");
		String type = req.getParameter("intent");
		System.out.println(userName + "  " + password);
		String sql = "select *from user where phone = '" + userName + "'";

		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet result = null;
		try {
			st = conn.createStatement();
			if (type.equals("login")) {
				result = st.executeQuery(sql);
				result.last();
				int row = result.getRow();
				System.out.println(row + "");
				result.first();
				if (row == 1) {
					String resultPassword = result.getString("password");
					System.out.println(resultPassword);
					if (resultPassword.equals(password)) {
						Employee employee = JDBCUtil.queryEmployeeWithPhone(userName);
						if (employee != null) {
							Gson gson = GsonUtil.getDateFormatGson();
							String json = gson.toJson(employee);
							out.write(json);
						} else {
							out.write("non_info");
						}
					} else {
						out.write("error:1");
					}
				} else {
					out.write("error:2");
				}

			} else if (type.equals("register")) {

				int row = queryNewUser(userName, companyCode);
				if (row <= 0) {
					out.write("注册失败：尚未加入该公司，请与该公司管理员联系！");
				} else {
					String insertSql = "insert into user (phone,password,companyId)values('" + userName + "','"
							+ password + "','" + companyCode + "')";
					int i = st.executeUpdate(insertSql);
					System.out.println("增加了 " + i + " 名新用户！");
					if (i == 1) {
						out.write("true");
					} else {
						out.write("注册失败：用户已存在！");
					}
				}
			}
		} catch (Exception e) {
			out.write("error:3");
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
			out.close();
		}
	}

	private int queryNewUser(String phone, String companyId) {
		String querySql = "select * from employee where phone = '" + phone + "' and company_id = '" + companyId + "'";
		System.out.println(querySql);
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		int row = 0;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(querySql);
			rs.last();
			row = rs.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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

		return row;

	}
}
