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

import com.nisoft.instools.bean.MaterialInspectRecode;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**+
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String intent = request.getParameter("intent");
		switch(intent){
		case "new_problem":
			String newProblemId = getRecodeId();
			if(newProblemId!=null){
				out.write(newProblemId);
			}else{
				out.write("false");
			}
			break;
		}
		out.close();
	}
	
	private String getRecodeId(){
		String sql = "select *from problem_recode recode order by problem_id";
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
			if(row == 0){
				return dateFormat+"-"+1;
			}else{
				String lastId = rs.getString("problem_id");
				String []strs = lastId.split("-");
				String []dateStrs = dateFormat.split("-");
				if(strs[0].equals(dateStrs[0])||strs[1].equals(dateStrs[1])){
					int num = Integer.parseInt(strs[strs.length-1])+1;
					return dateFormat+"-"+num;
				}else{
					return dateFormat+"-"+1;
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
		return null;
	}
	private int update(String table,Recode recode) {
		String job_id = recode.getRecodeId();
		if(job_id == null||job_id.equals("")){
			return -1;
		}
//		String job_type = recode.getType();
//		String folder = recode.getPicFolderPath();
//		String description = recode.getDescription();
//		String inspector_id = recode.getInspectorId();
		Date date = recode.getDate();
		if(date == null){
			date = new Date();
		}
		long dateTime = date.getTime();
		long update_time = recode.getUpdateTime();
		String insertSql = "insert into problem_recode"
				+ "(problem_id,problem_type,description,author_id,date,update_time)values('" + job_id + "','"
				+ recode.getType()  + "','" + recode.getDescription() + "','"
				+ recode.getAuthor() + "','" + dateTime+ "','"+update_time
				+ "') on duplicate key update job_type = values(job_type),"
				+ "description = values(description),inspector_id = values(inspector_id),"
				+ "date = values(date),update_time = values(update_time)";
		System.out.println(insertSql);
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		int row = -1;
		try {
			st = conn.createStatement();
			row = st.executeUpdate(insertSql);
			System.out.println("update row:"+row);

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
	private String updateSql(String table,Recode recode){
		String sql = null;
		Date date = recode.getDate();
		if(date == null){
			date = new Date();
		}
		long dateTime = date.getTime();
		long update_time = recode.getUpdateTime();
		if(table.equals(RecodeTable.ANALYSIS_NAME)||table.equals(RecodeTable.PROGRAM_NAME)){
			sql = "insert into "+table +"("+
					RecodeTable.Cols.PROBLEM_ID+","+
					RecodeTable.Cols.TYPE + ","+ 
					RecodeTable.Cols.AUTHOR + ","+
					RecodeTable.Cols.DATE + ","+ 
					RecodeTable.Cols.SUSPECTS + ","+ 
					RecodeTable.Cols.UPDATE_TIME + ","+ 
					RecodeTable.Cols.DESCRIPTION_TEXT +
					")values('"+
					recode.getRecodeId()+"','"+
					recode.getType()+"','"+
					recode.getAuthor()+"','"+
					dateTime+"','"+
					""+"','"+
					recode.getUpdateTime()+"','"+
					recode.getDescription()+ 
					"') on duplicate key update"+
					RecodeTable.Cols.TYPE+"= values("+RecodeTable.Cols.TYPE+"),"+
					RecodeTable.Cols.AUTHOR+"= values("+RecodeTable.Cols.AUTHOR+"),"+
					RecodeTable.Cols.DATE+"= values("+RecodeTable.Cols.DATE+"),"+
					RecodeTable.Cols.SUSPECTS+"= values("+RecodeTable.Cols.SUSPECTS+"),"+
					RecodeTable.Cols.UPDATE_TIME+"= values("+RecodeTable.Cols.UPDATE_TIME+"),"+
					RecodeTable.Cols.DESCRIPTION_TEXT+"= values("+RecodeTable.Cols.DESCRIPTION_TEXT+")";
		}else if(table.equals(RecodeTable.RESULT_NAME)){
			sql = "insert into "+table +"("+
					RecodeTable.Cols.PROBLEM_ID+","+
					RecodeTable.Cols.TYPE + ","+ 
					RecodeTable.Cols.AUTHOR + ","+
					RecodeTable.Cols.DATE + ","+ 
					RecodeTable.Cols.SUSPECTS + ","+ 
					RecodeTable.Cols.UPDATE_TIME + ","+ 
					RecodeTable.Cols.DESCRIPTION_TEXT +
					")values('"+
					recode.getRecodeId()+"','"+
					recode.getType()+"','"+
					recode.getAuthor()+"','"+
					dateTime+"','"+
					""+"','"+
					recode.getUpdateTime()+"','"+
					recode.getDescription()+ 
					"') on duplicate key update"+
					RecodeTable.Cols.TYPE+"= values("+RecodeTable.Cols.TYPE+"),"+
					RecodeTable.Cols.AUTHOR+"= values("+RecodeTable.Cols.AUTHOR+"),"+
					RecodeTable.Cols.DATE+"= values("+RecodeTable.Cols.DATE+"),"+
					RecodeTable.Cols.SUSPECTS+"= values("+RecodeTable.Cols.SUSPECTS+"),"+
					RecodeTable.Cols.UPDATE_TIME+"= values("+RecodeTable.Cols.UPDATE_TIME+"),"+
					RecodeTable.Cols.DESCRIPTION_TEXT+"= values("+RecodeTable.Cols.DESCRIPTION_TEXT+")";
		}else if(table.equals(RecodeTable.PROBLEM_NAME)){
			sql = "insert into "+table +"("+
					RecodeTable.Cols.PROBLEM_ID+","+
					RecodeTable.Cols.TYPE + ","+ 
					RecodeTable.Cols.AUTHOR + ","+
					RecodeTable.Cols.DATE + ","+ 
					RecodeTable.Cols.SUSPECTS + ","+ 
					RecodeTable.Cols.UPDATE_TIME + ","+ 
					RecodeTable.Cols.DESCRIPTION_TEXT +
					")values('"+
					recode.getRecodeId()+"','"+
					recode.getType()+"','"+
					recode.getAuthor()+"','"+
					dateTime+"','"+
					""+"','"+
					recode.getUpdateTime()+"','"+
					recode.getDescription()+ 
					"') on duplicate key update"+
					RecodeTable.Cols.TYPE+"= values("+RecodeTable.Cols.TYPE+"),"+
					RecodeTable.Cols.AUTHOR+"= values("+RecodeTable.Cols.AUTHOR+"),"+
					RecodeTable.Cols.DATE+"= values("+RecodeTable.Cols.DATE+"),"+
					RecodeTable.Cols.SUSPECTS+"= values("+RecodeTable.Cols.SUSPECTS+"),"+
					RecodeTable.Cols.UPDATE_TIME+"= values("+RecodeTable.Cols.UPDATE_TIME+"),"+
					RecodeTable.Cols.DESCRIPTION_TEXT+"= values("+RecodeTable.Cols.DESCRIPTION_TEXT+")";
		}
		return sql;
	}
}
