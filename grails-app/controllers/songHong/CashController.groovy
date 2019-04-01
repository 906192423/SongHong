package songHong

import com.alibaba.fastjson.JSONObject
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CashController extends BaseController{
    def cashService
    def creat={
        println(params)
        def or=dataService.mongoDb.findOneOrder([_id:params._orderId],[include:["_id","cashList"]])
        if(!or){
            render(js(false,"此订单不存在"))
            return
        }
        try {
            def payForm= JSONObject.parse(params.domains)
            def pay=[]
            double total=0
            payForm.each {
                def amount=Double.valueOf(it.amount)
                total+=amount
                pay.add([
                        name:Integer.valueOf(it.name),
                        amount:amount
                ])
            }
            def cas=Cash.newOne([
                    code:cashService.getCode(),//流水号
                    _orderId:params._orderId,//订单号
                    _creatId:session.user._id,//创建人id
                    name:"",//支付人姓名
                    amount:total,//金额
                    payForm:pay,
                    remark : params.remark,//备注支付信息
            ])
            def c=dataService.mongoDb.saveCash(cas)
            if(or.cashList){
                or.cashList.add([
                        code:c.code,
                        _id:c._id,
                ]
                )}else {
                or.cashList=[
                        [
                                code:c.code,
                                _id:c._id,
                        ]
                ]
            }
            dataService.mongoDb.updateOrder([_id:or._id],[cashList:or.cashList])
            render(js(true,"成功"))
        }catch(Exception e){
            render(js(false,"失败"))
        }
    }
}