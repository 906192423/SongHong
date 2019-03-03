package songHong

import grails.transaction.Transactional

@Transactional(readOnly = true)
class OrderController extends BaseController{
    def creat={
        println(params)
        def order=Order.newOne([
                _creatId:session.user._id,
                sellCold : "",//订单号
                detail:[],/*[
                        _pid:[
                                num:0,
                                price:0,
                                remark:""
                        ],
                ]*/
                modeTransport : params.modeTransport,//运输方式
                userName : "",//客户姓名
                _Uid: params.uid,//客户id
                leadTime:params.leadTime,//交货时间
                remark :params.remark,//备注
                amount :params.amount,//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                payWay:params.payWay,//支付方式
                state: 0,//0为新创建的订单，1为生产中，2完成，-1未完成的
                addr:params.addr,//交货地址
        ])
        dataService.mongoDb.saveOrder(order)
        render(js(true,"创建成功"))
    }
}
