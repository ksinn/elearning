<%-- 
    Document   : passwords
    Created on : 08.12.2017, 22:30:52
    Author     : ksinn
--%>

<%@page import="java.util.Date"%>
<%@page import="Auth.GoogleAuthenticator"%>
<%@page import="Entety.User"%>
<%@page import="Service.UserService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/header.jsp" %>
<%@include file="/bar.jsp" %>
<%    UserService userService = new UserService();
    long d = GoogleAuthenticator.liveTime;
    long s = d * ((System.currentTimeMillis() / 1000L) / d);
    long e = (s + d) * 1000L;
    request.setAttribute("userServ", userService);
    request.setAttribute("generateTime", new Date(e));
%>

<div class="row centered bg-blue">
    <div class="col col-8">
        <h4>User's current passwords</h4>
        <h5>next passwords will generate in ${generateTime}</h5>
        <table class='table'>
            <c:forEach items="${userServ.users}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.surname} ${user.name}</td>
                    <td>${user.mail}</td>
                    <c:set var="code" value="${userServ.getCurrentCode(user)}"/>
                    <td>${code}</td>
                    <td> 
                        <c:if test="${code==-1}">
                            <button onclick="genKey(${user.id})">gen new key</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
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
    function genKey(id) {

        $.ajax("generateKey?id=" + id, {
            method: 'get',
            success: function (result) {
                console.log(123);
                document.getElementById("modalBody").innerHTML = result;
                $.modalwindow({target: '#my-modal', width: '300px', header: 'Response'});
            }
        });

    }
</script>
<%@include file="/footer.jsp" %>


