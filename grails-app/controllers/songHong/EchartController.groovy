package songHong

import com.alibaba.fastjson.JSONObject

class EchartController extends BaseController{
    def cashService
    def getCash={
        println(params)
        def c=cashService.count(params.start,params.end)
        render(JSONObject.toJSONString(c))
    }
}
