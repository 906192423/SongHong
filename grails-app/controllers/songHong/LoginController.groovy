package songHong

import com.alibaba.fastjson.JSONObject
class LoginController extends BaseController{
    def index() {
        def a=dataService.mongoDb.findUser([sort:[ct:-1],limit:50])
        render(JSONObject.toJSONString(a))
    }
    def login={//登陆界面
        println("登陆界面")
        redirect(status:605,uri:"/dist/index.html")
        return
    }
    def doLogin={//进行登陆验证
        println(params)
        def u=dataService.mongoDb.findOneUser([name:params.name.trim()])
        if(u){
            if(u.pwd==params.password){
                session.user=u
                render(JSONObject.toJSONString([flag:true,remark:"登陆成功"]))
                return
            }
        }
        render(JSONObject.toJSONString([flag:false,remark:"登陆失败"]))
    }
    def register={
    }
    def doRegister={//注册
        println(params)
        if(params.name==null)return null
        if(params.password==null)return null
        def u=dataService.mongoDb.findOneUser([name:params.name.trim()])
        println("注册："+u)
        if (u){
            render(JSONObject.toJSONString([flag:false,remark:"此用户已存在！"]))
            return
        }
        def a=[
                name:params.name.toString().trim(),
                pwd:params.password.toString().trim()
        ]
        dataService.mongoDb.saveUser(User.newOne(a))
        render([flag:true])
    }
}
