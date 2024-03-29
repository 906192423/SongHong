package songHong

class Product {
    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                name:"",//商品名
                classification:"",//商品分类，2为标准商品，3为称重商品，4为组合商品
                code: "",//商品编号
                remark: "",//备注
                costPrice:"",//进价
                number:"",//库存数量
                price:0,//单价
                unit:"",//单位
                group:false,//是否为组合商品
                count:[
//                        [
//                               _id:"",//商品_id 标识
//                               num:"",//需要数量
//                                ]
                ],
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                state: 0,//0为可生产，-1无法生产

        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
