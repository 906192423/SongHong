package songHong

import com.alibaba.fastjson.JSONObject

class RepetoryController extends BaseController{
    def creat={
        println("创建库存")
        println(params)
        def code
        try{
            code=Integer.valueOf(params.code)
        }catch(Exception e){
            render(js(false,"库存码为纯数字"))
            return
        }
        def a=dataService.mongoDb.findOneRepetory([name:params.name],[include:["_id"]])
        if(a){
            render(js(false,"名字已有"))
            return
        }
        a=dataService.mongoDb.findOneRepetory([code:params.name],[include:["_id"]])
        if(a){
            render(js(false,"库存码已有"))
            return
        }
        try{
            def p=Product.newOne([
                    name:params.name,//原料名
                    code:code,//原料编号
                    remark:params.remark,//备注
                    price:params.price,//单价
                    amount:Integer.valueOf(params.amount),
            ])
            dataService.mongoDb.saveRepetory(p)
            render(js(true,"创建库存商品成功"))
        }catch(Exception e){
            render(js(false,"请输入正确的数据！"))
        }
    }
    def edit={
        def id=params._id
        def pr=dataService.mongoDb.findOneRepetory([_id:id])
        if(!pr){
            render(js(false,"此货物不存在"))
            return
        }
        def p=[
                name:params.name,//原料名
                code:Integer.valueOf(params.code),//原料编号
                remark:params.remark,//备注
                price:params.price,//单价
                amount:Integer.valueOf(params.amount),
        ]
        dataService.mongoDb.updateRepetory([_id:id],p)
        render(js(true,"修改成功"))
    }
    def delete={
        def id=params._id
        dataService.mongoDb.delRepetory([_id: id])
        render(js(true,"删除成功"))
    }
    def getGoods={
        println("查找库存数据"+params)
        def form=[sort:[_id:-1]]
        if(params.name){
            def nu=params.name
            try{
                nu=Integer.parseInt(params.name)
                form+=[code: [$regex:/^${nu.toString()}/]]
                println("库存码查找")
            }catch(Exception e){
                println("库存名查找："+nu)
                form+=[name: [$regex:/^${nu}/]]
            }
        }
        def page=1
        try{
            page=Integer.valueOf(params.page)
        }catch(Exception e){
        }
        def d=dataService.mongoDb.searchRepetory(form,page,10)
        def goods=d.contentlist
        render(JSONObject.toJSONString([list:goods, flag:true, num:d.allNum]))
    }
}
