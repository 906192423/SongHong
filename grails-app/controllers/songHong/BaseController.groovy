package songHong

import com.alibaba.fastjson.JSONObject

class BaseController {
    def dataService
    def api={//路径映射
        println("路径映射 "+params.apicon+"   "+params.apiAction)
        redirect(controller:params.apicon,action:params.apiAction,params:params)
    }
    def js(boolean flag,String remark){
        JSONObject.toJSONString([flag:flag,remark:remark])
    }
}
