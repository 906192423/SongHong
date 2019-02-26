package songHong

class Order {

    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                sellCold : "",//订单号
                detail:[],/*[
                        _pid:[
                                num:0,
                                price:0,
                                remark:""
                        ],
                ]*/
                modeTransport : "",//运输方式
                userName : "",//客户姓名
                _Uid: "",//客户id
                leadTime: " ",//交货时间
                remark : "",//备注
                amount :"0",//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                payWay:"",//支付方式
                state: 0,//0为新创建的订单，1为生产中，2完成，-1未完成的
                addr:"",//交货地址
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
