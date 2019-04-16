package songHong

import com.alibaba.fastjson.JSONObject

class BaseController {
    def dataService
    def api={//路径映射
        println("路径映射 "+params.apicon+"   "+params.apiAction)
        redirect(controller:params.apicon,action:params.apiAction,params:params)
    }
    protected js(boolean flag,String remark,map=null){
        def item=[flag:flag,remark:remark]
        if(map!=null){
            item.putAll(map)
        }
        JSONObject.toJSONString(item)
    }
    protected def norTwo(d) {
        BigDecimal bg = new BigDecimal(Double.valueOf(d))
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
    }
}
