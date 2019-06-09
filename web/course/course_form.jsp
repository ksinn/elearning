<%-- 
    Document   : CreateProgram
    Created on : 08.07.2016, 15:12:56
    Author     : ksinn
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/checkUser.jsp" %>
<%! String pageTitle = "Program";%>
<%@include file="/header.jsp"%>
<div class="row blue-blok">
    <div class="col offset-2 col-8">
        <nav class="breadcrumbs">
            <ul>
                <li><a class="link" href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a class="link" href="${pageContext.request.contextPath}/user/cabinet">Cabinet</a></li>
                    <c:choose>
                        <c:when test="${not empty param.id}">
                        <li><a class="link" href="${pageContext.request.contextPath}/course/render?id=${course.id}">${course.name}</a></li>
                        <li><span>Edit</span></li>
                        </c:when>
                        <c:otherwise>
                        <li><span>Add</span></li>
                        </c:otherwise>
                    </c:choose>
            </ul>
        </nav>
        <div class="row space-top">
            <div class="col col-7">
                <c:choose>
                    <c:when test="${not empty param.id}">
                        <h3>Edit course <span class="italic">${course.name}</span></h3>
                        </c:when>
                        <c:otherwise>
                        <h3>Add course</h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col col-5 text-right">

            </div>
        </div>

        <hr class="space-both">

        <div class="row around">
            <div class="col col-8">
                <form id="form" class="form" action="" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${course.id}">
                    <div class="form-item">
                        <label>Name:</label>
                        <input class="width-100" required type="text" name="name" value="${course.name}">
                    </div>                    

                    <div class="form-item">
                        <label>Description:</label>
                        <textarea  id="input" rows="6" required  name="discription">${course.description}</textarea>
                    </div> 

                    <div class="form-item">
                        <label title="сколько дне будет длиться курс">Duration(in days):</label>                        
                        <input class="width-100" required type="number" name="duration" min="1" value="${course.duration}">
                    </div>

                    <div class="form-item">
                        <label title="дата начала курса">Start date:</label>                        
                        <input class="width-100" onchange='{
                                            $("#date").val((new Date(this.value)).getTime());
                                        }' required type="date" value="${course.startDate}">
                        <input type="hidden" id="date" name="start_date" value="${course.startDate.getTime()}">
                    </div>

                    <div class="form-item">
                        <label title="Курс доступин для самостоятельной записи">
                            <input type="checkbox" name="opened" <c:if test="${course.isOpened()}"> checked="true" </c:if>> Public
                            </label>
                    </div>
                    <%--                <div class="form-item">
                                            <label>Picture:</label>
                                            <input class="width-100" type="file" name="picture" >
                                        </div>  
                    --%>                    
                    <div class="form-item text-center">        
                        <button class="button primary w25 big">Complete</button>
                    </div>  
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/resourse/js/jquery.validate.min.js"></script> 
<script>
                                    $(document).ready(function () {

                                        $("#form").validate({
                                            rules: {
                                                name: {
                                                    required: true,
                                                    maxlength: 128,
                                                },
                                                discription: {
                                                    required: true,
                                                    maxlength: 4096,
                                                },
                                                duration: {
                                                    required: true,
                                                    number: true,
                                                    min: 1,
                                                    max: 183
                                                }
                                            },
                                        });


                                    }); //end of ready
</script>
<script src="<%=request.getServletContext().getContextPath()%>/resourse/js/tinymce/tinymce.min.js"></script>
<script>

                                    tinymce.init({
                                        selector: '#input',
                                        theme: 'modern',
                                        plugins: [
                                            'advlist autolink link lists charmap hr',
                                            'searchreplace wordcount visualchars fullscreen insertdatetime',
                                            'template textcolor'
                                        ],
                                        toolbar: 'forecolor backcolor | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage '

                                    });


</script>
<%@include file="/footer.jsp" %>