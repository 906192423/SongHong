package songHong

import grails.transaction.Transactional

@Transactional
class UserService extends BaseService{
    def doLogin={user->
        def date=new Date().format("yyyy-MM-dd HH:mm:ss.SSS")
        dataService.mongoDb.updateUser([_id:user._id],[lastLoginTime:date])
    }
}
