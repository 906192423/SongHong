package songHong
import com.alibaba.fastjson.JSONObject
import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap

class OrderController extends BaseController{
    def orderService
    def cashService
    def creat={
        def cu=dataService.mongoDb.findOneCustomer([_id:params._Uid])
        if(!cu){
            render(js(false,"此客户不存在，请输入正确的客户！"))
            return
        }
        try {
            int payState
            def earnest=Integer.valueOf(params.earnest)
            if(earnest==0){
                payState=1
            }else {
                payState=0
            }
            def detail=JSONObject.parse(params.detail)
            for (int i=0;i<detail.size();i++){
                detail[i].price=norTwo(detail[i].price)
                detail[i].num=Double.valueOf(detail[i].num)
                detail[i].total=norTwo(detail[i].price*detail[i].num)
            }
            def order=Order.newOne([
                    _creatId:session.user._id,
                    creatName:session.user.name,
                    sellCode:orderService.getCode(),
                    payState:payState,
                    phone:params.phone,
                    detail:detail,
                    earnest:earnest,
                    modeTransport : params.modeTransport,//运输方式
                    userName :cu.name,//客户姓名
                    _Uid:cu._id,//客户id
                    leadTime:params.leadTime,//交货时间
                    remark :params.remark,//备注
                    amount :norTwo(params.amount),//合计金额
                    addr:params.addr,//交货地址
            ])
            dataService.mongoDb.saveOrder(order)
            orderService.out(detail)
            render(js(true,"创建成功"))
        }catch(Exception e){
            render(js(false,"创建失败"))
        }
    }
    def creatAll={
        def order=JSONObject.parse(params.order)
        def cu=dataService.mongoDb.findOneCustomer([_id:order._Uid])
        if(!cu){
            render(js(false,"此客户不存在，请输入正确的客户！"))
            return
        }
        try {
            int payState
            def earnest=1
            if(earnest==0){
                payState=1
            }else {
                payState=0
            }
            def detail=JSONObject.parse(order.detail)
            for (int i=0;i<detail.size();i++){
                detail[i].price=norTwo(detail[i].price)
                detail[i].num=Double.valueOf(detail[i].num)
                detail[i].total=norTwo(detail[i].price*detail[i].num)
            }
            def nOrder=Order.newOne([
                    _creatId:session.user._id,
                    creatName:session.user.name,
                    sellCode:orderService.getCode(),
                    payState:payState,
                    phone:order.phone,
                    detail:detail,
                    earnest:earnest,
                    modeTransport : order.modeTransport,//运输方式
                    userName :cu.name,//客户姓名
                    _Uid:cu._id,//客户id
                    leadTime:order.leadTime,//交货时间
                    remark :order.remark,//备注
                    amount :norTwo(order.amount),//合计金额
                    addr:order.addr,//交货地址
            ])
            def o=dataService.mongoDb.saveOrder(nOrder)
            orderService.out(detail)
            def payForm= JSONObject.parse(params.domains)
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
                    _orderId:o._id,//订单号
                    _creatId:session.user._id,//创建人id
                    creatName:session.user.name,
                    ordCode:o.sellCode,
                    cutAmount:cutAmount,
                    earnest:0,
                    name:"",//支付人姓名
                    amount:total,//金额
                    payForm:pay,
                    remark : params.remark,//备注支付信息
            ])
            def c=dataService.mongoDb.saveCash(cas)
            if(o.cashList){
                o.cashList.add([
                        code:c.code,
                        _id:c._id,
                ]
                )}else {
                o.cashList=[
                        [
                                code:c.code,
                                _id:c._id,
                                amount:c.amount
                        ]
                ]
            }
            if(o.payState==1){//下次交款状态的转移
                o.payState=2
            }else {
                o.payState=-1
                o.allPay=true
            }
            dataService.mongoDb.updateOrder([_id:o._id],[cashList:o.cashList,payState:o.payState,allPay:o.allPay])
            render(js(true,"创建成功"))
        }catch(Exception e){
            render(js(false,"创建失败"))
        }
    }
    def getOrder={
        println("查看订单数据"+params)
        def form=[sort:[_id:-1]]
        def page=Integer.valueOf(params.page)
        if(params.state){
            form.state=Integer.valueOf(params.state)
        }
        if(params.name){
            def nu=params.name
            try{
                nu=Integer.parseInt(params.name)
                form+=[sellCode:[$regex:/^${nu.toString()}/]]
                println("订单号查找")
            }catch(Exception e){
                println("名字查找："+nu)
                form+=[userName: [$regex:/^${nu}/]]
            }
        }
        def u=dataService.mongoDb.searchOrder(form,page,10)
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
        if(cu._creatId==session.user._id||session.user.superUser){
            if(cu.state==0){
                orderService.income(cu.detail)
                dataService.mongoDb.delOrder([_id:_id])
                render(js(true,"删除成功！"))
                return
            }else {
                render(js(false,"现在状态下无法删除"))
            }
        }
        render(js(false,"只有创建者或超级管理员能删除！"))
    }
    def deletes={
        if(session.user.superUser){
            def start=params.start
            def end=params.end
            def query=[ct:[$lte:end,$gte:start]]
            def page=1
            def allPages=2
            def all
            def Or=dataService.mongoDb.searchOrder(query,[include:["_id"]],page,200)
            if(Or){
                allPages=Or.allPages
                all=Or.allNum
            }
            ConcurrentReaderHashMap loop=[
                    a:0,
                    b:0,
            ]
            for(;page<=allPages;page++){
                def ord=dataService.mongoDb.searchOrder(query,[include:["_id"]],page,200)
                loop.a++
                println("删除第${page}次--------${Or}")
                baseService.taskPoolExecute{
                    ord.contentlist.each {
                        dataService.mongoDb.delOrder([_id:it._id])
                        dataService.mongoDb.delCash([_orderId:it._id])
                    }
                    loop.b++
                }
            }
            for(int i=0;loop.b<loop.a&&i<=200;i++){//最多等待40s
                Thread.sleep(200)
            }
            render(js(true,"删除成功！总删除${all}个订单"))
            return
        }
        render(js(false,"只有超级管理员能删除！"))
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
                render(js(true,"部分成功！"))
                return
            }
            render(js(false,"全部失败！"))
        }
    }
    def toComplete={
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
            if(cu.allPay){
                dataService.mongoDb.updateOrder([_id:_id],[state:2])
                b++
            }else {
                a++
            }
        }
        if(a==0){
            render(js(true,"全部成功"))
        }else {
            if(b!=0){
                render(js(true,"部分成功，注意只有订单结清全部款才能进入到完成队列！成功${b}条"))
                return
            }
            render(js(false,"全部失败,注意只有订单结清全部款才能进入到完成队列！"))
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
    def printOrder={
        println(params)
        def form=[sort:[_id:-1]]
        if(!session.user.superUser){
            form.isPrint=0
        }
        form+=[sellCode:[$regex:/^${params.code}/]]
        def list=dataService.mongoDb.searchOrder(form,1,10)
        def orders=[]
        list.contentlist.each{
            orders.add([
                    value:it.sellCode,
                    item:it
            ])
        }
        render JSONObject.toJSONString(list:orders,ph:session.user.phone)
    }
    def print={
        dataService.mongoDb.updateOrder([_id:params.id],[isPrint:1])
        render(js(true,"15151"))
    }
    def a={
        orderService.countProduct()
        println(orderService.nowNeedProduct)
        render(JSONObject.toJSONString([list:orderService.nowNeedProduct]))
    }
}
