package songHong


class LoginInterceptor {
    LoginInterceptor(){
        match(controller:"*",action:"*")
    }
    boolean before() {//登陆过滤器
        if(controllerName=="Login")return true
        println("过滤器。。。。。:"+actionName+"    "+controllerName)
        if(session.user){
            return true
        }else {
            println("未登陆---重定向到登陆界面")
            redirect(url: "/")
        }
    }

    boolean after() { true }
    void afterView() {
        // no-op
    }

}
