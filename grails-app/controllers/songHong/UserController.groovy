package songHong

import grails.transaction.Transactional
import com.alibaba.fastjson.JSONObject
@Transactional(readOnly = true)
class UserController extends BaseController{
    def creat={
        println(params)
        if(session.user.superUser){
            def cuss=User.newOne([
                    name:params.name,
                    phone:params.phone,
                    pwd:params.pwd,
                    email:params.email,
                    idCardNumber:params.idCardNumber,
                    email :params.email,
                    remark :params.remark,
                    trueName:params.trueName
            ])
            dataService.mongoDb.saveUser(cuss)
            render(js(true,"添加成功"))
        }else{
            render(js(false,"你没有权限"))
        }
    }
    def isLogin={
        render(js(true,"已登陆"))
    }
    def getInfo={
        def id=params._id
        def user=dataService.mongoDb.findOneUser([_id:id])
        user.pwd=""
        if(user)
            render(js(true,"成功",user))
        else render(js(true,"此用户不存在"))
    }
    def info={
        def user=session.user
        user.pwd=""
        if(user)
            render(js(true,"成功",user))
        else render(js(true,"此用户不存在"))
    }
    def getUsers={
        def page=params.page
        println("请求客户数据")
        if(session.user.superUser){
            def form=[sort:[_id:-1]]
            if(params.name){
                def nu=params.name
                try{
                    nu=Integer.parseInt(params.name)
                    form+=[name: [$regex:/^${nu.toString()}/]]
                    println("电话查找")
                }catch(Exception e){
                    println("名字查找："+nu)
                    form+=[trueName: [$regex:/^${nu}/]]
                }
            }
            def users=dataService.mongoDb.searchUser(form,page,20)
            render(JSONObject.toJSONString([users:users.contentlist, num:users.allNum]))
        }else{
            render(js(false,"你不具备此权限"))
        }
    }
    def getSmallUsers={
        def page=0
        println("请求销售员")
        if(session.user.superUser){
            def form=[sort:[_id:1]]
            def users=dataService.mongoDb.searchUser(form,[include:["_id","trueName"]],page,50)
            println(users)
            render(JSONObject.toJSONString([users:users.contentlist, num:users.allNum]))
        }else{
            render(js(false,"你不具备此权限"))
        }
    }
    def edit={
        println("修改用户数据" +params)
        def cu=dataService.mongoDb.findOneUser([_id:params._id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(session.user._id==params._id||session.user.superUser){
            def cus=[
                    pwd:params.pwd,
                    level:Integer.valueOf(params.level),
                    qq:params.qq,
                    trueName:params.trueName,
                    phone:params.phone,
                    email:params.email,
                    idCardNumber:params.idCardNumber,
                    email :params.email,
                    remark :params.remark,
            ]
            println cus
            dataService.mongoDb.updateUser([_id:params._id],cus)
            render(js(true,"修改成功"))
            return
        }
        render(js(false,"你不具有此权限，只有创建者或超级管理员可以删除"))
    }
    def sch={
        println("修改登陆权限"+params)
        def cu=dataService.mongoDb.findOneUser([_id:params._id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(session.user.superUser){
            dataService.mongoDb.updateUser([_id:params._id],[canLogin:Boolean.valueOf(params.canLogin)])
            baseService.userList.remove(params._id)
            render(js(true,"修改成功"))
            return
        }
        render(js(false,"你不具有此权限，只有创建者或超级管理员可以删除"))
    }
    def delete={
        println("用户数据"+params)
        def _id=params._id
        def cu=dataService.mongoDb.findOneUser([_id:_id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(cu._id==params._id||session.user.superUser){
            dataService.mongoDb.delUser([_id:_id])
            render(js(true,"删除成功！"))
            return
        }
        render(js(false,"只有创建者或超级管理员能删除！"))
    }
}
