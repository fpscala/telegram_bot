
$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/add-student'
    getStudents: '/get-students'
  #    getDepartment: '/get-department'
  #    getSubject: '/get-subjects'


  vm = ko.mapping.fromJS
    firstName: ''
    lastName: ''
    birthday: ''
    telegramId: ''
    checkBinding: "It's connected"
    listStudents: []
  #    subjectList: []
  #    selectedSubject: ''
  #    selectedDepartment: ''
  #    listDepartment: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    data =
      firstName: vm.firstName()
      lastName: vm.lastName()
      birthday: vm.birthday()
      telegramId: parseInt(vm.telegramId())

    $.ajax
      url: apiUrl.send
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)
      vm.firstName undefined
      vm.lastName undefined
      vm.birthday undefined
      vm.telegramId undefined
      $('.close').click() ->
        $(this).parent().hide()

  getStudents = ->

    $.ajax
      url: apiUrl.getStudents
      type: 'GET'
    .fail handleError
    .done (response) ->
      console.log("11111111111111111", response)
      vm.listStudents(response)
  getStudents()
  #
  #  getDepartment = ->
  #    $.ajax
  #      url: apiUrl.getDepartment
  #      type: 'GET'
  #      .fail handleError
  #      .done (response) ->
  #      vm.listDepartment(response)
  #
  #  getDepartment()
  #
  #  getSubjectList = ->
  #    $.ajax
  #      url: apiUrl.getSubject
  #      type: 'GET'
  #      .fail handleError
  #      .done (response) ->
  #      vm.subjectList(response)
  #
  #  getSubjectList()

  ko.applyBindings {vm}