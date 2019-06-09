<%-- 
    Document   : Userbar.jsp
    Created on : Aug 23, 2016, 4:41:08 PM
    Author     : javlonboy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/checkUser.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<sql:query var="studys" dataSource="jdbc/DB">
    select course, s.id, name, completed, (now()::date - start_date)+1 as day  
    from (study s join course c on s.course=c.id) 
    where s.users = ${user.id}</sql:query>  

<sql:query var="courses" dataSource="jdbc/DB">
    select course.*, (start_date + interval '1 day'*duration)::date as end_date  
    from course 
    where users = ${user.id}
</sql:query>
<%! String pageTitle = "Cabinet";%>
<%@include file="/header.jsp" %>
<div class="row">
    <div class="col offset-1 col-8">
        <nav class="breadcrumbs">
            <ul>
                <li><a class="link" href="${pageContext.request.contextPath}">Home</a></li>
                <li><span>Cabinet</span></li>
            </ul>
        </nav>
    </div>
</div>
<div class="row">
    <div class="col col-12">
        <div class="row userbar-1">
            <div class="col col-3 text-center">
                <img src="${pageContext.request.contextPath}/file/user/ico/${user.id}.png" onerror="if (this.src != 'error.jpg') this.src = '${pageContext.request.contextPath}/resourse/img/cat_${user.id%8+1}.png';" alt="" class="img-circle">
            </div>
            <div class="col col-3">
                <br>
                <h1>${user.name}<br>${user.surname}</h1>
            </div>
            <div class="col text-center"> 
                <br>
                <br>
            </div>
        </div> 
    </div>
</div>
<div class="row bg-blue">
    <div class="col col-12 borded-block">
        <nav class="tabs space-top" data-component="tabs">
            <ul>
                <li class="active"><a href="#studys">Study</a></li>
                <li><a href="#courses">Course</a></li>
                <li><a href="#tool">Tool</a></li>
            </ul>
        </nav>
        <div id="studys">
            <c:if test="${studys.rowCount==0}">
                <div class="row">
                    <div class="col col-7 contaner">
                        <p class="strong">No studed course yet</p>
                    </div>
                    <div class="col col-5 text-right">
                    </div>
                </div>
            </c:if>
            <c:forEach var="study" items="${studys.rows}">
                <div class="row space-top">
                    <div class="col col-7">
                        <p class="strong"><a href="${pageContext.request.contextPath}/course/render?id=${study.course}">${study.name}</a>  <c:if test="${study.completed == 1}">completed</c:if></p>
                    </div>
                </div>
                <sql:query var="tasks" dataSource="jdbc/DB">
                    SELECT now() between start_time and (start_time + interval '1 second'*time/1000) and not exists(select * from work where study=${study.id} and task = task.id and not completed=-1) as mayStart, 
                    *,
                    to_char(start_time, 'DD/MM HH24:MI') as start_date,
                    to_char(start_time + interval '1 second'*time/1000, 'DD/MM HH24:MI') as end_date
                    FROM task 
                    where course=(select id from course where id=${study.course}) and current_date between start_time::date and (start_time + interval '1 second'*time/1000)::date     
                    order by start_time
                </sql:query>
                <c:if test="${tasks.rowCount==0}">
                    <div class="row align-left">
                        <div class="col">
                            <div>
                                <p class="strong">
                                    No today tasks!
                                </p>
                            </div>
                        </div>
                    </div>
                    <hr>
                </c:if>
                <c:forEach var="task" items="${tasks.rows}">

                    <div class="row borded-block">  
                        <div class="col col-4">
                            <div>
                                <p class="strong">${task.name}</p>
                                <p class="italic small muted">
                                    ${task.total_count} tasks from ${task.start_date}
                                    till ${task.end_date}
                                </p>
                            </div>
                        </div>
                        <div class="col col-1 push-middle">
                            <c:choose>
                                <c:when test="${task.mayStart}">
                                    <a class="button outline primary round" href="${pageContext.request.contextPath}/task/work/${System.currentTimeMillis()}?s=${study.id}&t=${task.id}">
                                        Start
                                    </a>
                                </c:when>
                                <c:otherwise>                                    
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
                <hr class="space-both">
            </c:forEach> 
        </div>
        <div id="courses">
            <c:forEach var="course" items="${courses.rows}">
                <div class="row">
                    <div class="col col-9">
                        <p class="strong">
                            <a class="blue-link" href="${pageContext.request.contextPath}/course/render?id=${course.id}">${course.name}</a>
                            <c:choose>
                                <c:when test="${course.open==1}">
                                    <i title="All user can salf join to course " class="fa fa-eye font-blue" aria-hidden="true" ></i>
                                </c:when>
                                <c:otherwise>
                                    <i title="Only you can join student to course" class="fa fa-eye" aria-hidden="true"></i>
                                </c:otherwise>
                            </c:choose>
                        </p>    
                        <p>
                            <span class="italic small muted">
                                from ${course.start_date} to ${course.end_date} 
                            </span>
                        </p>
                    </div>
                    <div class="col col-2 text-right">
                        <a class="blue-edit" href="${pageContext.request.contextPath}/course/edit?id=${course.id}">
                            <i class="fa fa-cog"></i> Edit
                        </a>
                    </div>

                </div>
                <hr>
            </c:forEach> 
        </div>
        <div id="tool">
            For creating course you need do next steps
            <ul>
                <li>Create new program</li>
                <li>Create same material ther;</li>
                <li>Create  test and Exem;</li>
                <li>Publish;</li>
            </ul>
            <a href="${pageContext.request.contextPath}/course/add"><button class="button round outline">Create new course</button></a>
            <a href="${pageContext.request.contextPath}/task/create/${System.currentTimeMillis()}"><button class="button round outline">Create task</button></a>
        </div>
    </div>
</div> 
<%@include file="/footer.jsp" %>

