package songHong

import grails.transaction.Transactional

@Transactional(readOnly = true)
class CashController extends BaseController{
    def creat={
        println(params)
        def or=dataService.mongoDb.findOneOrder([_id:params._orderId])
        if(!or){
            render(js(false,"此订单不存在"))
            return
        }
        render(js(true,"成功"))
    }
}
