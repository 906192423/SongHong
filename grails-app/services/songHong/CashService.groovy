package songHong

import grails.transaction.Transactional

import java.text.SimpleDateFormat

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
    def countWeek(start){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd")
        def time=new Date(format.parse(start).getTime()).getTime()
        def to=[:]
        def s,e
        def query
        for(int i=1;i<=7;i++){
            s=format.format(time)
            time=time+3600*1000*24
            e=format.format(time)
            query=[ct:[$lte:e,$gte:s]]
            def all=0
            def page=1
            def allPages=2
            def allList=[]
            for(;page<allPages;page++){
                def list=dataService.mongoDb.searchCash(query,[include:["amount"]],page,100)
                if(list){
                    allPages=list.allPages
                    allList.addAll(list.contentlist)
                }
            }
            allList.each {
                all+=it.amount
            }
            to."w${i}"=norTwo(all)
        }
        println(to)
        return to
    }
    def count(start,end,uid){
        println(start+"--------"+end+"-------------uid"+uid)
        def query=[ct:[$lte:end,$gte:start]]
        if(!"0".equals(uid)){
            query._creatId=uid
        }
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
    def Exchange(start,end,uid){
        println(start+"--------"+end+"-------------uid"+uid)
        def query=[ct:[$lte:end,$gte:start]]
        if(!"0".equals(uid)){
            query._creatId=uid
        }
        def page=1
        def allPages=2
        def allList=[]
        for(;page<allPages;page++){
            def list=dataService.mongoDb.searchExchange(query,[include:["amount"]],page,100)
            if(list){
                allPages=list.allPages
                allList.addAll(list.contentlist)
            }
        }
        def all=0
        println("000000000000000   "+allList)
        allList.each {
            all+=Integer.valueOf(it.amount)
        }
        return all
    }
}
