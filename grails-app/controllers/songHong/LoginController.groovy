package songHong

import com.alibaba.fastjson.JSONObject

import javax.servlet.http.Cookie

class LoginController extends BaseController{
    def userService
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
                if(u.superUser||u.canLogin){
                    session.user=u
                    userService.doLogin(u)
                    def code=UUID.randomUUID().toString()
                    println(code)
                    baseService.userList.put(u._id,code)
                    def cook= new Cookie("code",code)
                    cook.setMaxAge(200000)
                    cook.setPath("/")
                    response.addCookie(cook)
                    render(JSONObject.toJSONString([flag:true,remark:"登陆成功"]))
                    return
                }else {
                    render(JSONObject.toJSONString([flag:false,remark:"登陆失败,你已被禁止登录！"]))
                    return
                }
            }
        }
        render(JSONObject.toJSONString([flag:false,remark:"登陆失败"]))
    }
    def logout={
        println(params)
        session.user=null
        render(js(true,"成功下线"))
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
