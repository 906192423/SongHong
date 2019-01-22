package songHong

import com.alibaba.fastjson.JSONObject


class LoginController extends BaseController{
    def LoginInterceptor = {
        println("controllerName: "+controllerName+"  actionName:"+actionName)
    }
    def index() { }
    def login={//登陆界面
    }
    def doLogin={//进行登陆验证
        println(params)
        dataService.mongoDb.saveUser(params)
        render(JSONObject.toJSONString([flag:false,remark:"登陆成功"]))
//        def u=dataService.mongoDb().findOneUser([name:params.name.trim()])
//        if(u){
//            if(u.pwd==params.password){
//                render(JSONObject.toJSONString([flag:false,remark:"登陆成功"]))
//                session.user=u
//            }
//        }
    }
    def register={
    }
    def doRegister={//注册
        println(params)
        if(params.name==null)return null
        if(params.password==null)return null
        def u=dataService.mongoDb.findOneUser([name:params.name.trim()])
        if (u){
            render(JSONObject.toJSONString([flag:false,remark:"此用户已存在！"]))
            return
        }
        def a=[
                name:params.name.toString().trim(),
                pwd:params.password.toString().trim()
        ]
        dbService.saveUser(User.newOne(a))
        render([flag:true])
    }
}
