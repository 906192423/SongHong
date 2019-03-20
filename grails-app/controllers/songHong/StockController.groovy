package songHong

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StockController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Stock.list(params), model:[stockCount: Stock.count()]
    }

    def show(Stock stock) {
        respond stock
    }

    def create() {
        respond new Stock(params)
    }

    @Transactional
    def save(Stock stock) {
        if (stock == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (stock.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond stock.errors, view:'create'
            return
        }

        stock.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'stock.label', default: 'Stock'), stock.id])
                redirect stock
            }
            '*' { respond stock, [status: CREATED] }
        }
    }

    def edit(Stock stock) {
        respond stock
    }

    @Transactional
    def update(Stock stock) {
        if (stock == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (stock.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond stock.errors, view:'edit'
            return
        }

        stock.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'stock.label', default: 'Stock'), stock.id])
                redirect stock
            }
            '*'{ respond stock, [status: OK] }
        }
    }

    @Transactional
    def delete(Stock stock) {

        if (stock == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        stock.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'stock.label', default: 'Stock'), stock.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'stock.label', default: 'Stock'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
