<%-- 
    Document   : addUser
    Created on : 19.12.2017, 12:04:53
    Author     : ksinn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<%if (!request.getMethod().equals("POST")) {%>
<h1>Add user</h1>
<form class="form" action="" method="post">
    <div class="form-item">
        <label>Name</label>
        <input type="text" name="name" required >
    </div>
    <div class="form-item">
        <label>Surame</label>
        <input type="text" name="surname" required >
    </div>
    <div class="form-item">
        <label>g-mame</label>
        <input type="text" name="mail" required >
    </div>
    <div class="form-item">
        <button class="button primary width-100 big">Sign In</button>
    </div>
</form>
<%} else {%>
<c:choose>
    <c:when test="${param.id==null}">
        <sql:update var="res" dataSource="jdbc/DB">
            insert into users(name, surname, mail) values(?,?,?)
            <sql:param value="${param.name}"/>
            <sql:param value="${param.surname}"/>
            <sql:param value="${param.mail}"/>
        </sql:update>
    </c:when>
    <c:otherwise>
        <sql:update var="res" dataSource="jdbc/DB">
            update users set name=?, surname=?, mail=? where id = ?
            <sql:param value="${param.name}"/>
            <sql:param value="${param.surname}"/>
            <sql:param value="${param.mail}"/>
            <sql:param value="${Integer.parseInt(param.id)}"/>
        </sql:update>
    </c:otherwise>
</c:choose>
${res}
<%}%>

