package com.nisoft.instools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nisoft.instools.utils.FileUtils;

/**
 * Servlet implementation class DownloadRecodeServlet
 */
@WebServlet("/DownloadRecodeServlet")
public class DownloadRecodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadRecodeServlet() {
        super();
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String org_id = request.getParameter("org_id");
		String recode_type = request.getParameter("recode_type");
		String job_id = request.getParameter("job_id");
		String imagesDirPath = request.getSession().getServletContext()
				.getRealPath("/recode/"+org_id+"/"+recode_type+"/"+job_id+"/");
		File dir = new File(imagesDirPath);
		if(dir.exists()){
			String [] filesName = dir.list();
			ArrayList<String> names = new ArrayList<>();
			for(String name:filesName){
				String fileType = FileUtils.getFileType(name);
				if(fileType.equals("jpg")||fileType.equals("bmp")){
					names.add(name);
				}
			}
			if(names.size()>0){
				out.write(names.toString());
			}else{
				out.write("no_image");
			}
		}else{
			out.write("no_image");
		}
		
		out.close();
	}

}
