package songHong

import com.alibaba.fastjson.JSONObject

class ProductController extends BaseController{
    def creat={
        println("创建商品")
        println(params)
        def p=Product.newOne([
                name:params.name,//商品名
                code:params.code,//商品编号
                img:params.img,//商品图片
                remark: params.remark,//备注
                price:params.price,//单价
        ])
        try{
            p.state=Integer.valueOf(params.state)
            p.price=Double.valueOf(params.price)
            dataService.mongoDb.saveProduct(p)
            render(js(true,"创建商品成功"))
        }catch(Exception e){
            render(js(false,"请输入正确的数据！"))
        }
    }
    def edit={
        def id=params._id
        def pr=dataService.mongoDb.findOneProduct([_id:id])
        if(!pr){
            render(js(false,"此商品不存在"))
            return
        }
        def p=Product.newOne([
                name:params.name,//商品名
                code:params.code,//商品编号
                img:params.img,//商品图片
                remark: params.remark,//备注
                price:params.price,//单价
                state:Integer.valueOf(params.state),//0为可生产，-1无法生产
        ])
        dataService.mongoDb.updateProduct([_id:id],p)
        render(js(true,"修改成功"))
    }
    def delete={
        def id=params._id
        dataService.mongoDb.delProduct([_id: id])
        render(js(true,"删除成功"))
    }
    def getGoods={
        println("查找商品数据"+params)
        def form=[sort:[_id:-1]]
        if(params.name){
            def nu=params.name
            try{
                nu=Integer.parseInt(params.name)
                form+=[code: [$regex:/^${nu.toString()}/]]
                println("商品码查找")
            }catch(Exception e){
                println("商品名查找："+nu)
                form+=[name: [$regex:/^${nu}/]]
            }
        }
        def page=1
        try{
            page=Integer.valueOf(params.page)
        }catch(Exception e){
        }
        def d=dataService.mongoDb.searchProduct(form,page,10)
        def goods=d.contentlist
        render(JSONObject.toJSONString([list:goods,flag:true,num:d.allNum]))
    }
}
