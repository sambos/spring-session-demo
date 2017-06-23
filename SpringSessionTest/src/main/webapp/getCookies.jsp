<html>
<head>
<title>Reading Cookies</title>
</head>
<body>
<center>
<h1>Reading Cookies</h1>
</center>
<%
   Cookie cookie = null;
   Cookie[] cookies = null;
   // Get an array of Cookies associated with this domain
   cookies = request.getCookies();
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
             String id = tokens.nextToken();
             out.println(alias + " -- > "+ id);
             out.print("<br>");
         } 
         out.print("<br><br>");
         
      }
  }else{
      out.println("<h2>No cookies founds</h2>");
  }
%>
</body>
</html>