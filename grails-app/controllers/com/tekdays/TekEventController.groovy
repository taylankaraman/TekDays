package com.tekdays

import org.springframework.dao.DataIntegrityViolationException

class TekEventController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [tekEventInstanceList: TekEvent.list(params), tekEventInstanceTotal: TekEvent.count()]
    }

    def create() {
        [tekEventInstance: new TekEvent(params)]
    }

    def save() {
        def tekEventInstance = new TekEvent(params)
        if (!tekEventInstance.save(flush: true)) {
            render(view: "create", model: [tekEventInstance: tekEventInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), tekEventInstance.id])
        redirect(action: "show", id: tekEventInstance.id)
    }

    def show(Long id) {
        def tekEventInstance = TekEvent.get(id)
        if (!tekEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), id])
            redirect(action: "list")
            return
        }

        [tekEventInstance: tekEventInstance]
    }

    def edit(Long id) {
        def tekEventInstance = TekEvent.get(id)
        if (!tekEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), id])
            redirect(action: "list")
            return
        }

        [tekEventInstance: tekEventInstance]
    }

    def update(Long id, Long version) {
        def tekEventInstance = TekEvent.get(id)
        if (!tekEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tekEventInstance.version > version) {
                tekEventInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tekEvent.label', default: 'TekEvent')] as Object[],
                          "Another user has updated this TekEvent while you were editing")
                render(view: "edit", model: [tekEventInstance: tekEventInstance])
                return
            }
        }

        tekEventInstance.properties = params

        if (!tekEventInstance.save(flush: true)) {
            render(view: "edit", model: [tekEventInstance: tekEventInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), tekEventInstance.id])
        redirect(action: "show", id: tekEventInstance.id)
    }

    def delete(Long id) {
        def tekEventInstance = TekEvent.get(id)
        if (!tekEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), id])
            redirect(action: "list")
            return
        }

        try {
            tekEventInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tekEvent.label', default: 'TekEvent'), id])
            redirect(action: "show", id: id)
        }
    }
}
