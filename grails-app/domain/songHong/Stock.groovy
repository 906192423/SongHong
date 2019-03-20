package songHong

class Stock {

    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                _supplierId:"",//供货商id
                creatName:"",//创建者姓名
                stockCode: "",//订单号
                detail:[
//                        [
//                                _id: "",
//                                name:"",
//                                code: "",
//                                num:0,
//                                price:0,
//                        ],
                ],
                time: " ",//交货时间
                remark : "",//备注
                amount :"0",//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                state: 0,//0为新创建的订单，1为 ，2生产完成，-1未完成的
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
