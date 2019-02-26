package songHong

import com.alibaba.fastjson.JSONObject

class BaseController {
    def dataService
    def js(boolean flag,String remark){
        JSONObject.parse([flag:flag,remark:remark])
    }
}
