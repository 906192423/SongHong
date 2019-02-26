package songHong

class Customer {

    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                _creatId:"",//创建者id
                email : "",//郵箱
                idCardNumber : "",//身份证号
                name : "",//姓名
                phone : "",//手机号
                qq : " ",//qq
                remark : "",//简介
                point:"0",//积分
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                sex:"",//性别
                birth:"",//生日
                addr:"",//地址
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
