<%@page import="es.uji.commons.sso.User" %>
<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de organos colegiados</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="http://static.uji.es/js/extjs/ext-6.0.3.64/build/classic/theme-triton/resources/theme-triton-all.css">
    <link rel="stylesheet" type="text/css" href="http://static.uji.es/js/extjs/ext-6.0.3.64/build/packages/font-awesome/resources/font-awesome-all.css">
    <link rel="stylesheet" type="text/css" href="css/style.css">

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <script type="text/javascript" src="http://static.uji.es/js/extjs/ext-6.0.3.64/build/ext-all-debug.js"></script>

    <title>Portal</title>

    <%
        User user = (User) session.getAttribute(User.SESSION_USER);
    %>

    <script type="text/javascript">
        var login = '<%=user.getName()%>';
        var perId = <%=user.getId()%>;
    </script>

    <script type="text/javascript">

        function getValidLang(lang) {
            if (!lang || [ 'ca', 'es' ].indexOf(lang.toLowerCase()) === -1) {
                return 'ca';
            }

            return lang;
        }

        var requestLang = '<%= request.getParameter("lang")%>';
        var appLang = getValidLang(requestLang);
    </script>

    <script type="text/javascript" src="app/Application.js"></script>
</head>
<body>
</body>
</html>