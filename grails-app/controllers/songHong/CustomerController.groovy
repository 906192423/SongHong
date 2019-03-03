package songHong

import com.alibaba.fastjson.JSONObject
import grails.transaction.Transactional

import java.lang.reflect.Array

@Transactional(readOnly = true)
class CustomerController extends BaseController{
    def creat={
        def cus=Customer.newOne([
                name:params.name,
                _creatId:session.user._id,
                email:params.email,
                idCardNumber:params.idCardNumber,
                phone:params.phone,
                qq :params.qq,
                remark :params.remark,
                sex:params.sex,
                birth:params.birth,
                addr:params.addr,
        ])
        dataService.mongoDb.saveCustomer(cus)
        render(js(true,"添加成功"))
    }
    def getCustomers={
        def page=params.page
        println("请求客户数据")
        def users=dataService.mongoDb.searchCustomer([sort:[_id:-1]],page,20)
        println(users)
        render(JSONObject.toJSONString([users:users.contentlist,num:users.allNum]))
    }
    def edit={
        println("修改用户数据")
        def cu=dataService.mongoDb.findOneCustomer([_id:params._id])
        if(!cu){
            render(js(false,"词用户不存在！"))
            return
        }
        if(cu._creatId==params._id||session.user.superUser){
            def cus=Customer.newOne([
                    name:params.name,
                    email:params.email,
                    idCardNumber:params.idCardNumber,
                    phone:params.phone,
                    qq :params.qq,
                    remark :params.remark,
                    sex:params.sex,
                    birth:params.birth,
                    addr:params.addr,
            ])
            dataService.mongoDb.updateCustomer([_id:params._id],cus)
            render(js(true,"修改成功"))
            return
        }
        render(js(false,"你不具有此权限，只有创建者或超级管理员可以删除"))
    }
    def delete={
        def _id=params._id
        def cu=dataService.mongoDb.findOneCustomer([_id:_id])
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
