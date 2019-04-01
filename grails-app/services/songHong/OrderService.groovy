package songHong

import grails.transaction.Transactional

@Transactional
class OrderService extends BaseService{
    def getCode(){//生成订单号
        def time=new Date().format("yyyy-MM-dd")
        //def num=dataService.mongoDb.findCountOrder(ct:[$gt:time])
        def n=dataService.mongoDb.findOneOrder([sort:[sellCode:-1],ct:[$gt:time]],[include:['sellCode']])
        String a=time.replace("-","")
        if(n){
            String b=n.sellCode.replace(a,"")
            def c=Integer.valueOf(b)
            def code=c+1
            code=String.format("%04d", code)
            return a+code
        }else {
            return a+"0001"
        }
    }
}
