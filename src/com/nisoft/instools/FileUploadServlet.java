package com.nisoft.instools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("post");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");  
		PrintWriter out = response.getWriter();
        String uploadFileName = ""; // 上传的文件名  
        String fieldName = ""; // 表单字段元素的name属性值  
        // 请求信息中的内容是否是multipart类型  
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
        System.out.println("isMultipart:"+isMultipart); 
        
        if (isMultipart) { 
        	String orgId = "";
        	String recode_type = "";
        	String folderName = "";
            FileItemFactory factory = new DiskFileItemFactory();  
            ServletFileUpload upload = new ServletFileUpload(factory);  
            try {  
                // 解析form表单中所有文件  
                List<FileItem> items = upload.parseRequest(request); 
                ArrayList<FileItem> fileItems = new ArrayList<>();
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) { // 依次处理每个文件  
                    FileItem item = (FileItem) iter.next();  
                    if (item.isFormField()) { // 普通表单字段  
                        fieldName = item.getFieldName(); // 表单字段的name属性值  
                        if (fieldName.equals("org_id")) {  
                            // 输出表单字段的值  
                        	orgId=item.getString("UTF-8"); 
                        } else if(fieldName.equals("recode_type")){
                        	recode_type=item.getString("UTF-8");
                        } else if(fieldName.equals("folder_name")){
                        	folderName=item.getString("UTF-8");
                        }
                    } else { // 文件表单字段  
                        fileItems.add(item);
                    } 
                    
                }
                String path = request.getSession().getServletContext()
        				.getRealPath("/WEB-INF/recode/"+orgId+"/"+recode_type+"/"+folderName+"/");
                System.out.println(path);
        		File file = new File(path);
        		if(!file.exists()){
        			file.mkdirs();
        		} 
                for(FileItem item:fileItems){
                	String fullFileName = item.getName();
                	if (fullFileName != null && !fullFileName.equals("")) {  
                        File fullFile = new File(fullFileName);
                        String fileName = fullFile.getName();
                        String [] strs = fileName.split("\\.");
                        String fileType = strs[strs.length-1];
                        File saveFile = new File(path, fileName); 
                        if(!(fileType.equals("jpg")&&saveFile.exists())){
							item.write(saveFile);
							uploadFileName = fullFile.getName();
							System.out.println("上传的文件名是:" + uploadFileName);
							out.write(uploadFileName + "上传完成！");
                        }
                    }  
                }
               
            } catch (Exception e) {  
                e.printStackTrace();  
            }  finally{
            	out.close();
            }
        }  
		
	}
}
