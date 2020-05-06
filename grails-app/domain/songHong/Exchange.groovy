package songHong

class Exchange {

    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                creatName:"",//创建者姓名
                code: "",//订单号
                detail:[
//                        [
//                                _id: "",
//                                name:"",
//                                code: "",
//                                num:0,
//                                price:0,
//                        ],
                ],
                cashList:[
//                        [
//                                _id:"",
//                                code:"",
//                                amount:"",
//                        ]
                ],//交易单号
                userName : "",//客户姓名
                _Uid: "",//客户id
                remark : "",//备注
                amount :0,//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                allPay:false,//false未付，true全部付清
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
