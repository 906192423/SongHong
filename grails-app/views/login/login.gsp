<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登录</title>
<link type="text/css" rel="stylesheet" href="/assets/login/login.css" />
</head>
</head
<body>

<script src="/assets/jquery-2.1.3.js" type="text/javascript"></script>

<div class="logo_box">
	<h3>洪峰商贸公司欢迎你</h3>
	<form action="#" name="f" method="post" id="loginform">
		<div class="input_outer">
			<span class="u_user"></span>
			<input name="name" class="text" onFocus=" if(this.value=='输入ID或用户名登录') this.value=''" onBlur="if(this.value=='') this.value='输入ID或用户名登录'" value="输入ID或用户名登录" style="color: #FFFFFF !important" type="text">
		</div>
		<div class="input_outer">
			<span class="us_uer"></span>
			<label class="l-login login_password" style="color: rgb(255, 255, 255);display: block;">输入密码</label>
			<input name="password" class="text" style="color: #FFFFFF !important; position:absolute; z-index:100;" onFocus="$('.login_password').hide()" onBlur="if(this.value=='') $('.login_password').show()" value="" type="password">
		</div>
		<div class="mb2"><a onclick="login()" class="act-but submit" style="color: #FFFFFF">登录</a></div>
		<input name="savesid" value="0" id="check-box" class="checkbox" type="checkbox"><span>记住用户名</span>
	</form>
	<a href="#" class="login-fgetpwd" style="color: #FFFFFF">忘记密码？</a>
	<div class="sas">
		<a href="#">还没注册账号！</a>
	</div>
	
</div>
<script type="text/javascript">
    function login(){
        $.ajax({
            url : "login/doLogin",
            type : "post",
            dataType : "json",
            data:$('#loginform').serialize(),
            success : function(json) {
                if(json.flag){
                	alert("success")
                }else{
                    console.info('失败',json);
                    alert("false")
                }

            },
            beforeSend : function() {

            }
        });
    }
</script>
</body>
</html>