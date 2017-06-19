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
import com.nisoft.instools.jdbc.Company;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.jdbc.RegisterDataPackage;
import com.nisoft.instools.utils.StringsUtil;

//@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
				company.setOrgStructure(StringsUtil.stringToList(result.getString("structure")));
				allCompanies.add(company);
			}
			RegisterDataPackage datapackage = new RegisterDataPackage();
			datapackage.setCompanies(allCompanies);
			Gson gson = new Gson();
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
				if(row==1){
					String resultPassword = result.getString("password");
					System.out.println(resultPassword);
					if (resultPassword.equals(password)) {
						out.write("true");
						System.out.println("true");
					} else {
						out.write("登陆失败:密码错误！");
					}
				}else{
					out.write("登陆失败：用户不存在！");
				}
				
			}else if (type.equals("login")) {
				String insertSql = "insert into user (phone,psaaword,companyId)values('" + userName + "','"
						+ password + "','" + companyCode + "')";
				int i = st.executeUpdate(insertSql);
				System.out.println("增加了 " + i + " 名新用户！");
				if (i == 1) {
					out.write("true");
				} else {
					out.write("注册失败！");
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			out.write("系统错误！");
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

}
