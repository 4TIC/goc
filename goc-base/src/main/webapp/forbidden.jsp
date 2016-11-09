<%@ page import="org.springframework.security.web.WebAttributes" %>
<!DOCTYPE html>
<html>
  <body>
    <h1>Privilegios insuficientes</h1>

    <%= ((Throwable) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage() %>
  </body>
</html>
