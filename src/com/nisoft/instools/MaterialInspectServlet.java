package com.nisoft.instools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.nisoft.instools.bean.MaterialInspectRecode;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.utils.StringsUtil;

/**
 * Servlet implementation class MaterialInspectServlet
 */
@WebServlet("/MaterialInspectServlet")
public class MaterialInspectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaterialInspectServlet() {
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String intent = request.getParameter("intent");
		String type = request.getParameter("type");
		switch (intent) {
		case "new":
			String newNum = getNewJobNum(type);
			out.write(newNum);
			break;
		case "query":
			ArrayList<String> joblist = queryAll(type);
			out.write(joblist.toString());
			break;
		case "start_work":
			String job_id = request.getParameter("job_id");
			MaterialInspectRecode recode = queryJobWithId(job_id);
			Gson gson = new Gson();
			out.write(gson.toJson(recode));
			break;
		}
		out.close();
	}

	private String getNewJobNum(String type) {
		String number = null;
		String sql = "select * from material_inspect where type = '" + type + "' order by job_id";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			String job_id = rs.getString("job_id");
			String[] strsJobId = job_id.split("-");
			Date date = new Date();
			String fomatDate = StringsUtil.dateFormatForNum(date);
			String[] strsDate = fomatDate.split("-");

			int num = Integer.parseInt(strsJobId[strsJobId.length - 1]) + 1;
			switch (type) {
			case "金属材料":
				if (strsDate[0].equals(strsJobId[0])) {
					number = strsDate[0] + "-" + num;
				} else {
					number = strsDate[0] + "-" + 5001;
				}
				break;
			case "非金属材料":
				if (strsDate[0].equals(strsJobId[0])) {
					number = strsDate[0] + "-" + num;
				} else {
					number = strsDate[0] + "-" + 1;
				}
				break;
			case "外购件":
				if (strsDate[0].equals(strsJobId[0]) && strsDate[1].equals(strsJobId[1])) {
					number = strsDate[0] + "-" + strsDate[0] + "-" + num;
				} else {
					number = strsDate[0] + "-" + strsDate[0] + "-" + 1;
				}
				break;
			case "其他":
				break;
			}
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
		return number;
	}

	private ArrayList<String> queryAll(String type) {
		ArrayList<String> allJobs = null;
		String sql = "select * from material_inspect where type = '" + type + "' order by job_id";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.beforeFirst();
			allJobs = new ArrayList<>();
			while (rs.next()) {
				allJobs.add(rs.getString("job_id"));
			}
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
		return allJobs;
	}

	private MaterialInspectRecode queryJobWithId(String job_id) {
		MaterialInspectRecode job = new MaterialInspectRecode();
		String sql = "select * from material_inspect where job_id = '" + job_id + "'";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.first();
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
		return job;
	}
}
