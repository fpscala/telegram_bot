
$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/add-student'
    getStudents: '/get-students'
    updateStudents: '/update-students'
    deleteStudents: '/delete-students'
  #    getTeacher: '/get-teacher'
  #    getDepartment: '/get-department'
  #    getSubject: '/get-subjects'

  $modalEditStudent = $('#editStudents')
  $modalAddStudent = $('#addStudentModal')
  $modalDeleteStudent = $('#deletedStudents')

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
    getStudents: []
    id: 0
    selected:
      id: ''
      firstName: ''
      lastName: ''
      birthday: ''
      telegramId: ''
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

  vm.convertIntToDateTime = (intDate)->
    moment(intDate).format('MMM DD, YYYY')

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
        $modalAddStudent.modal("hide")
        getStudents()

  getStudents = ->
    $.ajax
      url: apiUrl.getStudents
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.getStudents(response)
  getStudents()

  vm.openEditFormStudents = (data) -> ->
    vm.selected.id(data.id)
    vm.selected.firstName(data.first_name)
    vm.selected.lastName(data.last_name)
    vm.selected.birthday(data.birthDay)
    vm.selected.telegramId(data.telegram_id)

  vm.updateStudents =  ->
    data =
      id: vm.selected.id()
      firstName: vm.selected.firstName()
      lastName: vm.selected.lastName()
      birthday: vm.selected.birthday()
      telegramId: vm.selected.telegramId()
    $.ajax
      url: apiUrl.updateStudents
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      $modalEditStudent.modal("hide")
      toastr.success(response)
      getStudents()

  vm.askDelete = (id) -> ->
    vm.selected.id(id)
    $modalDeleteStudent.modal("show")

  vm.deleteStudents = ->
    data =
      id: vm.selected.id()
    $.ajax
      url: apiUrl.deleteStudents
      type: 'DELETE'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)
      $modalDeleteStudent.modal("hide")
      getStudents()

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