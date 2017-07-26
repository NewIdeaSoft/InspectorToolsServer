package com.nisoft.instools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nisoft.instools.bean.product.WorkType;
import com.nisoft.instools.jdbc.JDBCUtil;

/**
 * Servlet implementation class ProductDataServlet
 */
@WebServlet("/ProductDataServlet")
public class ProductDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductDataServlet() {
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
		String companyId = request.getParameter("company_id");
		ArrayList<WorkType> typeList = findAllWorkType(companyId);
		PrintWriter out = response.getWriter();
		if (typeList == null) {
			out.write("none");
		} else {
			out.write(typeList.toString());
		}
	}

	private ArrayList<WorkType> findAllWorkType(String companyId) {
		String sql = "select * from work_type where company_code = '" + companyId + "'";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			int row = rs.getRow();
			if (row > 0) {
				rs.beforeFirst();
				ArrayList<WorkType> typeList = new ArrayList<>();
				while (rs.next()) {
					String id = rs.getString("type_id");
					String name = rs.getString("name");
					String branch = rs.getString("branch");
					WorkType workType = new WorkType();
					workType.setTypeId(id);
					workType.setName(name);
					workType.setCompanyId(companyId);
					workType.setBranch(branch);
				}
				return typeList;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
