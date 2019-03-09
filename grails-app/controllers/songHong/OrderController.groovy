package songHong

import com.alibaba.fastjson.JSONObject
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OrderController extends BaseController{
    def creat={
        println(params)
        def cu=dataService.mongoDb.findOneCustomer([_id:params._Uid])
        if(!cu){
            render(js(false,"此客户不存在，请输入正确的客户！"))
            return
        }
        def order=Order.newOne([
                _creatId:session.user._id,
                creatName:session.user.name,
                detail:[],/*[
                        _pid:[
                                num:0,
                                price:0,
                                remark:""
                        ],
                ]*/
                modeTransport : params.modeTransport,//运输方式
                userName : "",//客户姓名
                _Uid: params._Uid,//客户id
                leadTime:params.leadTime,//交货时间
                remark :params.remark,//备注
                amount :params.amount,//合计金额
                payWay:params.payWay,//支付方式
                addr:params.addr,//交货地址
        ])
        dataService.mongoDb.saveOrder(order)
        render(js(true,"创建成功"))
    }
    def getOrder={
        println("查看订单数据"+params)
        def form=[sort:[_id:-1]]
        def page=Integer.valueOf(params.page)
        def u=dataService.mongoDb.searchOrder(form,page,20)
        render(js(true,"查询成功",[list:u.contentlist,num:u.allNum]))
    }
    def delete={
        def _id=params._id
        println(1111111122)
        println params
        dataService.mongoDb.delOrder([_id:_id])
        render(js(true,"删除成功！"))
    }
}
