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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;




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
		System.out.println("post");
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String orgId = inputStream2String(req.getPart("org_id").getInputStream());
		String recode_type = inputStream2String(req.getPart("recode_type").getInputStream());
		String folderName = inputStream2String(req.getPart("folder_name").getInputStream());
		String path = req.getSession().getServletContext()
				.getRealPath("/WEB-INF/recode/"+orgId+"/"+recode_type+"/"+folderName+"/");
		System.out.println(path);
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		} 
		factory.setRepository(file);
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem>list = upload.parseRequest(req);
			for(FileItem item:list){
				String name = item.getFieldName();
				if(item.isFormField()){
					String value = item.getString();
					req.setAttribute(name, value);
				}else{
					item.write(file);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String inputStream2String(InputStream in) throws IOException {  
        StringBuffer out = new StringBuffer();  
        byte[] b = new byte[1024];  
        for (int n; (n = in.read(b)) != -1;) {  
            out.append(new String(b, 0, n));  
        }  
        return out.toString();  
    }  
}
