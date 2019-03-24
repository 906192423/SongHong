package songHong

class Cash {
    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                code:"",//流水号
                _orderId:"",//订单号
                _creatId:"",//创建人id
                name:"",//支付人姓名
                amount:"",//金额
                payform:[
                        [
                                name:"",//支付名称
                                amount:"",//支付金额

                        ],
                ],//支付详情
                remark : "",//备注支付信息
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
