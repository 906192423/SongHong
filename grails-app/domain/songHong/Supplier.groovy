package songHong

class Supplier {

    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                name:"",//供货商名
                phone:"",//供货电话
                address:"",//地址
                email : "",//郵箱
                remark : "",//简介开户信息
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
