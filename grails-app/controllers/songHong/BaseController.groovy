package songHong

import com.alibaba.fastjson.JSONObject

class BaseController {
    def baseService
    def dataService
    protected js(boolean flag,String remark,map=null){
        def item=[flag:flag,remark:remark]
        if(map!=null){
            item.putAll(map)
        }
        JSONObject.toJSONString(item)
    }
    protected def norTwo(d){
        if(!d){d=0}
        BigDecimal bg = new BigDecimal(Double.valueOf(d))
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
    }
}
