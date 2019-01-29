package songHong


class BaseInterceptor {
    BaseInterceptor(){
        match(controller:"*",action:"*")
    }
    boolean before() {
        println("全局过滤器")
        true
    }

    boolean after() { true }
    void afterView() {
        // no-op
    }

}
