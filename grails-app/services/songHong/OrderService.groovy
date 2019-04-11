package songHong

import grails.transaction.Transactional

@Transactional
class OrderService extends BaseService{
    static nowNeedProduct
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
    def countProduct(){
        def query=[state:1]
        def page=1
        def allPages=2
        def needList=[:]
        for(;page<allPages;page++){
            def list=dataService.mongoDb.searchOrder(query,[include:["detail"]],page,100)
            if(list){
                allPages=list.allPages
                list.contentlist.each{
                    it.detail.each{
                        if(!needList.get(it._id)){
                            needList.put(it._id,it)
                        }else {
                            needList.get(it._id).num+=it.num
                        }
                    }
                }
            }
        }
        nowNeedProduct=needList
    }
}
