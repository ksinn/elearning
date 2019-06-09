<%-- 
    Document   : Program.jsp
    Created on : Aug 25, 2016, 1:52:15 PM
    Author     : javlonboy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@include file="/checkUser.jsp" %>

<sql:query var="courses" dataSource="jdbc/DB">
    select *, 
    (select concat(name, ' ', surname) from users where id=users) as user_name,
    open=1 and not users=${user.id} and not exists(select * from study where users=${user.id} and course = course.id) as canJoin
    from course
    where course.id=?
    <sql:param value="${Integer.parseInt(param.id)}"/>
</sql:query>

<c:forEach items="${courses.rows}" var="course">

    <c:if test="${courses.rowCount != 1}">
        <c:redirect url="/error" />
    </c:if>
    <c:if test="${course.users == user.id}">
        <c:set var="tuter" value="true"/>
    </c:if>

    <sql:query var="tasks" dataSource="jdbc/DB">
        SELECT *, 
        to_char(start_time, 'DD/MM HH24:MI') as start_date,
        to_char(start_time + interval '1 second'*time/1000, 'DD/MM HH24:MI') as end_date
        FROM task 
        where course = ${course.id}
        order by start_time, time
    </sql:query>

    <sql:query var="students" dataSource="jdbc/DB">
        select * from users where exists (select * from study where users = users.id and course=${course.id} and completed=0) order by surname
    </sql:query>


    <%! String pageTitle = "Course";%>
    <%@include file="/header.jsp" %>
    <%@include file="/bar.jsp" %>
    <div class="row">
        <div class="col offset-1 col-8">
            <nav class="breadcrumbs">
                <ul>
                    <li><a class="link" href="${pageContext.request.contextPath}">Home</a></li>
                    <li><a class="link" href="${pageContext.request.contextPath}/courses">Courses</a></li>
                    <li><span>${course.name}</span></li>
                </ul>
            </nav>
        </div>
    </div>
    <div class="green-blok">
            <div class="row">
                <div class="col col-9 contaner">
                    <h3>${course.name}</h3>
                </div>
                <div class="col col-2 contaner text-right push-middle">
                    <c:if test="${tuter}">
                        <c:choose>
                            <c:when test="${course.status==0}">
                                <a class="red-edit" onclick="start()">
                                    <i class="fa fa-play font-con" aria-hidden="true"> Start</i>
                                </a>
                            </c:when>
                            <c:when test="${course.status==1}">
                                <a class="red-edit" onclick="stop()">
                                    <i class="fa fa-pause font-con" aria-hidden="true"> Stop</i>
                                </a>
                            </c:when>
                        </c:choose>
                        <a class="red-edit"  href="${pageContext.request.contextPath}/course/edit?id=${course.id}">
                            <i class="fa fa-cog" aria-hidden="true"> Edit</i>
                        </a>
                    </c:if>
                    <c:if test="${course.canJoin}">
                        <a onclick="join()" class="red-edit">
                            <i class="fa fa-plus" aria-hidden="true"> Join</i>
                        </a>
                    </c:if>
                </div>
            </div>
            <div class="row green-bg">
                <div class="col col-3">
                    <img src="${pageContext.request.contextPath}/${initParam.FileDir}/program/${course.program}.jpg" 
                         onerror="if (this.src != 'error.jpg') this.src = '${pageContext.request.contextPath}/resourse/img/default_program.png';">
                </div>
                <div class="col col-8 text-left">
                    <p class="space-top">
                        Start date: ${course.start_date} <br> 
                        Duration: ${course.duration} days<br> 
                        Teacher:  ${course.user_name}<br>
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col space-top contaner">
                    <p>
                        ${course.description}
                    </p>
                </div>
            </div>
            <div class="row">
                <div class="col col-12 borded-block">
                    <nav class="tabs" data-component="tabs" data-equals="true">
                        <ul>
                            <li class="active"><a href="#tasks">TASK</a></li>
                            <li><a href="#students">STUDENTS</a></li>
                            <li><a href="#marks">RESULT</a></li>
                        </ul>
                    </nav>
                    <div id="tasks">
                        <div class="row">
                            <div class="col offset-10 col-2 text-right">
                                <p>
                                    <c:if test="${tuter}"> 
                                        <a class="green-edit" href="${pageContext.request.contextPath}/task/add?course=${course.id}">
                                            <i class="fa fa-plus font-green" aria-hidden="true"> Add task</i>
                                        </a>
                                    </c:if>
                                </p>
                            </div>
                        </div>
                        <c:if test="${tasks.rowCount eq 0}">
                            <div class="row">
                                <div class="offset-1 col col-9">
                                    <div>
                                        <p class="bold">
                                            No tasks
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:forEach items="${tasks.rows}" var="task">  
                            <hr>
                            <div class="row">  
                                <div class="col col-1 text-center align-middle">
                                    <span></span><br>
                                    <span></span><br>
                                </div>
                                <div class="col col-9">
                                    <div>
                                        <p class="bold">
                                            ${task.name}
                                        </p>
                                        <p>
                                            ${task.total_count} tasks from ${task.start_date}
                                            till ${task.end_date}
                                        </p>
                                    </div>
                                </div>
                                <div class="col col-2 text-right">
                                    <c:if test="${tuter}"> 
                                        <a class="green-edit" href="${pageContext.request.contextPath}/task/edit?id=${task.id}">
                                            <i class="fa fa-cog green-edit" aria-hidden="true"> Edit</i>
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>                  
                    </div>
                    <div id="students">
                        <div class="row">
                            <div class="col offset-10 col-2 text-right">
                                <p>
                                    <c:if test="${tuter}"> 
                                        <a class="green-edit" href="${pageContext.request.contextPath}/task/add/list">
                                            <i class="fa fa-plus font-green" aria-hidden="true"> Add student</i>
                                        </a>
                                    </c:if>
                                </p>
                            </div>
                        </div>
                        <c:if test="${students.rowCount eq 0}">
                            <div class="row">
                                <div class="offset-1 col col-9">
                                    <div>
                                        <p class="bold">
                                            No students
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:forEach items="${students.rows}" var="student">
                            <hr> 
                            <div class="row">  
                                <div class="col col-1 text-center align-middle">
                                    <br><br>
                                </div>
                                <div class="col col-9">
                                    <div>
                                        ${student.surname} ${student.name}
                                    </div>
                                </div>
                                <div class="col col-2 text-right">
                                </div>
                            </div>
                        </c:forEach>                        
                    </div>
                    <div id="marks">
                        <nav class="tabs space-top" data-component="tabs">
                            <ul>
                                <c:forEach items="${tasks.rows}" var="task">
                                    <li>
                                        <a href="#task${task.id}">${task.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                        <c:forEach items="${tasks.rows}" var="task">
                            <sql:query var="res" dataSource="jdbc/DB">
                                select mail ,users, name, surname, (select max(completed) from work where task = ${task.id} and study = study.id) as result 
                                from study join users on users.id = users 
                                where course = ${course.id}  
                                order by result                    
                            </sql:query>
                            <div id="task${task.id}">
                                <table calss="bordered striped">
                                    <tr>
                                        <th>mail</th>
                                        <th>Name</th>
                                        <th>Result</th>
                                    </tr>
                                    <c:forEach items="${res.rows}" var="res">
                                        <tr>
                                            <td>${res.mail}</td>
                                            <td>${res.surname} ${res.name}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${res.result == -1}">
                                                        not completed
                                                    </c:when>
                                                    <c:when test="${res.result == null}">
                                                        no attempt
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${res.result}
                                                    </c:otherwise>
                                                </c:choose>                                        
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
    </div>                   

    <div id="my-modal" class="modal-box hide">
        <div class="modal">
            <span class="close"></span>
            <div class="modal-header"></div>
            <div id="modalBody" class="modal-body text-center">

            </div>
        </div>
    </div>
    <script>
        function join() {

            $.ajax("${pageContext.request.contextPath}/course/join?id=${course.id}", {
                        method: 'get',
                        success: function (result) {
                            document.getElementById("modalBody").innerHTML = result;
                            $.modalwindow({target: '#my-modal', width: '300px', header: 'Response'});
                        }
                    });

                }



                function start() {

                    $.ajax("${pageContext.request.contextPath}/course/start?id=${course.id}", {
                                method: 'post',
                                success: function (result) {
                                    document.getElementById("modalBody").innerHTML = result;
                                    $.modalwindow({target: '#my-modal', width: '300px', header: 'Response'});
                                }
                            });

                        }
                        ;

                        function stop() {

                            $.ajax("${pageContext.request.contextPath}/course/stop?id=${course.id}", {
                                        method: 'post',
                                        success: function (result) {
                                            document.getElementById("modalBody").innerHTML = result;
                                            $.modalwindow({target: '#my-modal', width: '300px', header: 'Response'});
                                        }
                                    });

                                }
                                ;

                                function pause() {

                                    $.ajax("${pageContext.request.contextPath}/course/pause?id=${course.id}", {
                                                method: 'post',
                                                success: function (result) {
                                                    document.getElementById("modalBody").innerHTML = result;
                                                    $.modalwindow({target: '#my-modal', width: '300px', header: 'Response'});
                                                }
                                            });

                                        }
                                        ;
    </script>
    <%@include file="/footer.jsp" %>
</c:forEach>