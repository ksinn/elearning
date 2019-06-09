<%-- 
    Document   : Error
    Created on : 24.07.2016, 11:37:12
    Author     : ksinn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String pageTitle = "Set Up";%>
<%@include file="/header.jsp" %>
<%@include file="/bar.jsp" %>
<div class="row centered bg-blue">
    <div class="col col-3 text-center">
        <img src="${pageContext.request.contextPath}/resourse/img/smile-ghost.png">
        <div class="p-error">
            <h3>Warning! You can not take QR-code agane!</h3>

            <form target="_blank" method="post" action="signUp" id="key">
                <p>                         
                    <button class="button primary width-100 big">get QR-cod</button>
                </p>
            </form>
            <button onclick="window.location = '${pageContext.request.contextPath}/user/signIn';">Sign In</button>        
        </div>
    </div>
</div>                  
<script>
    $('#key').submit(function (e) {
        this.submit();
        setTimeout(function () {
            window.location.href = '${pageContext.request.contextPath}/user/signIn';
        }, 100);
    });
</script>                   
<%@include file="/footer.jsp" %>
