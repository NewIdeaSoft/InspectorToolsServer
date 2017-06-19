package com.nisoft.instools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nisoft.instools.jdbc.JDBCUtil;

/**
 * Servlet implementation class OrgInfoSettingServlet
 */
@WebServlet("/OrgInfoSettingServlet")
public class OrgInfoSettingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrgInfoSettingServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String intent = request.getParameter("intent");
		PrintWriter out = response.getWriter();
		Connection connection = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql;
		try {
			st = connection.createStatement();
			if(intent.equals("company")){
				String parentId = request.getParameter("companyId");
				
				sql = "SELECT *FROM org_info WHERE parentId = '"+parentId +"'";
				rs = st.executeQuery(sql);
			}else if(intent.equals("station")){
				sql = "";
				rs = st.executeQuery(sql);
				
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
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			out.close();
		}
	}

}
