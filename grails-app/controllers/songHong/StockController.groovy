package songHong
import com.alibaba.fastjson.JSONObject
class StockController extends BaseController{

    def creat={
        println(params)
        try {
            def order=Stock.newOne([
                    _creatId:session.user._id,
                    detail: JSONObject.parse(params.detail),
                    earnest:Integer.valueOf(params.earnest),
                    time:params.time,//交货时间
                    amount :Double.valueOf(params.amount),//合计金额
            ])
            dataService.mongoDb.saveStock(order)
            render(js(true,"创建成功"))
        }catch(Exception e){
            render(js(false,"创建失败"))
        }
    }
    def getCustomers={
        def page=params.page
        println("请求客户数据")
        def form=[sort:[_id:-1]]
        if(params.name){
            def nu=params.name
            try{
                nu=Integer.parseInt(params.name)
                form+=[elephone: [$regex:/^${nu.toString()}/]]
                println("电话查找")
            }catch(Exception e){
                println("名字查找："+nu)
                form+=[name: [$regex:/^${nu}/]]
            }
        }
        def users=dataService.mongoDb.searchStock(form,page,20)
        render(JSONObject.toJSONString([users:users.contentlist, num:users.allNum]))
    }
    def getOrder={
        println("查看订单数据"+params)
        def form=[sort:[_id:-1]]
        def page=Integer.valueOf(params.page)
        if(params.state){
            form.state=Integer.valueOf(params.state)
        }
        def u=dataService.mongoDb.searchStock(form,page,20)
        println("查看订单数据"+u)
        render(js(true,"查询成功",[list:u.contentlist,num:u.allNum]))
    }
    def getUsers={
        println("查找供应商数据"+params)
        def form=[sort:[_id:-1]]
        if(params.name){
            def nu=params.name
            try{
                nu=Integer.parseInt(params.name)
                form+=[elephone: [$regex:/^${nu.toString()}/]]
                println("电话查找")
            }catch(Exception e){
                println("名字查找："+nu)
                form+=[name: [$regex:/^${nu}/]]
            }
            def u=dataService.mongoDb.searchStock(form,1,40)
            println("查找"+u)
            def users=[]
            u.contentlist.each{
                users.add([
                        value:JSONObject.toJSONString(it),
                        label:it.name
                ])
            }
            render(JSONObject.toJSONString([users:users,flag:true]))
        }
    }
    def edit={
        println("修改用户数据")
        def cu=dataService.mongoDb.findOneStock([_id:params._id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
            def cus=[
                    name:params.name,
                    email:params.email,
                    idCardNumber:params.idCardNumber,
                    phone:params.phone,
                    qq :params.qq,
                    remark :params.remark,
                    sex:params.sex,
                    birth:params.birth,
                    addr:params.addr,
            ]
            dataService.mongoDb.updateStock([_id:params._id],cus)
            render(js(true,"修改成功"))
            return
        }
        render(js(false,"你不具有此权限，只有创建者或超级管理员可以删除"))
    }
    def delete={
        def _id=params._id
        def cu=dataService.mongoDb.findOneStock([_id:_id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
            dataService.mongoDb.delCustomer([_id:_id])
            render(js(true,"删除成功！"))
            return
        }
        render(js(false,"只有创建者或超级管理员能删除！"))
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
            def cu=dataService.mongoDb.findOneCustomer([_id:_id])
            if(cu._creatId==params._id||session.user.superUser){
                dataService.mongoDb.delCustomer([_id:_id])
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
