import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;


public class Upload extends HttpServlet{

	private boolean isMultipart;
	private String filePath;
	private int maxFileSize=500*1024;
	private int maxMemSize=4*1024;
	private File file;
	
	public void init(){
		filePath=getServletContext().getInitParameter("file-upload");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		isMultipart = ServletFileUpload.isMultipartContent(req);
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		if(!isMultipart){
			out.println("<html>");
			out.println("<head>");
	         out.println("<title>Servlet upload</title>");  
	         out.println("</head>");
	         out.println("<body>");
	         out.println("<p>No file uploaded</p>"); 
	         out.println("</body>");
	         out.println("</html>");
	         return;
		}
		DiskFileItemFactory factory=new DiskFileItemFactory();
		factory.setSizeThreshold(maxMemSize);
		
		factory.setRepository(new File("E:/temp/"));
		ServletFileUpload upload=new ServletFileUpload(factory);
		upload.setSizeMax(maxFileSize);
		
		try{
			Map<String, List<FileItem>> fileitems=(Map<String, List<FileItem>>)upload.parseParameterMap(req);
			Iterator<Entry<String, List<FileItem>>> i=fileitems.entrySet().iterator();
			out.println("<html>");
		      out.println("<head>");
		      out.println("<title>Servlet upload</title>");  
		      out.println("</head>");
		      out.println("<body>");
		      out.println("try statement passe");
		      FileItem fi = null;
		      while(i.hasNext()){
		    	  Map.Entry<String, List<FileItem>> fil=(Map.Entry<String,List<FileItem>>)i.next();
		    	  List<FileItem> lfi=fil.getValue();

		    	    for(FileItem fitem:lfi){
		    	        fi=(FileItem) fitem;
		    	    }
		    	  
		    	  if(!fi.isFormField()){
		    		  String fieldName=fi.getFieldName();
		    		  String fileName=fi.getName();
		    		  String contentType=fi.getContentType();
		    		  boolean isInMemory=fi.isInMemory();
		    		  long sizeInBytes=fi.getSize();
		    		  
		    		  if(fileName.lastIndexOf("\\")>=0){
		    			  file=new File(filePath+fileName.substring(fileName.lastIndexOf("\\")));
		    		  }
		    		  else{
		    			  file = new File(filePath+fileName.substring(fileName.lastIndexOf("\\")+1));
		    		  }
		    		  fi.write(file);
		    		  out.println("Uploaded Filename: "+fileName+"<br>");
		    	  }
		      }
		      
		      out.println("</body>");
		      out.println("</html>");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		throw new ServletException("GET method used with "+ getClass().getName()+" : POST method required");
	}
}
