package songHong

import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserController extends BaseController{
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
}
