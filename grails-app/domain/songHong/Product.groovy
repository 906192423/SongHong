package songHong

class Product {
    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                name:"",//商品名
                classification:"",//商品分类
                code: "",//商品编号
                remark: "",//备注
                costPrice:"",//进价
                number:"",//库存数量
                price:0,//单价
                unit:"",//单位
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                state: 0,//0为可生产，-1无法生产

        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
