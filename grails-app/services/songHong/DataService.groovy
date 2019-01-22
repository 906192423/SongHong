package songHong

import grails.transaction.Transactional
import service.DbService

@Transactional
//mongo 链接服务
class DataService {
    static private def map = [
    ip:"localhost",
    port:"27017",
    dbName:"songHong",//数据库名
    mongoUser:"",//登录名
    mongoUserPwd:"",//登录密码
    timeout:"",//option
    keepalive:"",//option
    connectRetry: "",//option
    ]
    def mongoDb=new DbService(map)
}