package songHong

class BaseController {
    def dataService
    def dbService={
        return dataService.mongoDb()
    }
}
