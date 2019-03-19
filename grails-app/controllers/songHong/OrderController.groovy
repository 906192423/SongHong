package songHong

import com.alibaba.fastjson.JSONObject
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OrderController extends BaseController{
    def orderService
    def creat={
        println(params)
        def cu=dataService.mongoDb.findOneCustomer([_id:params._Uid])
        if(!cu){
            render(js(false,"此客户不存在，请输入正确的客户！"))
            return
        }
        try {
            def order=Order.newOne([
                    _creatId:session.user._id,
                    creatName:session.user.name,
                    sellCode:orderService.getCode(),
                    detail: JSONObject.parse(params.detail),
                    earnest:params.earnest,
                    modeTransport : params.modeTransport,//运输方式
                    userName :cu.name,//客户姓名
                    _Uid:cu._id,//客户id
                    leadTime:params.leadTime,//交货时间
                    remark :params.remark,//备注
                    amount :Double.valueOf(params.amount),//合计金额
                    addr:params.addr,//交货地址
            ])
            dataService.mongoDb.saveOrder(order)
            render(js(true,"创建成功"))
        }catch(Exception e){
            render(js(false,"创建失败"))
        }
    }
    def getOrder={
        println("查看订单数据"+params)
        def form=[sort:[_id:-1]]
        def page=Integer.valueOf(params.page)
        def u=dataService.mongoDb.searchOrder(form,page,20)
        render(js(true,"查询成功",[list:u.contentlist,num:u.allNum]))
    }
    def delete={
        def _id=params._id
        def cu=dataService.mongoDb.findOneOrder([_id:_id])
        if(!cu){
            render(js(false,"此订单不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
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
    def deletes={
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
            if(cu._creatId==params._id||session.user.superUser){
                dataService.mongoDb.delOrder([_id:_id])
                b++
            }else {
                a++
            }
        }
        if(a==0){
            render(js(true,"全部成功"))
        }else {
            if(b!=0){
                render(js(true,"部分成功，注意只有是你创建的用户才能被你或超级管理员删除！"))
                return
            }
            render(js(false,"全部失败,注意只有是你创建的用户才能被你或超级管理员删除！！!"))
        }
    }
}
