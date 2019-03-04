package songHong


class LoginInterceptor {
    LoginInterceptor(){
        match(controller:"*",action:"*")
    }
    boolean before() {//登陆过滤器
        if(controllerName=="login"||controllerName=="base")return true
        println("登陆过滤器-------    :"+actionName+"    "+controllerName)
        if(session.user){
            return true
        }else {
            println("未登陆---重定向到登陆界面")
            return false
        }
    }

    boolean after() { true }
    void afterView() {
        // no-op
    }

}
