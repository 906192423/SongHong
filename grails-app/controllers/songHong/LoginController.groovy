package songHong

import com.alibaba.fastjson.JSONObject


class LoginController{
    def LoginInterceptor = {
        println("controllerName: "+controllerName+"  actionName:"+actionName)
    }
    def index() { }
    def login={//登陆界面
    }
    def doLogin={//进行登陆验证
        println(params)
        render(JSONObject.toJSONString([flag:false,remark:"登陆成功"]))
    }
}
