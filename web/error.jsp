<%-- 
    Document   : Error
    Created on : 24.07.2016, 11:37:12
    Author     : ksinn
    Страница отображаемая при ошибках.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%! String pageTitle = "Error";%>
<%@include file="/header.jsp" %>
<div class="row centered bg-blue">
    <div class="col col-3 text-center">
        <img src="${pageContext.request.contextPath}/resourse/img/ghost.png" alt="error">
        <div class="p-error">
            <h3>WHOOPS!</h3>
            <p>
                ${pageContext.exception}
            </p>
        </div>
    </div>
</div>
<%@include file="/footer.jsp" %>
