<%@ page import="java.time.LocalDateTime"%>
<html>
<body>
<h2><a href="http://localhost:8080/actors">http://localhost:8080/actor</a></h2>
<h2><a href="http://localhost:8080/films">http://localhost:8080/film</a></h2>
<h2><a href="http://localhost:8080/directors">http://localhost:8080/film</a></h2>
<%!
    String getDate(){
    return LocalDateTime.now().toString();
    }
%>
<h1><i>today <%=getDate()%></i></h1>
</body>
</html>
