package org.rsol.poc.springsession.hazlecast;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CookieInfo
 */
@WebServlet("/CookieInfo")
public class CookieInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CookieInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		   Cookie[] cookies = null;
		   PrintWriter out = response.getWriter();
		   // Get an array of Cookies associated with this domain
		   cookies = request.getCookies();
		   
		   displayCookieInfo(cookies, out);		
		   String delete = request.getParameter("delete");
		   if(delete != null){
				response.addCookie(deleteCookie(delete));
				out.println("<h2>cookie with name" + delete + " deleted </h2>");
				
		   }
	}
	
	private Cookie deleteCookie(String cookieName) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setMaxAge(0); 
		return cookie;

	}

	private void displayCookieInfo(Cookie[] cookies, PrintWriter out) {
		Cookie cookie;
		if( cookies != null ){
		      out.println("<h2> Found Cookies Name and Value</h2>");
		      for (int i = 0; i < cookies.length; i++){
		         cookie = cookies[i];
		         
		         java.util.StringTokenizer tokens = new java.util.StringTokenizer(cookie.getValue( ), " ");
		         out.println("Cookie : " + cookie.getName( ));
		         out.print("<br>");
		         
		         if(tokens.countTokens() == 1){
		        	 out.print("default --> " + tokens.nextToken());
		        	 out.print("<br>");
		         }
		         
		           while(tokens.hasMoreTokens()) {
		             String alias = tokens.nextToken();		             
		             String id = null;
		             if(tokens.hasMoreTokens()){
		            	 id = tokens.nextToken();
		             }
		             out.println(alias + " -- > "+ id);
		             out.print("<br>");
		         } 
		         out.print("<br><br>");
		         
		      }
		  }else{
		      out.println("<h2>No cookies founds</h2>");
		  }
	}

}
