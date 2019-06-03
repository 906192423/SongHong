package songHong

import com.alibaba.fastjson.JSONObject


class LoginInterceptor {
    LoginInterceptor(){
        match(controller:"*",action:"*")
    }
    boolean before() {//登陆过滤器
        if(controllerName=="login"||controllerName=="base")return true
        println("登陆过滤器-------    :"+actionName+"    "+controllerName)
        if(session.user){
            def c=request.getCookies()
            String code=0
            for(def co:c){
                if(co.getName().equals("code")){
                    code=co.getValue()
                    break
                }
            }
            if(code.equals(BaseService.userList.get(session.user._id))){
                return true
            }
        }
        println("未登陆---重定向到登陆界面")
        //redirect(uri:"/dist/index.html")
        //forward(controller:"login",action:"login")
        render(JSONObject.toJSONString([status:302,OnLine:true,remark:"登陆已失效！请重新登陆！！"]))
        return false
    }

    boolean after() { true }
    void afterView() {
        // no-op
    }

}
