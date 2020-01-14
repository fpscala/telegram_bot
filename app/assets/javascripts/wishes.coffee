
$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/holiday-congratulation'
  #    getTeacher: '/get-teacher'
  #    getDepartment: '/get-department'
  #    getSubject: '/get-subjects'


  vm = ko.mapping.fromJS
    wishes: ''
    holidayId: ''
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
    data =
      wishes: vm.wishes()
      holidayId: parseInt(vm.holidayId())

    $.ajax
      url: apiUrl.send
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)
      vm.wishes undefined
      vm.holidayId undefined
      $('.close').click() ->
        $(this).parent().hide()



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