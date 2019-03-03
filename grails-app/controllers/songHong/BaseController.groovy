package songHong

import com.alibaba.fastjson.JSONObject

class BaseController {
    def dataService
    def js(boolean flag,String remark){
        JSONObject.toJSONString([flag:flag,remark:remark])
    }
}
