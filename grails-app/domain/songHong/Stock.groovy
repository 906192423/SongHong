package songHong

class Stock {

    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                _supplierId:"",//供货商id
                supName:"",//供货商名字
                creatName:"",//创建者姓名
                code: "",//订单号
                earnest:"",//交款状态
                detail:[
//                        [
//                                _id: "",
//                                name:"",
//                                code: "",
//                                num:0,
//                                inPrice:0,
//                        ],
                ],
                inTime: " ",//交货时间
                amount :"0",//合计金额
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间

        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
