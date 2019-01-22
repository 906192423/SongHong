package songHong

import grails.transaction.Transactional

@Transactional
class BaseService {
    def dataService
    def dbService=dataService.mongoDb
}
