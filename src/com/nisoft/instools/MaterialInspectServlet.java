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
	public static final String PATH = "http://47.93.191.62:8080/recode/JXCZ/原材料检验/";

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
			if(newNum!=null){
				MaterialInspectRecode newRecode = new MaterialInspectRecode();
				newRecode.setJobNum(newNum);
				newRecode.setType(type);
				update(newRecode);
			}
			out.write(newNum);
			break;
		case "query":
			ArrayList<String> joblist = queryAll(type);
			out.write(joblist.toString());
			break;
		case "recoding":
			String job_id = request.getParameter("job_id");
			MaterialInspectRecode recode = queryJobWithId(job_id);
			Gson gson = new Gson();
			out.write(gson.toJson(recode));
			break;
		case "jub_num":
			String job_num = request.getParameter("job_id");
			MaterialInspectRecode recode1 = queryJobWithId(job_num);
			if (recode1 == null) {
				out.write("false");
			} else {
				out.write("true");
			}
			break;
		case "upload":
			String jobJson = request.getParameter("job_json");
			Gson gson1 = new Gson();
			MaterialInspectRecode jobRecode = gson1.fromJson(jobJson, MaterialInspectRecode.class);
			String picFolderPath = PATH+jobRecode.getType()+"/"+jobRecode.getJobNum();
			System.out.println(picFolderPath);
			jobRecode.setPicFolderPath(picFolderPath);
			int row = update(jobRecode);
			if (row>0){
				out.write("OK");
			}else{
				out.write("上传失败！");
			}
			break;
		case "change_id":
			String newId = request.getParameter("newId");
			String oldId = request.getParameter("oldId");
			String folderPath = PATH+type+"/"+newId;
			int changedRow = changeJobId(newId,oldId,folderPath);
			if(changedRow == 1){
				out.write("OK");
			}else{
				out.write("failed");
			}
			break;
		}
		out.close();
	}

	private int changeJobId(String newId,String oldId,String folderPath) {
		String sql = "update job_material_inspect set job_id = '"+newId+"',folder='"+folderPath
				+"' where job_id = '"+oldId+"'";
		System.out.println(sql);
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		int row = -1;
		try {
			st = conn.createStatement();
			row = st.executeUpdate(sql);
			System.out.println("changeJobId:"+row);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("更新编号失败！");
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

	private String getNewJobNum(String type) {
		String number = null;
		String sql = "select * from job_material_inspect where job_type = '" + type + "' order by job_id";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			int row = rs.getRow();
			String job_id = null;
			String[] strsJobId = null;
			int num = 0;
			Date date = new Date();
			String fomatDate = StringsUtil.dateFormatForNum(date);
			String[] strsDate = fomatDate.split("-");
			if(row>0){
				job_id = rs.getString("job_id");
				strsJobId = job_id.split("-");
				num = Integer.parseInt(strsJobId[strsJobId.length - 1]) + 1;
			}
			
			switch (type) {
			case "金属材料":
				if(job_id!=null&&strsDate[0].equals(strsJobId[0])){
					number = strsDate[0] + "-" + num;
				}else {
					number = strsDate[0] + "-" + 5001;
				}
				
				break;
			case "非金属材料":
				if (job_id!=null&&strsDate[0].equals(strsJobId[0])) {
					number = strsDate[0] + "-" + num;
				} else {
					number = strsDate[0] + "-" + 1;
				}
				break;
			case "外购件":
				if (job_id!=null&&strsDate[0].equals(strsJobId[0]) && strsDate[1].equals(strsJobId[1])) {
					number = strsDate[0] + "-" + strsDate[1] + "-" + num;
				} else {
					number = strsDate[0] + "-" + strsDate[1] + "-" + 1;
				}
				break;
			case "其他":
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			number = "获取编号失败！";
			System.out.println("获取编号失败！");
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
		String sql = "select * from job_material_inspect where job_type = '" + type + "' order by job_id";
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
			System.out.println("查询失败！");
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
		MaterialInspectRecode job = null;
		String sql = "select * from job_material_inspect where job_id = '" + job_id + "'";
		Connection conn = JDBCUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.last();
			int row = rs.getRow();
			if (row == 1) {
				job = new MaterialInspectRecode();
				job.setJobNum(rs.getString("job_id"));
				job.setType(rs.getString("job_type"));
				job.setDescription(rs.getString("description"));
				job.setDate(new Date(rs.getLong("date")));
				job.setInspectorId("inspector_id");
				job.setPicFolderPath(rs.getString("folder"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询记录失败！");
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

	private int update(MaterialInspectRecode recode) {
		String job_id = recode.getJobNum();
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
		long update_time = recode.getLatestUpdateTime();
		String insertSql = "insert into job_material_inspect"
				+ "(job_id,job_type,folder,description,inspector_id,date,update_time)values('" + recode.getJobNum() + "','"
				+ recode.getType() + "','" + recode.getPicFolderPath() + "','" + recode.getDescription() + "','"
				+ recode.getInspectorId() + "','" + dateTime+ "','"+update_time
				+ "') on duplicate key update job_type = values(job_type),folder=values(folder),"
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
}
