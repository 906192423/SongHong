package songHong

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
}
