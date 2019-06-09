<%-- 
    Document   : about_as
    Created on : 13.09.2016, 11:08:30
    Author     : ksinn
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String pageTitle = "About as";%>
<%@include file="header.jsp"%>
<%@include file="bar.jsp"%>
<div class="user-block">
    <div class="row around">
        <div class="col">
            <h3 class="error upper text-center">People</h3>
        </div>
    </div>	
    <div class="row  left">
        <div class="col text-center">		
            <div class="item">
                <img src="${pageContext.request.contextPath}/img/ksinn.png">
            </div>
            <div class="item">
                <b>Andreeva Kseniya</b>
                <p>
                    gre gverbg vtrbtr vberber dvdfbdf vrever erberbe rbeb
                </p>
            </div>
        </div>
        <div class="col text-center">		
            <div class="item">
                <img src="${pageContext.request.contextPath}/img/nata.png">
            </div>
            <div class="item">
                <b>Kim Nata</b>
                <p>
                    gre gverbg vtrbtr vberber dvdfbdf vrever erberbe rbeb
                </p>
            </div>
        </div>
        <div class="col text-center">		
            <div class="item">
                <img src="${pageContext.request.contextPath}/img/nata.png">
            </div>
            <div class="item">
                <b>Obidov Javlon</b>
                <p>
                    gre gverbg vtrbtr vberber dvdfbdf vrever erberbe rbeb
                </p>

            </div>
        </div>

    </div>        
</div>
<%@include file="footer.jsp" %>
