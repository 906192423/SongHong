package songHong

import com.alibaba.fastjson.JSONObject

class ExchangeController extends BaseController{
    def exchangeService
    def creat={
        println(params)
        def cu=dataService.mongoDb.findOneCustomer([_id:params._Uid])
        if(!cu){
            render(js(false,"此客户不存在，请输入正确的客户！"))
            return
        }
        try {
            def detail=JSONObject.parse(params.detail)
            def exchange=Exchange.newOne([
                    _creatId:session.user._id,
                    creatName:session.user.name,
                    code:exchangeService.getCode(),
                    detail: detail,
                    userName :cu.name,//客户姓名
                    _Uid:cu._id,//客户id
                    remark :params.remark,//备注
                    amount :Integer.valueOf(params.amount),//合计金额
            ])
            def e=dataService.mongoDb.saveExchange(exchange)
            if(e){
                if(exchangeService.inCome(detail)){
                    render(js(true,"创建成功"))
                    return
                }
            }
            render(js(false,"创建失败"))
        }catch(Exception e){
            render(js(false,"创建失败"))
        }
    }
    def getOrder={
        println("查看订单数据"+params)
        def form=[sort:[_id:-1]]
        def page=Integer.valueOf(params.page)
        def u=dataService.mongoDb.searchExchange(form,page,10)
        println("查看订单数据"+u)
        render(js(true,"查询成功",[list:u.contentlist,num:u.allNum]))
    }
    def delete={
        def _id=params._id
        def cu=dataService.mongoDb.findOneOrder([_id:_id])
        if(!cu){
            render(js(false,"此订单不存在！"))
            return
        }
        println(cu._creatId+"-------"+params._id+"----------"+session.user.superUser)
        if(cu._creatId==session.user._id||session.user.superUser){
            dataService.mongoDb.delOrder([_id:_id])
            render(js(true,"删除成功！"))
            return
        }
        render(js(false,"只有创建者或超级管理员能删除！"))
    }
    def edit={
        println("修改订单数据")
        def cu=dataService.mongoDb.findOneOrder([_id:params._id])
        if(!cu){
            render(js(false,"此订单不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
            def ord=[
                    modeTransport:params.modeTransport,
                    leadTime:params.leadTime,
                    remark:params.remark,
                    amount:params.amount,
                    qq :params.qq,
                    remark :params.remark,
                    addr:params.addr,
            ]
            dataService.mongoDb.updateOrder([_id:params._id],ord)
            render(js(true,"修改成功"))
            return
        }
        render(js(false,"你不具有此权限，只有创建者或超级管理员可以删除"))
    }
    def toProduct={
        println(params)
        def ids=params."ids[]"
        def id=[]
        if(ids instanceof String){
            id.add(ids)
        }else {
            id=ids
        }
        def a=0
        def b=0
        for(def _id:id){
            def cu=dataService.mongoDb.findOneOrder([_id:_id])
            if(cu._creatId==session.user._id||session.user.superUser){
                dataService.mongoDb.updateOrder([_id:_id],[state:1])
                b++
            }else {
                a++
            }
        }
        if(a==0){
            render(js(true,"全部成功"))
        }else {
            if(b!=0){
                render(js(true,"部分成功，注意只有是你创建的订单才能被你或超级管理员操作！"))
                return
            }
            render(js(false,"全部失败,注意只有是你创建的订单才能被你或超级管理员操作！"))
        }
    }
    def cheOrder={
        println(params)
        def form=[sort:[_id:-1]]
        form+=[sellCode:[$regex:/^${params.code}/],allPay:false]
        def list=dataService.mongoDb.searchOrder(form,1,10)
        def orders=[]
        list.contentlist.each{
            orders.add([
                    value:it.sellCode,
                    item:it
            ])
        }
        render JSONObject.toJSONString([list:orders])
    }
}
