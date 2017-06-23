<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Session Attributes</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <style type="text/css">
        body {
            padding: 1em;
             width: 50%;
        }
		table {
		    border-collapse: collapse;
		}
		
		table, th, td {
		    border: 1px solid black;
		    text-align: left;
		}
		
		tr.highlight {
		background-color: #ffffcc;
    	color: black;
		}      
    </style>
</head>
<body>
    <div class="container">
        <h3>Spring Session Demo</h3>
        <p>This application demonstrates how to use a Hazlecast instance to back your session. 
		</p>
		<!--         
		Notice that there is no JSESSIONID cookie. 
        We are also able to customize the way of identifying what the requested session id is.
         -->

		<ul>
		<li><a href="${newSessionUrl}"> Add New Session </a></li>
		<li><a href="${logoutUrl}"> logout </a></li>
		
		</ul>
		Switch to Available sessions
		<table class="table">
		<tr><th>Alias</th><th>SessionId</th></tr>
		<c:forEach var="entry" items="${requestScope.sessionIdMap}">

		    <c:choose>
		      <c:when test="${pageContext.session.id == entry.value}">
		        <tr class="highlight">
		 			<td>
		 				<c:out value="${entry.key}"/>
		 			</td>
		  			<td><c:out value="${entry.value}"/></td>
		  		</tr>
		      </c:when>
		
		      <c:otherwise>
		      <tr>
		 		<td>
		 			<a href="session?_s=<c:out value="${entry.key}"/>"><c:out value="${entry.key}"/></a>
		 		</td>
		  		<td><c:out value="${entry.value}"/></td>      
		  	 </tr>
		      </c:otherwise>
		    </c:choose>
    
		</c:forEach>
		
		</table>
		
		

    		
        <h3>Enter Key/Value </h3>

        <form class="form-inline" action="${formUrl}" method="post">
           
            <label for="attributeValue">Key</label>
            <input id="attributeValue" type="text" name="attributeName"/>
            <label for="attributeValue">Value</label>
            <input id="attributeValue" type="text" name="attributeValue"/><br/>
            
             <input type="submit" value="Set Attribute"/>
        </form>

        <hr/>

        <table class="table table-striped">
            <thead>
            <tr>
                <th>Attribute Name</th>
                <th>Attribute Value</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${sessionScope}" var="attr">
                <tr>
                    <td><c:out value="${attr.key}"/></td>
                    <td><c:out value="${attr.value}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>