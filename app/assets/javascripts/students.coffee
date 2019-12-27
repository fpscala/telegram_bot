
$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/add-student'
  #    getTeacher: '/get-teacher'
  #    getDepartment: '/get-department'
  #    getSubject: '/get-subjects'


  vm = ko.mapping.fromJS
    firstName: ''
    lastName: ''
    birthday: ''
    telegramId: 0
  #    subjectList: []
  #    selectedSubject: ''
  #    listTeachers: []
  #    selectedDepartment: ''
  #    listDepartment: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    #    if (!vm.firstName())
    #      toastr.error("Please enter teacher's full name")
    #      return no
    #    else if (vm.fullName().length < 6 and vm.fullName().indexOf('-') isnt 0)
    #      toastr.error("The teacher's full name must consist of 6 letters")
    #      return no
    #    else if (!vm.selectedSubject())
    #      toastr.error("Please enter teacher's  subject")
    #      return no
    #    else if (!vm.selectedDepartment())
    #      toastr.error("Please enter teacher's department")
    #      return no

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
  #
  #  getTeachers = ->
  #    $.ajax
  #      url: apiUrl.getTeacher
  #      type: 'GET'
  #      .fail handleError
  #      .done (response) ->
  #      vm.listTeachers(response)
  #
  #  getTeachers()
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