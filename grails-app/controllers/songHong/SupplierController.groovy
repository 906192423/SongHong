package songHong

import com.alibaba.fastjson.JSONObject

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SupplierController extends BaseController{

    def creat={
        println(params)
        def cuss=Supplier.newOne([
                name:params.name,
                elephone:params.elephone,
                email:params.email,
                address:params.address,
                email :params.email,
                remark :params.remark,
        ])
        dataService.mongoDb.saveSupplier(cuss)
        render(js(true,"添加成功"))
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
        def users=dataService.mongoDb.searchSupplier(form,page,20)
        render(JSONObject.toJSONString([users:users.contentlist, num:users.allNum]))
    }
    def getUsers={
        println("创建订单查找客户数据"+params)
        def form=[sort:[_id:-1]]
        if(params.name){
            def nu=params.name
            def a
            try{
                nu=Integer.parseInt(params.name)
                form+=[phone: [$regex:/^${nu.toString()}/]]
                a="name"
                println("电话查找")
            }catch(Exception e){
                println("名字查找："+nu)
                form+=[name: [$regex:/^${nu}/]]
                a="phone"
            }
            def u=dataService.mongoDb.searchSupplier(form,1,40)
            def users=[]
            u.contentlist.each{
                users.add([
                        value:it._id,
                        label:"${it."${a}"}".toString()
                ])
            }
            render(JSONObject.toJSONString([users:users,flag:true]))
        }
    }
    def edit={
        println("修改用户数据" +params)
        def cu=dataService.mongoDb.findOneSupplier([_id:params._id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
            def cus=[
                    name:params.name,
                    elephone:params.elephone,
                    email:params.email,
                    address:params.address,
                    email :params.email,
                    remark :params.remark,
            ]
            println cus
            dataService.mongoDb.updateSupplier([_id:params._id],cus)
            render(js(true,"修改成功"))
            return
        }
        render(js(false,"你不具有此权限，只有创建者或超级管理员可以删除"))
    }
    def delete={
        println("scvsdvssv用户数据"+params)
        def _id=params._id
        def cu=dataService.mongoDb.findOneSupplier([_id:_id])
        if(!cu){
            render(js(false,"此用户不存在！"))
            return
        }
        if(cu._id==params._id||session.user.superUser){
            dataService.mongoDb.delSupplier([_id:_id])
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
            def cu=dataService.mongoDb.findOneSupplier([_id:_id])
            if(cu._creatId==params._id||session.user.superUser){
                dataService.mongoDb.delSupplier([_id:_id])
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
