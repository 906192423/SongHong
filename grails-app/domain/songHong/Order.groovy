package songHong

class Order {

    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                creatName:"",//创建者姓名
                sellCode: "",//订单号
                phone:"",//联系电话
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
                earnest:0,//交款方式，1为全款一次结清，0交一部分定金，-1欠款
                userName : "",//客户姓名
                _Uid: "",//客户id
                leadTime: " ",//交货时间
                remark : "",//备注
                amount :"0",//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                state: 0,//0为新创建的订单，1为生产中，2生产完成，-1未完成的
                allPay:false,//false未付完全部，true全部付清
                addr:"",//交货地址
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
