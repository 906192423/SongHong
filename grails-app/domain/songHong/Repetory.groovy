package songHong

class Repetory {
    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                name:"",//原料名
                code: "",//原料编号
                remark: "",//备注
                price:0,//单价
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                state: 0,//0为可生产，-1无法生产

        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
