package com.nisoft.instools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nisoft.instools.bean.ImageRecode;
import com.nisoft.instools.bean.ProblemDataPackage;
import com.nisoft.instools.bean.ProblemRecode;
import com.nisoft.instools.bean.Recode;
import com.nisoft.instools.jdbc.JDBCUtil;
import com.nisoft.instools.strings.RecodeDbSchema.RecodeTable;
import com.nisoft.instools.utils.StringsUtil;

/**
 * Servlet implementation class ProblemRecodeServlet
 */
@WebServlet("/ProblemRecodeServlet")
public class ProblemRecodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProblemRecodeServlet() {
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
	 * +
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String intent = request.getParameter("intent");
		switch (intent) {
		case "new_problem":
			String newProblemId = getRecodeId();
			if (newProblemId != null) {
				ProblemDataPackage data = new ProblemDataPackage(newProblemId);
				boolean isUpdated = update(data);
				if (isUpdated) {
					out.write(newProblemId);
				} else {
					out.write(newProblemId);
				}
			} else {
				out.write("false");
			}
			break;
		}
		out.close();
	}

	private String getRecodeId() {
		String sql = "select *from problem order by problem_id";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			int row = rs.getRow();
			Date date = new Date();
			String dateFormat = StringsUtil.dateFormatForNum(date);
			if (row == 0) {
				return dateFormat + "-" + 1;
			} else {
				String lastId = rs.getString("problem_id");
				String[] strs = lastId.split("-");
				String[] dateStrs = dateFormat.split("-");
				if (strs[0].equals(dateStrs[0]) || strs[1].equals(dateStrs[1])) {
					int num = Integer.parseInt(strs[strs.length - 1]) + 1;
					return dateFormat + "-" + num;
				} else {
					return dateFormat + "-" + 1;
				}
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
		return null;
	}

	private int update(String table, Recode recode) {
		String job_id = recode.getRecodeId();
		if (job_id == null || job_id.equals("")) {
			return -1;
		}
		// String job_type = recode.getType();
		// String folder = recode.getPicFolderPath();
		// String description = recode.getDescription();
		// String inspector_id = recode.getInspectorId();
		Date date = recode.getDate();
		if (date == null) {
			date = new Date();
		}
		String updateSql = updateSql(table, recode);
		System.out.println(updateSql);
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		int row = -1;
		try {
			st = conn.createStatement();
			row = st.executeUpdate(updateSql);
			System.out.println("update row:" + row);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("更新记录失败！");
		} finally {
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

	private boolean update(ProblemDataPackage data) {
		int a = update(RecodeTable.PROBLEM_NAME, data.getProblem());
		int b = update(RecodeTable.ANALYSIS_NAME, data.getAnalysis());
		int c = update(RecodeTable.PROGRAM_NAME, data.getProgram());
		int d = update(RecodeTable.RESULT_NAME, data.getResultRecode());
		if (a > 0 && b > 0 && c > 0 && d > 0) {
			return true;
		}
		return false;
	}

	private String updateSql(String table, Recode recode) {
		String sql = null;
		Date date = recode.getDate();
		if (date == null) {
			date = new Date();
		}
		long dateTime = date.getTime();
		long update_time = recode.getUpdateTime();
		if (table.equals(RecodeTable.ANALYSIS_NAME) || table.equals(RecodeTable.PROGRAM_NAME)) {
			sql = "insert into " + table + "(" + RecodeTable.Cols.PROBLEM_ID + "," + RecodeTable.Cols.TYPE + ","
					+ RecodeTable.Cols.AUTHOR + "," + RecodeTable.Cols.DATE + "," + RecodeTable.Cols.UPDATE_TIME + ","
					+ RecodeTable.Cols.DESCRIPTION_TEXT + ")values('" + recode.getRecodeId() + "','" + recode.getType()
					+ "','" + recode.getAuthor() + "','" + dateTime  + "','" + update_time + "','"
					+ recode.getDescription() + "') on duplicate key update " + RecodeTable.Cols.TYPE + "= values("
					+ RecodeTable.Cols.TYPE + ")," + RecodeTable.Cols.AUTHOR + "= values(" + RecodeTable.Cols.AUTHOR
					+ ")," + RecodeTable.Cols.DATE + "= values(" + RecodeTable.Cols.DATE + "),"
					+ RecodeTable.Cols.UPDATE_TIME + "= values(" + RecodeTable.Cols.UPDATE_TIME + "),"
					+ RecodeTable.Cols.DESCRIPTION_TEXT + "= values(" + RecodeTable.Cols.DESCRIPTION_TEXT + ")";
		} else if (table.equals(RecodeTable.RESULT_NAME)) {
			sql = "insert into " + table + "(" + RecodeTable.Cols.PROBLEM_ID + "," + RecodeTable.Cols.TYPE + ","
					+ RecodeTable.Cols.AUTHOR + "," + RecodeTable.Cols.DATE + "," + RecodeTable.Cols.UPDATE_TIME + ","
					+ RecodeTable.Cols.DESCRIPTION_TEXT + "," + RecodeTable.Cols.FOLDER_PATH + ")values('"
					+ recode.getRecodeId() + "','" + recode.getType() + "','" + recode.getAuthor() + "','" + dateTime
					+ "','" + update_time + "','" + recode.getDescription() + "','"
					+ ((ImageRecode) recode).getImagesFolderPath() + "') on duplicate key update "
					+ RecodeTable.Cols.TYPE + "= values(" + RecodeTable.Cols.TYPE + ")," + RecodeTable.Cols.AUTHOR
					+ "= values(" + RecodeTable.Cols.AUTHOR + ")," + RecodeTable.Cols.DATE + "= values("
					+ RecodeTable.Cols.DATE + ")," + RecodeTable.Cols.UPDATE_TIME + "= values("
					+ RecodeTable.Cols.UPDATE_TIME + ")," + RecodeTable.Cols.DESCRIPTION_TEXT + "= values("
					+ RecodeTable.Cols.DESCRIPTION_TEXT + ")," + RecodeTable.Cols.FOLDER_PATH + "= values("
					+ RecodeTable.Cols.FOLDER_PATH + ")";
		} else if (table.equals(RecodeTable.PROBLEM_NAME)) {
			sql = "insert into " + table + "(" + RecodeTable.Cols.PROBLEM_ID + "," + RecodeTable.Cols.TYPE + ","
					+ RecodeTable.Cols.AUTHOR + "," + RecodeTable.Cols.DATE + "," + RecodeTable.Cols.SUSPECTS + ","
					+ RecodeTable.Cols.UPDATE_TIME + "," + RecodeTable.Cols.DESCRIPTION_TEXT + ","
					+ RecodeTable.Cols.FOLDER_PATH + "," + RecodeTable.Cols.TITLE + "," + RecodeTable.Cols.ADDRESS
					+ ")values('" + recode.getRecodeId() + "','" + recode.getType() + "','" + recode.getAuthor() + "','"
					+ dateTime + "','" + "" + "','" + recode.getUpdateTime() + "','" + recode.getDescription() + "','"
					+ ((ProblemRecode) recode).getImagesFolderPath() + "','" + ((ProblemRecode) recode).getTitle()
					+ "','" + ((ProblemRecode) recode).getAddress() + "') on duplicate key update "
					+ RecodeTable.Cols.TYPE + "= values(" + RecodeTable.Cols.TYPE + ")," + RecodeTable.Cols.AUTHOR
					+ "= values(" + RecodeTable.Cols.AUTHOR + ")," + RecodeTable.Cols.DATE + "= values("
					+ RecodeTable.Cols.DATE + ")," + RecodeTable.Cols.SUSPECTS + "= values(" + RecodeTable.Cols.SUSPECTS
					+ ")," + RecodeTable.Cols.UPDATE_TIME + "= values(" + RecodeTable.Cols.UPDATE_TIME + "),"
					+ RecodeTable.Cols.DESCRIPTION_TEXT + "= values(" + RecodeTable.Cols.DESCRIPTION_TEXT + "),"
					+ RecodeTable.Cols.FOLDER_PATH + "= values(" + RecodeTable.Cols.FOLDER_PATH + "),"
					+ RecodeTable.Cols.TITLE + "= values(" + RecodeTable.Cols.TITLE + ")," + RecodeTable.Cols.ADDRESS
					+ "= values(" + RecodeTable.Cols.ADDRESS + ")";
		}
		return sql;
	}
}
