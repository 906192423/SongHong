package songHong

class Cash {
    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                code:"",//流水号
                ordCode:"",//订单号
                _orderId:"",//订单id
                _creatId:"",//创建人id
                name:"",//支付人姓名
                cutAmount:0,//优惠金额
                amount:"",//金额
                earnest:"",//交款方式，0为全款一次结清，1付定金 2支付剩余金额 其他 额外交款
                payForm:[
//                        [
//                                name:"",//支付名称  1：支付宝   2：微信   3:现金   4：刷卡
//                                amount:"",//支付金额
//
//                        ],
                ],//支付详情
                remark : "",//备注支付信息
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
