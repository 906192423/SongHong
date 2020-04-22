package songHong

import com.alibaba.fastjson.JSONObject

class EchartController extends BaseController{
    def cashService
    def getCash={
        println(params)
        def c=cashService.count(params.start,params.end,params.uid)
        render(JSONObject.toJSONString(c))
    }
    def getCc={//取到周收款统计
        println(params)
        def c=cashService.countWeek(params.start)
        render(JSONObject.toJSONString([list:c]))
    }
}
