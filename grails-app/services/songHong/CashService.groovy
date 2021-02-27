package songHong

import grails.transaction.Transactional
import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap
import java.util.concurrent.TimeUnit
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
        ConcurrentReaderHashMap cashList=[     /*线程安全*/
                z:0,
                w:0,
                x:0,
                k:0,
                all:"",
        ]
        def st=dataService.mongoDb.searchCash(query,[include:["_id"]],page,100)
        if(st){
            allPages=st.allPages
            cashList.all=st.allNum
        }
        ConcurrentReaderHashMap loop=[
            a:0,
            b:0,
        ]
        for(;page<=allPages;page++){
            println("查第${page}次--------${st}")
            int i=page
            loop.a++
            taskPoolExecute{
                println "统计第${i}次结果======"
                def list=dataService.mongoDb.searchCash(query,[include:["payForm"]],i,100)
                println "查询完成第${i}次======${list}"
                list.contentlist.each {
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
                loop.b++
            }
        }
        for(int i=0;loop.b<loop.a&&i<=100;i++){//最多等待20s
            Thread.sleep(200)
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
        long all=0
        allList.each {
            all+=it.amount
        }
        return all
    }
}
