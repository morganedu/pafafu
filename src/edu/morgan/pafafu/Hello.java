package edu.morgan.pafafu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class Hello extends HttpServlet {

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();
	out.println("<!DOCTYPE html>");
	out.println("<html>");
	out.println("<head>");
	out.println("<title>Morgan Admissions - Web App</title>"); 
	out.println("<link href='Styles/bootstrap.min.css' rel='stylesheet'/>");
	out.println("</head>");
	out.println("<body style='background-color:#eee;'>");
	out.println("<div class='container' style='width:700px;'>");
	
	out.println("Hello!");
	
	out.println("</div>");
	out.println("</body>");
	out.println("</html>");
  }
	
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	  this.processRequest(req, resp);
  }
	  
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	  this.processRequest(req, resp);
  }
}