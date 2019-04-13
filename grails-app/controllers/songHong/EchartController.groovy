package songHong

class EchartController extends BaseController{
    def cashService
    def getCash={
        println(params)
        def c=cashService.count(params.start,params.end)
        render(js(true,"${params.start}到${params.end}的记录"),c)
    }
}
