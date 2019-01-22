<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <title>INSPINIA | Register</title>

    <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet"/>
    <link th:href="@{/font-awesome/css/font-awesome.css}" rel="stylesheet"/>
    <link th:href="@{/css/plugins/iCheck/custom.css}" rel="stylesheet"/>
    <link th:href="@{/css/animate.css}" rel="stylesheet"/>
    <link th:href="@{/css/style.css}" rel="stylesheet"/>



    <!-- Sweet Alert -->
    <link th:href="@{/css/plugins/sweetalert/sweetalert.css}" rel="stylesheet"/>

</head>

<body class="gray-bg">

<div class="middle-box text-center loginscreen   animated fadeInDown" style="width: 30em; margin: auto; margin-top: 150px;">
    <div>
        <div>
            <h2 class="logo-name">欢迎您，先生/女士</h2>
        </div>
        <h3>欢迎注册</h3>
        <!--<p>Create account to see it in action.</p>-->
        <form class="m-t" role="form" action="/login/doRegister" method="post">
            <div class="form-group">
                <input type="text" class="form-control" placeholder="userName" name="name" required=""/>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" placeholder="password" name="password" required=""/>
            </div>
            <div class="form-group">
                <input type="email" class="form-control" placeholder="email" name="email" required=""/>
            </div>
            <div class="form-group">
                <div class="checkbox i-checks"><label> <input type="checkbox"/><i></i> 记住账号密码 </label></div>
            </div>
            <button type="submit" class="btn btn-primary block full-width m-b">注  册</button>

            <p class="text-muted text-center"><small>已经注册 ?</small></p>
            <a class="btn btn-sm btn-white btn-block" th:href="/login/login" href="login">登  录</a>
        </form>
        <!--<p class="m-t"> <small>Inspinia we app framework base on Bootstrap 3 &copy; 2014</small> </p>-->
    </div>
</div>
<!-- Mainly scripts -->
<script th:src="@{/js/jquery-2.1.1.js}"></script>
<script th:src="@{/js/bootstrap.min.js}"></script>
<!-- Mainly scripts -->
<!--<script src="js/jquery-2.1.1.js"></script>-->
<!--<script src="js/bootstrap.min.js"></script>-->
<!-- iCheck -->
<script src="js/plugins/iCheck/icheck.min.js"></script>
<!-- Sweet alert -->
<script th:src="@{/js/plugins/sweetalert/sweetalert.min.js}"></script>
<!--<script>-->

<!--$(document).ready(function(){-->
<!--$('.i-checks').iCheck({-->
<!--checkboxClass: 'icheckbox_square-green',-->
<!--radioClass: 'iradio_square-green',-->
<!--});-->
<!--});-->

<!--</script>-->

<script th:inline="javascript">
    var error = [[${error}]];
    $(document).ready(function () {
        if(error!=null){
            swal({
                title : "温馨提示",
                text : error
            });
        }
        $('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
        });
    });

</script>
</body>

</html>