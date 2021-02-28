package songHong

import grails.transaction.Transactional

@Transactional
class OrderService extends BaseService{
    static def nowNeedProduct=[]
    synchronized static def getCode(){//生成订单号
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
                            def item=getProduct(it)
                            needList.put(it._id,item)
                        }else {
                            needList.get(it._id).num+=it.num
                        }
                    }
                }
            }
        }
        nowNeedProduct=[]
        needList.each{
            nowNeedProduct.add(it.value)
        }
        nowNeedProduct
    }
    private def getProduct(item){
        def p=dataService.mongoDb.findOneProduct([_id:item._id])
        if(p){
            p.num=item.num
            return p
        }else {
            return item
        }
    }
    def income(detail){
        try {
            for(int i=0;i<detail.size();i++){
                def a=dataService.mongoDb.findOneProduct([_id:detail[i]._id],[include:["number","group","count"]])
                if(a) {
                    if(a.group){
                        a.count.each{
                            def p=dataService.mongoDb.findOneProduct([_id:it._id],[include:["number"]])
                            p.number+=Double.valueOf(detail[i].num*it.num)
                            dataService.mongoDb.updateProduct([_id:p._id], [number:p.number])
                        }
                    }else {
                        a.number += Double.valueOf(detail[i].num)
                        dataService.mongoDb.updateProduct([_id: detail[i]._id], [number:a.number])
                    }
                }
            }
            return true
        }catch(Exception e){
            return false
        }
    }
    def out(detail){
        try {
            for(int i=0;i<detail.size();i++){
                def a=dataService.mongoDb.findOneProduct([_id:detail[i]._id],[include:["number","group","count"]])
                if(a) {
                    if(a.group){
                        a.count.each{
                            def p=dataService.mongoDb.findOneProduct([_id:it._id],[include:["number"]])
                            p.number-=Double.valueOf(detail[i].num*it.num)
                            dataService.mongoDb.updateProduct([_id:p._id], [number:p.number])
                        }
                    }else {
                        a.number -= Double.valueOf(detail[i].num)
                        dataService.mongoDb.updateProduct([_id: detail[i]._id], [number:a.number])
                    }
                }
            }
            return true
        }catch(Exception e){
            return false
        }
    }
}
