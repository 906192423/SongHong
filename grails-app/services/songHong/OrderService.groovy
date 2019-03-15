package songHong

import grails.transaction.Transactional

@Transactional
class OrderService extends BaseService{
    def getCode(){//生产订单号
        def time=new Date().format("yyyy-MM-dd")
        def num=dataService.mongoDb.findCountOrder(ct:[$gt:time])
        def a=time.replace("-","")
        return a+num
    }
}
