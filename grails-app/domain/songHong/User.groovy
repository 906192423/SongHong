package songHong

class User {
    static constraints = {
    }
    static newOne = { initObj = null ->
        def item = [
                _id : "",
                audit_phone : "",//认证手机号 0:已验证,-1:未验证
                email : "",//郵箱
                idCardNumber : "",//身份证号
                lastLoginTime : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//最后登陆时间
                name : "",//用戶名
                phone : "",//手机号
                pwd : "",//密码
                qq : " ",//qq
                remark : "",//简介
                superUser : true,//是否是超級管理員
                trueName : "",//真实姓名
                level:"0",//管理等级
                ct  : new Date().format("yyyy-MM-dd HH:mm:ss.SSS"),//创建时间
                sex:"",//性别
        ]
        if (initObj) item.putAll(initObj)
        return item
    }
}
