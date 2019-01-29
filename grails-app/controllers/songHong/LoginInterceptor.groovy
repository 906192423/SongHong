package songHong


class LoginInterceptor {
    LoginInterceptor(){
        match(controller:"*",action:"*")
    }
    boolean before() {//登陆过滤器
        if(controllerName=="login")return true
        println("过滤器。。。。。:"+actionName+"    "+controllerName)
        if(session.user){
            return true
        }else {
            println("未登陆---重定向到登陆界面")
            redirect(uri:"/login/login")
            return false
        }
    }

    boolean after() { true }
    void afterView() {
        // no-op
    }

}
