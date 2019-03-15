package songHong

class Order {

    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                creatName:"",//创建者姓名
                sellCold : "",//订单号
                detail:[
//                        [
//                                _id: "",
//                                name:"",
//                                code: "",
//                                num:0,
//                                price:0,
//                        ],
                ],
                modeTransport : "",//运输方式
                earnest:0,//定金
                userName : "",//客户姓名
                _Uid: "",//客户id
                leadTime: " ",//交货时间
                remark : "",//备注
                amount :"0",//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                payWay:"",//支付方式
                state: 0,//0为新创建的订单，1为生产中，2完成，-1未完成的
                payState:"",//0结清，-1未结清
                payRemark:"",//未结清时的说明
                addr:"",//交货地址
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
