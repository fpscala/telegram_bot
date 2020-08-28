
$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/add-student'
  #    getTeacher: '/get-teacher'
  #    getDepartment: '/get-department'
  #    getSubject: '/get-subjects'

  Page =
    students: 'students'

  defaultStudentsData =
    firstName: ''
    lastName: ''
    birthday: ''
    telegramId: ''

  vm = ko.mapping.fromJS
    students: defaultStudentsData
    checkBinding: "It's connected"
    page: Glob.page
  #    subjectList: []
  #    selectedSubject: ''
  #    listTeachers: []
  #    selectedDepartment: ''
  #    listDepartment: []

  vm.selectedPage = (page) ->
    if (page is Page.students)
      vm.page(Page.students)

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.students.firstName)
      toastr.error("Please enter a first name")
      return no
    if (!vm.students.lastName)
      toastr.error("Please enter a last name")
      return no
    if (!vm.students.birthday)
      toastr.debug("Please enter a birthday date")
      return no
    if (!vm.students.telegramId)
      toastr.error("Please enter a telegram Id")
      return no
    else
      data =
        firstName: vm.students.firstName()
        lastName: vm.students.lastName()
        birthday: vm.students.birthday()
        telegramId: parseInt(vm.students.telegramId())
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
        ko.mapping.fromJS(defaultStudentsData, {}, vm.students)
        $('#addStudentModal').modal("hide")



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