package songHong

import grails.transaction.Transactional

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Transactional
class BaseService {
    synchronized static def dataService
    static def userList=[:]
    static ThreadPoolExecutor taskPool = new ThreadPoolExecutor(10, 30, 3000, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy())
    static {
        println("线程池创建成功-----")
        println(taskPool.toString())
        println("----------------------")
    }
    def norTwo(d) {
        if(!d){
            d=0
        }
        BigDecimal bg = new BigDecimal(Double.valueOf(d))
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
    }
    boolean getBoolean(Closure clo){
        boolean succ=true
        try{
            clo.call()
        }catch(Exception e){
            e.printStackTrace()
            succ=false
        }
        return succ
    }
    public def taskPoolExecute= { callback ->
        def c = {
            getBoolean(callback)//为的是捕捉异常
        }
        taskPool.execute(c)
    }
}
