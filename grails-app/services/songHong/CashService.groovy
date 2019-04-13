package songHong

import grails.transaction.Transactional

@Transactional
class CashService extends BaseService{
    def getCode(){//生成订单号
        def time=new Date().format("yyyy-MM-dd")
        def n=dataService.mongoDb.findOneCash([sort:[code:-1],ct:[$gt:time]],[include:['code']])
        String a=time.replace("-","")
        if(n){
            String b=n.code.replace("C"+a,"")
            def c=Integer.valueOf(b)
            def code=c+1
            code=String.format("%04d", code)
            return "C"+a+code
        }else {
            return "C"+a+"0001"
        }
    }
    def count(start,end){
        println(start+"--------"+end)
        def query=[ct:[$lte:end,$gte:start]]
        def page=1
        def allPages=2
        def allList=[]
        def cashList=[
                z:0,
                w:0,
                x:0,
                k:0,
                all:"",
        ]
        for(;page<allPages;page++){
            def list=dataService.mongoDb.searchCash(query,[include:["payForm"]],page,100)
            if(list){
                allPages=list.allPages
                allList.addAll(list.contentlist)
            }
        }
        cashList.all=allList.size()
        allList.each {
            it.payForm.each{
                if(it.name==1){
                    cashList.z+=it.amount
                }else if(it.name==2){
                    cashList.w+=it.amount
                }else if(it.name==3){
                    cashList.x+=it.amount
                }else{
                    cashList.k+=it.amount
                }
            }
        }
        return cashList
    }
}
