<%-- 
    Document   : index.jsp
    Created on : 08.07.2016, 10:45:40
    Author     : ksinn
    Домашняя страница приложения.   
--%>
<%@page import="Entety.Course"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<sql:query var="courses" dataSource="jdbc/DB">
    select * 
    from course  
    where open = 1 and status = 1
    order by start_date 
    limit 3
</sql:query>

<%! String pageTitle = "Main Page";%>
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
        <div class="fst-block">
            <body>
                <div class="row around">
                    <div class="offset-4 col col-4 text-center">
                        <a href="${pageContext.request.contextPath}"><div class="logo"></div></a>
                        <h5 class="upper primary"><img width="150" src="${pageContext.request.contextPath}/resourse/img/name.png"><%--=request.getServletContext().getInitParameter("SiteName")--%></h5>
                    </div>
                    <div class="col col-3 space-top text-right">
                        <c:choose>
                            <c:when test="${user != null}">
                                <a class="h4 main-login" data-component="offcanvas" data-direction="right" data-target="#sidenav" ><i class="fa fa-bars "></i></a>
                                </c:when>
                                <c:otherwise>
                                <img src="${pageContext.request.contextPath}/resourse/img/${main}login-ico.png"> 
                                <a class="${main}login h4" data-width="450px" data-component="modal" data-target="#login-modal">Log in</a>
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


                <c:if test="${user == null}">  
                    <div id="login-modal" class="modal-box hide">
                        <div class="modal">
                            <span class="close"></span>
                            <div class="modal-header">Sign in</div>
                            <div class="modal-body text-center">
                                <%@include file="/login_form.jspf" %>
                            </div> 
                        </div>
                    </div>
                </c:if>

                <div class="row around">
                    <div class="col col-6">			
                        <div class="valign-25">
                            <div class="h2 bold upper primary"><i class="fa fa-terminal" style="color:#000"></i><span id="blink">|</span> Database</div>
                            <div class="h3 bold subheading muted space-both">Intellectual. Laconic. Modern.</div>
                        </div>                    
                    </div>
                    <div class="col col-6">
                        <img src="${pageContext.request.contextPath}/resourse/img/database.png">
                    </div>
                </div>

        </div>
        <div class="green-blok">
            <div class="row around">
                <div class="col align-center space-top">
                    <h2 class="upper success">Online Courses</h2>
                </div>
            </div>

            <div class="row around">
                <c:forEach var="course" items="${courses.rows}">          
                    <div class="col col-3 text-center">		
                        <div>
                            <img src="${pageContext.request.contextPath}/${initParam.FileDir}/program/${course.program}.jpg" onerror="if (this.src != 'error.jpg') this.src = '${pageContext.request.contextPath}/resourse/img/default_program.png';">
                        </div>
                        <div class="course-info">
                            <p class="strong upper">
                                ${course.name}
                            </p>
                            <p class="strong upper">
                                ${course.start_date}
                            </p>
                            <div class="align-left">
                                <p>
                                    ${course.description}
                                </p>
                            </div>
                            <p>
                            <div class="row align-center">
                                <div class="col">
                                    <a class="button success outline small" href="${pageContext.request.contextPath}/course/render?id=${course.id}">Show more &rarr;</a>
                                </div>
                            </div>
                            </p>
                        </div>
                    </div>
                </c:forEach>               
            </div>
            <div class="row around">
                <div class="col col-11  space-top text-right"> 
                    <%--<a href="${pageContext.request.contextPath}/program/Courses" class="button round outline">See all &rarr;</a>--%>
                </div>
            </div>
        </div>
        <%@include file="/footer.jsp" %>

