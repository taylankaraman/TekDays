package com.tekdays



import org.junit.*
import grails.test.mixin.*

@TestFor(TekMessageController)
@Mock(TekMessage)
class TekMessageControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/tekMessage/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.tekMessageInstanceList.size() == 0
        assert model.tekMessageInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.tekMessageInstance != null
    }

    void testSave() {
        controller.save()

        assert model.tekMessageInstance != null
        assert view == '/tekMessage/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tekMessage/show/1'
        assert controller.flash.message != null
        assert TekMessage.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tekMessage/list'

        populateValidParams(params)
        def tekMessage = new TekMessage(params)

        assert tekMessage.save() != null

        params.id = tekMessage.id

        def model = controller.show()

        assert model.tekMessageInstance == tekMessage
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tekMessage/list'

        populateValidParams(params)
        def tekMessage = new TekMessage(params)

        assert tekMessage.save() != null

        params.id = tekMessage.id

        def model = controller.edit()

        assert model.tekMessageInstance == tekMessage
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tekMessage/list'

        response.reset()

        populateValidParams(params)
        def tekMessage = new TekMessage(params)

        assert tekMessage.save() != null

        // test invalid parameters in update
        params.id = tekMessage.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tekMessage/edit"
        assert model.tekMessageInstance != null

        tekMessage.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tekMessage/show/$tekMessage.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        tekMessage.clearErrors()

        populateValidParams(params)
        params.id = tekMessage.id
        params.version = -1
        controller.update()

        assert view == "/tekMessage/edit"
        assert model.tekMessageInstance != null
        assert model.tekMessageInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tekMessage/list'

        response.reset()

        populateValidParams(params)
        def tekMessage = new TekMessage(params)

        assert tekMessage.save() != null
        assert TekMessage.count() == 1

        params.id = tekMessage.id

        controller.delete()

        assert TekMessage.count() == 0
        assert TekMessage.get(tekMessage.id) == null
        assert response.redirectedUrl == '/tekMessage/list'
    }
}
