package songHong

import com.alibaba.fastjson.JSONObject

class ProductController extends BaseController{
    def creat={
        println("创建商品")
        println(params)
        try{
            def code=Integer.valueOf(params.code)
            def c=dataService.mongoDb.findOneProduct([code:code],[include:["_id"]])
            if(c){
                render(js(false,"此商品码已存在！"))
                return
            }
            c=dataService.mongoDb.findOneProduct([name:params.name],[include:["_id"]])
            if(c){
                render(js(false,"此商品名已存在！"))
                return
            }
            def detail=JSONObject.parse(params.detail)
            for (int i=0;i<detail.size();i++){
                detail[i].num=Double.valueOf(detail[i].num)
            }
            def p=Product.newOne([
                    name:params.name,//商品名
                    remark:params.remark,//备注
                    unit: params.unit,//单位
                    count:detail,
            ])
            if(detail.size()>0) p.group=true
            p.code=code//商品编号
            p.number=Double.valueOf(params.number)
            p.state=Integer.valueOf(params.state)
            p.classification=Integer.valueOf(params.classification)
            p.price=Double.valueOf(params.price)
            p.costPrice=Double.valueOf(params.costPrice)
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
                unit:params.unit,//商品单位
                remark: params.remark,//备注
        ])
        try{
            p.code=Integer.valueOf(params.code)//商品编号
            p.number=Integer.valueOf(params.number)
            p.state=Integer.valueOf(params.state)
            p.classification=Integer.valueOf(params.classification)
            p.price=Double.valueOf(params.price)
            p.costPrice=Double.valueOf(params.costPrice)
            dataService.mongoDb.updateProduct([_id:id],p)
            render(js(true,"修改成功"))
        }catch(Exception e){
            render(js(false,"请输入正确的数据！"))
        }
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
                form+=[code:nu]
                println("商品码查找"+nu)
            }catch(Exception e){
                println("商品名查找："+nu)
                form+=[name:[$regex:/${nu}/]]
            }
        }
        def page=1
        try{
            page=Integer.valueOf(params.page)
        }catch(Exception e){
        }
        def d=dataService.mongoDb.searchProduct(form,page,15)
        def goods=d.contentlist
        render(JSONObject.toJSONString([list:goods,flag:true,num:d.allNum]))
    }
    def gets={
        println("查找商品数据"+params)
        def nu=params.name
        def form=[sort:[_id:-1]]
        if(params.name){
            form+=[classification:Integer.valueOf(nu)]
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
