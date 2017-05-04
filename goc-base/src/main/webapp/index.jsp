<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="es.uji.apps.goc.auth.LanguageConfig" %>
<%@ page import="es.uji.apps.goc.auth.PersonalizationConfig" %>
<%@ page import="es.uji.commons.sso.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de órganos colegiados</title>
    <meta charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="//static.uji.es/js/extjs/ext-6.2.1/build/classic/theme-triton/resources/theme-triton-all.css">
    <link rel="stylesheet" type="text/css" href="//static.uji.es/js/extjs/ext-6.2.1/build/packages/font-awesome/resources/font-awesome-all.css">
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <%
        User user = (User) session.getAttribute(User.SESSION_USER);

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        LanguageConfig languageConfig = context.getBean(LanguageConfig.class);
        PersonalizationConfig personalizationConfig = context.getBean(PersonalizationConfig.class);
    %>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <script type="text/javascript" src="//static.uji.es/js/extjs/ext-6.2.1/build/ext-all.js"></script>

    <title>Portal</title>
    <script type="text/javascript">
        var login = '<%= user.getName() %>';
        var perId = <%= user.getId() %>;
        var mainLanguage = '<%= languageConfig.mainLanguage %>';
        var mainLanguageDescription = '<%= languageConfig.mainLanguageDescription %>';
        var alternativeLanguage = '<%= languageConfig.alternativeLanguage %>';
        var alternativeLanguageDescription = '<%= languageConfig.alternativeLanguageDescription %>';
        var logo = '<%= personalizationConfig.logo %>';
        var disconnectLink = '<%= personalizationConfig.disconnectLink %>';

        function getMultiLangLabel(value, lang) {
            if (isMultilanguageApplication() && lang === mainLanguage)
                return value + ' (' + mainLanguageDescription + ')';

            if (isMultilanguageApplication() && lang === alternativeLanguage)
                return value + ' (' + alternativeLanguageDescription + ')';

            return value;
        }

        function isMultilanguageApplication() {
            return (mainLanguage && mainLanguageDescription && alternativeLanguage && alternativeLanguageDescription);
        }
    </script>

    <script type="text/javascript">

        function getValidLang(lang) {
            if (!lang || (lang.toLowerCase() !== mainLanguage && lang.toLowerCase() !== alternativeLanguage)) {
                return mainLanguage;
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
