package songHong

import grails.transaction.Transactional

@Transactional
class StockService extends BaseService{
    def getCode(){//生成订单号
        def time=new Date().format("yyyy-MM-dd")
        def n=dataService.mongoDb.findOneStock([sort:[code:-1],ct:[$gt:time]],[include:['code']])
        String a=time.replace("-","")
        if(n){
            String b=n.code.replace("K"+a,"")
            def c=Integer.valueOf(b)
            def code=c+1
            code=String.format("%04d", code)
            return "K"+a+code
        }else {
            return "K"+a+"0001"
        }
    }
    def inCome(detail){
        try {
        for(int i=0;i<detail.size();i++){
                def a=dataService.mongoDb.findOneProduct([_id:detail[i]._id],[include:["number"]])
                if(a) {
                    a.number += Double.valueOf(detail[i].num)
                    dataService.mongoDb.updateProduct([_id: detail[i]._id], [number: a.number,costPrice:Double.valueOf(detail[i].inPrice)])
                }
            }
            return true
        }catch(Exception e){
            return false
        }
    }
}
