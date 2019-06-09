<%-- 
    Document   : header
    Created on : 20.08.2016, 14:44:22
    Author     : ksinn
    Заголовок страниц. 
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    String main = (request.getServletContext().getContextPath() + "/").equals(request.getRequestURI()) ? "main-" : "";

    String client_id = "1006393654499-p8mr2fj0fkg43ifvl68eo2k18o6u2qgm.apps.googleusercontent.com";
    String client_secret = "lU9JFY65Oy7Oas33THOn_CUN";
    String redirect_uri = "/user/signIn";
    String response_type = "code";
    String scope = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    String auth_url = "https://accounts.google.com/o/oauth2/auth?"
            + "client_id=" + client_id
            + "&redirect_uri=" + redirect_uri
            + "&response_type=" + response_type
            + "&scope=" + scope;

    pageContext.setAttribute("auth_url", auth_url);
    pageContext.setAttribute("main", main);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>${pageTitle} :: ${initParam.SiteName} &mdash; 2016 </title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/resourse/img/favicon.png" rel="shortcut icon" type="image/x-icon">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resourse/css/normalize.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resourse/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resourse/css/kube.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resourse/css/master.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resourse/css/newstyle.css">
        <script src="${pageContext.request.contextPath}/resourse/js/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resourse/js/kube.min.js"></script>
    </head>
    <body>
        <div class="row around">
            <div class="offset-4 col col-4 text-center">
                <a href="${pageContext.request.contextPath}"><div class="logo"></div></a>
                <h5 class="upper primary"><img width="150" src="${pageContext.request.contextPath}/resourse/img/name.png"><%--=request.getServletContext().getInitParameter("SiteName")--%></h5>
            </div>
            <div class="col col-3 space-top text-right">
                <c:choose>
                    <c:when test="${user != null}">
                        <a class="h4 login" data-component="offcanvas" data-direction="right" data-target="#sidenav" ><i class="fa fa-bars "></i></a>
                        </c:when>
                        <c:otherwise>
                        <img src="${pageContext.request.contextPath}/resourse/img/${main}login-ico.png"> 
                        <a class="login h4" data-width="450px" data-component="modal" data-target="#login-modal">Log in</a>
                    </c:otherwise>
                </c:choose>
            </div>            
        </div>

        <c:if test="${user != null}">  
            <div id="sidenav" class="hide">
                <a href="#" class="close"></a>                        
                <div class="text-center">
                    <img src="${pageContext.request.contextPath}/file/user/ico/${user.id}.png" onerror="if (this.src != 'error.jpg') this.src = '${pageContext.request.contextPath}/resourse/img/cat_${user.id%8+1}.png';" class="usr-img">
                    <p>${user.name}</p>
                </div>
                <nav>
                    <ul>
                        <li>
                            <a href="${pageContext.request.contextPath}/user/cabinet">CABINET</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/user/logOut">LOG OUT <i class="fa fa-sign-out"></i></a>
                        </li>
                    </ul>
                </nav>
            </div>
        </c:if>


        <c:if test="${user != null}">  
            <div id="login-modal" class="modal-box hide">
                <div class="modal">
                    <span class="close"></span>
                    <div class="modal-header">Sign in</div>
                    <div class="modal-body">
                        <%@include file="/login_form.jspf" %>
                    </div> 
                </div>
            </div>
        </c:if>

