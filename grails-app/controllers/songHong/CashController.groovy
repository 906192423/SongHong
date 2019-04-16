package songHong

import com.alibaba.fastjson.JSONObject
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CashController extends BaseController{
    def cashService
    def creat={
        println(params)
        def or=dataService.mongoDb.findOneOrder([_id:params._orderId],[include:["_id","cashList","sellCode","payState","allPay"]])
        if(!or){
            render(js(false,"此订单不存在"))
            return
        }
        try {
            def payForm= JSONObject.parse(params.domains)
            def earnest=Integer.valueOf(params.earnest)
            def cutAmount=Double.valueOf(params.cutAmount)
            def pay=[]
            double total=0
            payForm.each {
                def amount=norTwo(it.amount)
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
                    creatName:session.user.name,
                    ordCode:or.sellCode,
                    cutAmount:cutAmount,
                    earnest:earnest,
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
                                amount:c.amount
                        ]
                ]
            }
            if(or.payState==1){//下次交款状态的转移
                or.payState=2
            }else {
                or.payState=-1
                or.allPay=true
            }
            dataService.mongoDb.updateOrder([_id:or._id],[cashList:or.cashList,payState:or.payState,allPay:or.allPay])
            render(js(true,"成功"))
        }catch(Exception e){
            render(js(false,"失败"))
        }
    }
    def delete={
        def _id=params._id
        println(params)
        def cu=dataService.mongoDb.findOneCash([_id:_id],[include:['_creatId','_orderId']])
        if(!cu){
            render(js(false,"此交易单不存在！"))
            return
        }
        def or=dataService.mongoDb.findOneOrder([_id:cu._orderId],[include:['cashList','state']])
        if(or.state==2&&!session.user.superUser){
            render(js(false,"已交付的订单的交款记录不能被删除或联系超级管理员删除！"))
            return
        }
        if(cu._creatId==session.user._id||session.user.superUser){
            if(or){
                for(int i=0;i<or.cashList.size();i++){
                    if(or.cashList[i]._id==cu._id){
                        or.cashList.remove(i)
                        break
                    }
                }
                or.payState=2
                or.allPay=false
                dataService.mongoDb.updateOrder([_id:or._id],[cashList:or.cashList,payState:or.payState,allPay:false])
                dataService.mongoDb.delCash([_id:cu._id])
                render(js(true,"删除成功"))
                return
            }
        }
        render(js(false,"只有创建者或超级管理员能删除！"))
    }
    def getList= {
        println("查找交易数据" + params)
        def form = [sort: [_id: -1]]
        if (params.name) {
            def nu = params.name
            try {
                nu = Integer.parseInt(params.name)
                form += [ordCode: [$regex: /^${nu.toString()}/]]
                println("订单查找")
            } catch (Exception e) {
                println("交易号查找：" + nu)
                form += [code: [$regex: /^${nu}/]]
            }
        }
        def u = dataService.mongoDb.searchCash(form,params.page, 10)
        render(JSONObject.toJSONString([users: u.contentlist,num:u.allNum, flag: true]))
    }
    def getInfoList={
        println(params)
        def list=JSONObject.parse(params.list)
        def li=[]
        list.each {
            li.add(dataService.mongoDb.findOneCash([_id: it._id]))
        }
        println(li)
        render(JSONObject.toJSONString([list:li]))
    }
}
