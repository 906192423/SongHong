package songHong

import grails.transaction.Transactional
import com.alibaba.fastjson.JSONObject
@Transactional(readOnly = true)
class UserController extends BaseController{
    def creat={
        println(params)
        def cuss=Supplier.newOne([
                name:params.name,
                phone:params.elephone,
                email:params.email,
                idCardNumber:params.idCardNumber,
                email :params.email,
                remark :params.remark,
        ])
        dataService.mongoDb.saveUser(cuss)
        render(js(true,"添加成功"))
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
        def form=[sort:[_id:-1]]
        if(params.name){
            def nu=params.name
            try{
                nu=Integer.parseInt(params.name)
                form+=[phone: [$regex:/^${nu.toString()}/]]
                println("电话查找")
            }catch(Exception e){
                println("名字查找："+nu)
                form+=[name: [$regex:/^${nu}/]]
            }
        }
        def users=dataService.mongoDb.searchUser(form,page,20)
        println(users)
        render(JSONObject.toJSONString([users:users.contentlist, num:users.allNum]))
    }
    def edit={
        println("修改用户数据" +params)
        def cu=dataService.mongoDb.findOneUser([_id:params._id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
            def cus=[
                    name:params.name,
                    elephone:params.elephone,
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
    def delete={
        println("scvsdvssv用户数据"+params)
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
