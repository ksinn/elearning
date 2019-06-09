<%-- 
    Document   : Error
    Created on : 24.07.2016, 11:37:12
    Author     : ksinnаница для вывода сообщений.
    Стр
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%! String pageTitle = "Woops!";%>
<%@include file="/header.jsp" %>
<%@include file="/bar.jsp" %>
<div class="row centered bg-blue">
    <div class="col col-3 text-center">
        <div class="p-error">
            <h3>
                ${messageService.popWebMessage()}
            </h3>
        </div>
    </div>
</div>            
<%@include file="/footer.jsp" %>
</body>
</html>
