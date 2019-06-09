<%-- 
    Document   : checkUser
    Created on : 23.10.2017, 20:19:59
    Author     : ksinn
    Проверка акторизированности клиента. 
    Инклюдить в те страницы, каторые доступны только авторезированным клиенатм.
--%>

<%@page import="Entety.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user/signIn");
        return;
    };

%>
