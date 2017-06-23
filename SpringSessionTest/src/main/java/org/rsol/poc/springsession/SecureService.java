package org.rsol.poc.springsession;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Servlet implementation class SecureService
 */
@WebServlet("/SecureService")
public class SecureService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String baseUrl = "http://wpwdwrff.opr.statefarm.org:9080/securitystub-services/";

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SecureService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ssoToken = request.getParameter("ssoToken");
		String resource = request.getParameter("resource");
		String policy_agent_name = "jpetstore-access-policy";
		
		if(ssoToken == null || resource == null){
			displayForm(response);
			return;
		}
		
    	ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
    	HttpClient httpClient  = new DefaultHttpClient(connectionManager);
		httpClient.getParams().setParameter(
		        ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);    	
    	connectionManager.setMaxTotal(20);
    	connectionManager.setDefaultMaxPerRoute(20);
    	
		boolean isTokenValid = isTokenValid(httpClient, ssoToken);
		boolean isAuthorized = isAuthorized( httpClient, ssoToken, resource, policy_agent_name);
		String userId = getUserIdFromToken(httpClient,ssoToken);
		

		PrintWriter out = response.getWriter();
		StringWriter str = new StringWriter();
		str.append("<Html>");
		str.append("<H3>").append("OpenAm Security Test Service").append("<H3>");
		str.append("<H4>").append("Policy Agent &#10152; ").append(policy_agent_name).append("<H4>");
		str.append("<H4>").append("SSO Token &#10152; ").append(ssoToken).append("<H4>");
		str.append("<H4>").append("Resource &#10152; ").append(resource).append("<H4>");
		str.append("<H4>").append("isTokenValid &#10152; ").append(new Boolean(isTokenValid).toString()).append("<H4>");
		str.append("<H4>").append("userId &#10152; ").append(userId).append("<H4>");
		str.append("<H4>").append("isAuthorized &#10152; ").append(new Boolean(isAuthorized).toString()).append("<H4>");
		str.append("</Html>");
		out.println(str.toString());
	}
	
	private void displayForm(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		StringWriter str = new StringWriter();
		
		str.append("<Html>");
		str.append("<H4>").append("OpenAm Security Test Service").append("<H4>");
		
		str.append("<form action=\"SecureService\">");
		str.append("SSO Token: <input type=\"text\" name=\"ssoToken\" value=\"token \">");
		str.append("<br>");
		str.append("resource: <input type=\"text\" name=\"resource\" value=\"path to secure\">");
		str.append("<br>");
		str.append("<input type=\"submit\" value=\"Submit\">");
		str.append("</form>");
		str.append("</Html>");
		
		out.println(str.toString());
	}
	
    public boolean isAuthorized(HttpClient httpClient, String ssoToken, String resource,String policy_agent_name) 
    {    
/*    	logger.log(Level.INFO, "*************entering client isAuthorized method *****************");
		logger.log(Level.INFO, "Input for authenticate ssoToken: " + ssoToken);
		logger.log(Level.INFO, "Input for authenticate resource: " + resource);
		logger.log(Level.INFO, "HTTP Invocation url authorizeUrl: " + authorizeUrl);	*/
    	
    	//http://wpwdwrff.opr.statefarm.org:9080/securitystub-services/identity/authorize
    	
    	String authorizeUrl = baseUrl + "identity/authorize";
		  boolean entitled = false;
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("uri", resource));
			qparams.add(new BasicNameValuePair("subjectid", ssoToken));
			StringBuffer uri=new StringBuffer(authorizeUrl);
			uri.append("?").append(URLEncodedUtils.format(qparams, "UTF-8"));	
          Header header = new BasicHeader("policy_agent",policy_agent_name);
       
	        try {
	        	HttpGet httpGet=new HttpGet(uri.toString());
	        	httpGet.setHeader(header);
				HttpResponse response=httpClient.execute(httpGet);
				if(response.getStatusLine()!=null&&response.getStatusLine().getStatusCode()==200){
					HttpEntity entity = response.getEntity();
					String responseString=EntityUtils.toString(entity);
					if("boolean=true".equals(responseString.trim()))
						entitled=true;
				}
				else
				{
/*					logger.log(Level.SEVERE, "not authorized");
*/				}

        } catch (Throwable e) {
        	e.printStackTrace();
        }
		
/*		logger.log(Level.INFO, "*************exiting client isAuthorized method *****************");
*/        return entitled;
    }

    
	public boolean isTokenValid(HttpClient httpClient, String ssoToken)
	{
		
		boolean result =false;
		if(ssoToken==null)
			return result;
		
		String isTokenValidUrl=baseUrl+"identity/isTokenValid";
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("tokenid", ssoToken));
		StringBuffer uri=new StringBuffer(isTokenValidUrl);
		uri.append("?").append(URLEncodedUtils.format(qparams, "UTF-8"));
		HttpGet httpGet=new HttpGet(uri.toString());
		try 
		{
			HttpResponse response=httpClient.execute(httpGet);
			if(response.getStatusLine()!=null&&response.getStatusLine().getStatusCode()==200)
			{
				HttpEntity entity = response.getEntity();
				String responseString=EntityUtils.toString(entity);
				if("boolean=true".equals(responseString.trim()))
					result=true;
			}
			else
			{
				System.out.println("invalid token");
			}
		} catch (Throwable e) 
		{
				e.printStackTrace();
		}
			
		return result;
		
	}
	
    public String getUserIdFromToken(HttpClient httpClient, String ssoToken){
       	String uid=null;
    		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("subjectid", ssoToken));
    		qparams.add(new BasicNameValuePair("attributes", "uid"));
    		
    		String attributeUrl = baseUrl + "identity/attributes";
    		StringBuffer uri=new StringBuffer(attributeUrl);
    		uri.append("?").append(URLEncodedUtils.format(qparams, "UTF-8"));
    		HttpGet httpGet=new HttpGet(uri.toString());
    		try {
    			HttpResponse response=httpClient.execute(httpGet);	
    			if(response.getStatusLine()!=null&&response.getStatusLine().getStatusCode()==200){
    				InputStream entityStream=response.getEntity().getContent();
    				try{
			
    					  java.io.BufferedReader reader = new java.io.BufferedReader(
    			                    new java.io.InputStreamReader(entityStream));            
    			            String line = null;//boolean=true
    			            String  key = "";
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf("userdetails.attribute.name");
                if (index != -1) {
                    key = line.substring("userdetails.attribute.name".length() + 1);
                }
                index = line.indexOf("userdetails.attribute.value");
                if ("uid".equals(key)&& index != -1) {
                    uid = line.substring("userdetails.attribute.value".length() + 1);
                    break;
                }
            }  
    				}finally{
            	entityStream.close();
            }
    				}
        } catch (Throwable e) {
        	e.printStackTrace();
        }
     		
        return uid;
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
