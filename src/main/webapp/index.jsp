<%@ page import="java.time.LocalDateTime"%>
<html>
<body>
<h2><a href="http://localhost:8080/actors">Actors</a></h2>
<h2><a href="http://localhost:8080/films">Films</a></h2>
<h2><a href="http://localhost:8080/directors">Directors</a></h2>
<%!
    String getDate(){
    return LocalDateTime.now().toString();
    }
%>
<h1><i>today <%=getDate()%></i></h1>
</body>
</html>
