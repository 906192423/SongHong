package songHong

import grails.transaction.Transactional

@Transactional
class CashService extends BaseService{
    def getCode(){//生成订单号
        def time=new Date().format("yyyy-MM-dd")
        def n=dataService.mongoDb.findOneCash([sort:[code:-1],ct:[$gt:time]],[include:['sellCode']])
        String a=time.replace("-","")
        if(n){
            String b=n.sellCode.replace(a,"")
            def c=Integer.valueOf(b)
            def code=c+1
            code=String.format("%04d", code)
            return "S"+a+code
        }else {
            return "S"+a+"0001"
        }
    }
}
