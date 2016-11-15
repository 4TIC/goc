<%@ page import="org.springframework.security.web.WebAttributes" %>
<!DOCTYPE html>
<html>
  <body>
    <h1>Privilegios insuficientes</h1>

    <%
      String errorMessage = "";

      if (request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
          errorMessage = ((Throwable) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage();
      }
    %>

    <%= errorMessage %>
  </body>
</html>
