package com.nisoft.instools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;


public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = req.getSession().getServletContext().getRealPath("/upload");
		factory.setRepository(new File(path));
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem>list = upload.parseRequest((RequestContext) req);
			for(FileItem item:list){
				String name = item.getFieldName();
				if(item.isFormField()){
					String value = item.getString();
					req.setAttribute(name, value);
				}else{
					String value = item.getName();
					
					int start = value.lastIndexOf("\\");
					String filename = value.substring(start+1);
					req.setAttribute(name, filename);
					OutputStream out = new FileOutputStream(new File(path,filename));
					InputStream in = item.getInputStream();
					
					int length = 0;
					byte[] buf = new byte[1024];
					System.out.println("获取文件总量的容量："+item.getSize());
					while((length=in.read(buf))!=-1){
						out.write(buf, 0, length);
					}
					in.close();
					out.close();
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
	
}
