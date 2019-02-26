package songHong

import com.alibaba.fastjson.JSONObject
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CustomerController extends BaseController{
    def creat={
        def cus=Customer.newOne(params)
        cus._creatId=session.user._id
        dataService.mongoDb.saveCustomer(cus)
        render(js(true,"添加成功"))
    }
    def getCustomer={
        def page=params.page
        println("请求客户数据")
        def users=dataService.mongoDb.searchCustomer([sort:[id:-1]],page,20)
        render(JSONObject.toJSONString(users.contentlist))
    }
    def delete={
        def id=params.id
        dataService.mongoDb.delCustomer([_id:id])
    }
}
