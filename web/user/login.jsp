<%-- 
    Document   : Error
    Created on : 24.07.2016, 11:37:12
    Author     : ksinn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String pageTitle = "Sign In";%>
<%@include file="/header.jsp" %>
<%@include file="/bar.jsp" %>
<div class="row centered bg-blue">
    <div class="col col-3 text-center">
        <img src="${pageContext.request.contextPath}/resourse/img/ghost.png" alt="error">
        <div class="p-error">
            <h3>${err}</h3>
            <form class="form" action="signIn" method="post">
                <div class="form-item">
                    <label>Code</label>
                    <input type="text" name="code" required >
                </div>

                <div class="form-item">
                    <button class="button primary width-100 big">Sign In</button>
                </div>

            </form>
        </div>
    </div>
</div>
<%@include file="/footer.jsp" %>

<script>

    function show()
    {
        $.ajax({
            url: "${pageContext.request.contextPath}/user/SendSMS",
            cache: false,
            error: function () {
                $("#sms-message").html("error");
            },
            success: function (data) {
                $("#sms-message").html(data);

            }
        });
    }

</script> 
</body>
</html>
