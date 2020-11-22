app.controller("LinkController", function($scope, $http, $state/*, $timeout, $interval*/) {

    $scope.form = {};
    $scope.attestationForTable = {};
    getAttestation();
    getAllFormOfWork();

    function getAttestation () {
        $http({
            method: 'GET',
            url: '/getAttestation'
        }).then(
            function(res) {
                $scope.attestations = res.data;
                $scope.attestation = res.data[0].id;

                $scope.attestationsForTable = res.data;
                $scope.attestationForTable.id = res.data[0].id;
            },
            function(res) {
                // console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function getAllFormOfWork () {
        $http({
            method: 'GET',
            url: '/getAllFormOfWork'
        }).then(
            function(res) {
                $scope.forms_of_work = res.data;
                $scope.form_of_work = res.data[0].id;
            },
            function(res) {
                // console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    getAllFaculties();
    function getAllFaculties() {
        $http.get('/getAllFaculties')
            .then(
                /*$http({
                    method: 'GET',
                    url: '/getAllFaculties'
                }).then(*/
                function (res) { // success
                    $scope.faculties = res.data;
                },
                function (res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
    }

    $scope.getDepartments = function () {
        $http({
            method: 'PATCH',
            url: '/getDepartments',
            data: angular.toJson($scope.faculty),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function (res) { // success
                $scope.departments = res.data;
                $scope.department = null;
                $scope.group = null;
                $scope.studentsForm.group.$modelView = null;
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };

    $scope.getGroups = function () {
        /*if ($scope.department == null) {
            $scope.group = null;
            $scope.studentsForm.group = null;
            return;
        }*/
        $http({
            method: 'PATCH',
            url: '/getGroups',
            data: angular.toJson($scope.department),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function (res) { // success
                $scope.groups = res.data;
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };

    $scope.watchStudents = function() {
        if(!$scope.isSubgroup) {
            if (!$scope.group) {
                $scope.toasterWarning('Необходимо выбрать группу');
                return null;
            }
            else {
                $http({
                    method: 'PATCH',
                    url: '/findNotExpelledStudents',
                    data: angular.toJson($scope.group),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(
                    function(res) { // success
                        $scope.students = res.data;
                        $scope.getSubjectsWithTeachersByDep($scope.group);
                        // if (!$scope.students.length)
                        //     $scope.toasterWarning('В выбранной группе нет студентов или все были отчислены');
                    },
                    function(res) { // error
                        $scope.toasterWarning('В выбранной группе студенты не были найдены');
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
            }
        }
        else { // $scope.isSubgroup
            if(!$scope.subgroupId) {
                $scope.toasterWarning('Необходимо выбрать подгруппу');
                return null;
            }
            else {
                $scope.subgroup = {
                    id: $scope.subgroupId
                };
                $http({
                    method: 'PATCH',
                    url: '/findNotExpelledStudentsFromSubgroup',
                    data: angular.toJson($scope.subgroup),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(
                    function(res) { // success
                        $scope.students = res.data;
                        $scope.getSubjectsWithTeachersByDep($scope.subgroupId);
                        // if (!$scope.students.length)
                        //     $scope.toasterWarning('В выбранной подгруппе нет студентов или все были отчислены');
                    },
                    function(res) { // error
                        $scope.toasterWarning('В выбранной подгруппе студенты не были найдены');
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
            }
        }

    };

    $scope.getSubjectsWithTeachersByDep = function(groupOrSubgroup) {
        $http({
            method: 'PATCH',
            url: '/getSubjectsWithTeachersByDep',
            params: {
                isSubgroup: !!$scope.isSubgroup, // если undefined
                groupOrSubgroup: groupOrSubgroup,
                attestation: $scope.attestationForTable.id
            },
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) { // success
                $scope.subjectsAndTeachers = res.data;
            }
        );
    };

    $scope.findByNameSubgroup = function() {
        if (!$scope.partNameSubgroup) {
            $scope.toasterWarning('Необходимо ввести название (или часть названия) подгруппы');
            return null;
        }
        $scope.subgroup = {  // из-за ""
            name: $scope.partNameSubgroup
        };
        $http({
            method: 'PATCH',
            url: '/findByNameSubgroup',
            data: angular.toJson($scope.subgroup),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function (res) { // success
                $scope.subgroups = res.data;
                if (!$scope.subgroups.length)
                    $scope.toasterWarning('Согласно введённым данным не было найдено подгрупп');
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };

    $scope.checkIsSubgroup = function() {
        $scope.students = null;
        $scope.viewLinkForm = false;
    };

    $scope.checkPartNameSubgroup = function() {
        $scope.subgroups = null;
        $scope.viewLinkForm = false;
    };

    $scope.checkSubgroupId = function() {
        $scope.students = null;
        $scope.viewLinkForm = false;
    };

    $scope.checkGroup = function() {
        $scope.students = null;
        $scope.viewLinkForm = false;
    };

    $scope.addDiscipline = function() {
        $scope.viewLinkForm = true;
        getGroupsOfSelectedStudents();
    };

    function getGroupsOfSelectedStudents () {
        $http({
            method: 'PATCH',
            url: '/getGroupsOfSelectedStudents',
            data: angular.toJson($scope.students),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.groupsForDiscipline = res.data;
                $scope.groupForDiscipline = res.data[0].id;
                $scope.getSemesterNumbers($scope.groupForDiscipline);
            },
            function(res) {
                // console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    // при смене GroupsOfSelectedStudents вызывать getSemesterNumber();
    $scope.getSemesterNumbers = function(selectedGroupId) {
        $http({
            method: 'PATCH',
            url: '/getSemesterNumbers',
            data: angular.toJson(selectedGroupId),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.semesterNumbers = res.data;
                $scope.clearTeacherAndDiscipline();
                getCurSemNumb(selectedGroupId);
            },
            function(res) {
                // console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };

    function getCurSemNumb(selectedGroupId) {
        $http({
            method: 'PATCH',
            url: '/getCurSemNumb',
            data: angular.toJson(selectedGroupId), // id
            headers: { 'Content-Type': 'application/json' }
        }).then(
            function(res) {
                $scope.curSemesterNumber = res.data;
                // $scope.semesterNumber = res.data;
                // $scope.getDisciplinesByDep(selectedGroupId, $scope.curSemesterNumber);
            }
        );
    }

    $scope.getDisciplinesByDep = function (selectedGroupId, selectedSemesterNumber) {
        if (selectedSemesterNumber == null) // == undefined (р. и с null)
            return;
        // fetch();
        $http({
            method: 'PATCH',
            url: '/getDisciplinesByDep',
            params: {
                groupId: selectedGroupId,
                semesterNumber: selectedSemesterNumber
            }
        }).then(
            function(res) {
                $scope.clearTeacherAndDiscipline(); // важно в первом случае (status === 204)

                if (+res.status === 204) // не закрывать форму, так как может выбрать другой семестр
                    $scope.toasterWarning('У выбранной группы в данном семестре на Вашей кафедре нет предметов!');
                else // status == 200
                    $scope.disciplines = res.data;
                    // $scope.discipline = {}; // для этого clearTeacherAndDiscipline()
            }
        );
    };

    $scope.getTeachers = function (discipline) {
        $http({
            method: 'PATCH',
            url: '/getTeachers',
            data: angular.toJson(discipline.id),
            headers: { 'Content-Type': 'application/json' }
        }).then(
            function(res) {
                $scope.teachers = res.data;
                $scope.teacher = {};
            }
        );
    };

    $scope.addCertificationAttestation = function () {
        if ($scope.form.linkForm.$invalid) {
            $scope.toasterWarning('Необходимо корректно заполнить все поля формы!');
            return;
        }
        var group = null, subgroup = null;
        if ($scope.isSubgroup)
            subgroup = $scope.subgroupId;
        else
            group = $scope.group;

        $scope.certificationAttestation = {
            syllabusContent: {
                discipline: { id: $scope.form.linkForm.discipline.$modelValue.id },
                semesterNumber: $scope.form.linkForm.semesterNumber.$modelValue
            },
            teacher: { id: $scope.form.linkForm.teacher.$modelValue.id },
            attestation: { id: $scope.form.linkForm.attestation.$modelValue },
            isSubgroup : !!$scope.isSubgroup, // если вдруг undefined
            group: { id: group },
            subgroup: { id: subgroup },
            form_of_work: { id: $scope.form.linkForm.form_of_work.$modelValue }
        };
        // console.log('$scope.certificationAttestation', $scope.certificationAttestation)
        $http({
            method: 'PATCH',
            url: '/addCertificationAttestation',
            params: {
                groupId: $scope.form.linkForm.groupForDiscipline.$modelValue
            },
            data: angular.toJson($scope.certificationAttestation),
            headers: { 'Content-Type': 'application/json' }
        }).then(
            function(res) {
                $scope.subjectsAndTeachers = res.data;
                $scope.attestationForTable.id = $scope.form.linkForm.attestation.$modelValue;
                $scope.closeLinkForm();
                $scope.toasterSuccess('Предмет успешно добавлен!');
            },
            function(res) {
                $scope.closeLinkForm();
                $scope.toasterError('При добавлении предмета произошла ошибка! Обратитесь к администратору'); // или обновите страницу
            }
        );
    };

    $scope.closeLinkForm = function () {
        $scope.viewLinkForm = false;
        $scope.clearTeacherAndDiscipline();
    };

    $scope.clearTeacherAndDiscipline = function () {
        $scope.teachers = null;
        $scope.teacher = {};
        $scope.disciplines = null;
        $scope.discipline = {};
    };

    $scope.checkBeforeDeleteDiscipline = function (subjectAndTeacher) {
        $http({
            method: 'PATCH',
            url: '/checkBeforeDeleteDiscipline',
            params: {
                certificationAttestationId: subjectAndTeacher.id
            }
        }).then(
            function(res) {
                // console.log(res.data);
                deleteDiscipline(subjectAndTeacher);
            },
            function(res) {
                $scope.$root['ModalController'].confirmYesAndNo("Преподаватель уже внёс изменения в ведомость по данному предмету! " +
                    "Вы уверены, что хотите удалить?", "Да", "Нет", 'btn-outline-danger', 'btn-outline-info',
                    function () {
                        deleteDiscipline(subjectAndTeacher);
                        $scope.toasterSuccess("Запись удалена");
                    }
                );
                // console.log(res.data);
                // console.log(res.status);
            }
        );
    };

    function deleteDiscipline(subjectAndTeacher) {
        $http({
            method: 'PATCH',
            url: '/deleteDiscipline',
            params: {
                certificationAttestationId: subjectAndTeacher.id
            }
        }).then(
            function(res) {
                $scope.subjectsAndTeachers = res.data;
                $scope.toasterSuccess('Предмет успешно удалён!');
            },
            function(res) {
                $scope.toasterError('При удалении предмета произошла ошибка! Обратитесь к администратору');
            }
        );
    }

    /*$scope.openModal = function () {
        $scope.$root['ModalController'].openModal("rtyu", function () {
            console.log("yes")
        }, function () {
            console.log("no")
        });
    }*/

    /*$scope.clearTeacher = function () {
        $scope.teachers.selected = undefined;
    };*/

    /*var vm = this;

    vm.disabled = undefined;
    vm.searchEnabled = undefined;

    vm.setInputFocus = function () {
        $scope.$broadcast('UiSelectDemo1');
    };

    vm.enable = function () {
        vm.disabled = false;
    };

    vm.disable = function () {
        vm.disabled = true;
    };

    vm.enableSearch = function () {
        vm.searchEnabled = true;
    };

    vm.disableSearch = function () {
        vm.searchEnabled = false;
    };

    vm.clear = function () {
        vm.person.selected = undefined;
        vm.address.selected = undefined;
        vm.country.selected = undefined;
    };

    vm.someGroupFn = function (item) {

        if (item.name[0] >= 'A' && item.name[0] <= 'M')
            return 'From A - M';

        if (item.name[0] >= 'N' && item.name[0] <= 'Z')
            return 'From N - Z';

    };

    vm.firstLetterGroupFn = function (item) {
        return item.name[0];
    };

    vm.reverseOrderFilterFn = function (groups) {
        return groups.reverse();
    };

    vm.personAsync = {selected: "wladimir@email.com"};
    vm.peopleAsync = [];

    $timeout(function () {
        vm.peopleAsync = [
            {name: 'Adam', email: 'adam@email.com', age: 12, country: 'United States'},
            {name: 'Amalie', email: 'amalie@email.com', age: 12, country: 'Argentina'},
            {name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina'},
            {name: 'Adrian', email: 'adrian@email.com', age: 21, country: 'Ecuador'},
            {name: 'Wladimir', email: 'wladimir@email.com', age: 30, country: 'Ecuador'},
            {name: 'Samantha', email: 'samantha@email.com', age: 30, country: 'United States'},
            {name: 'Nicole', email: 'nicole@email.com', age: 43, country: 'Colombia'},
            {name: 'Natasha', email: 'natasha@email.com', age: 54, country: 'Ecuador'},
            {name: 'Michael', email: 'michael@email.com', age: 15, country: 'Colombia'},
            {name: 'Nicolás', email: 'nicole@email.com', age: 43, country: 'Colombia'}
        ];
    }, 3000);

    vm.counter = 0;
    vm.onSelectCallback = function (item, model) {
        vm.counter++;
        vm.eventResult = {item: item, model: model};
    };

    vm.removed = function (item, model) {
        vm.lastRemoved = {
            item: item,
            model: model
        };
    };

    vm.tagTransform = function (newTag) {
        var item = {
            name: newTag,
            email: newTag.toLowerCase() + '@email.com',
            age: 'unknown',
            country: 'unknown'
        };

        return item;
    };

    vm.peopleObj = {
        '1': {name: 'Adam', email: 'adam@email.com', age: 12, country: 'United States'},
        '2': {name: 'Amalie', email: 'amalie@email.com', age: 12, country: 'Argentina'},
        '3': {name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina'},
        '4': {name: 'Adrian', email: 'adrian@email.com', age: 21, country: 'Ecuador'},
        '5': {name: 'Wladimir', email: 'wladimir@email.com', age: 30, country: 'Ecuador'},
        '6': {name: 'Samantha', email: 'samantha@email.com', age: 30, country: 'United States'},
        '7': {name: 'Nicole', email: 'nicole@email.com', age: 43, country: 'Colombia'},
        '8': {name: 'Natasha', email: 'natasha@email.com', age: 54, country: 'Ecuador'},
        '9': {name: 'Michael', email: 'michael@email.com', age: 15, country: 'Colombia'},
        '10': {name: 'Nicolás', email: 'nicolas@email.com', age: 43, country: 'Colombia'}
    };

    vm.person = {};

    vm.person.selectedValue = vm.peopleObj[3];
    vm.person.selectedSingle = 'Samantha';
    vm.person.selectedSingleKey = '5';
    // To run the demos with a preselected person object, uncomment the line below.
    //vm.person.selected = vm.person.selectedValue;

    vm.people = [
        {name: 'Adam', email: 'adam@email.com', age: 12, country: 'United States'},
        {name: 'Amalie', email: 'amalie@email.com', age: 12, country: 'Argentina'},
        {name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina'},
        {name: 'Adrian', email: 'adrian@email.com', age: 21, country: 'Ecuador'},
        {name: 'Wladimir', email: 'wladimir@email.com', age: 30, country: 'Ecuador'},
        {name: 'Samantha', email: 'samantha@email.com', age: 30, country: 'United States'},
        {name: 'Nicole', email: 'nicole@email.com', age: 43, country: 'Colombia'},
        {name: 'Natasha', email: 'natasha@email.com', age: 54, country: 'Ecuador'},
        {name: 'Michael', email: 'michael@email.com', age: 15, country: 'Colombia'},
        {name: 'Nicolás', email: 'nicolas@email.com', age: 43, country: 'Colombia'}
    ];

    vm.availableColors = ['Red', 'Green', 'Blue', 'Yellow', 'Magenta', 'Maroon', 'Umbra', 'Turquoise'];

    vm.singleDemo = {};
    vm.singleDemo.color = '';
    vm.multipleDemo = {};
    vm.multipleDemo.colors = ['Blue', 'Red'];
    vm.multipleDemo.colors2 = ['Blue', 'Red'];
    vm.multipleDemo.selectedPeople = [vm.people[5], vm.people[4]];
    vm.multipleDemo.selectedPeople2 = vm.multipleDemo.selectedPeople;
    vm.multipleDemo.selectedPeopleWithGroupBy = [vm.people[8], vm.people[6]];
    vm.multipleDemo.selectedPeopleSimple = ['samantha@email.com', 'wladimir@email.com'];
    vm.multipleDemo.removeSelectIsFalse = [vm.people[2], vm.people[0]];

    vm.appendToBodyDemo = {
        remainingToggleTime: 0,
        present: true,
        startToggleTimer: function () {
            var scope = vm.appendToBodyDemo;
            var promise = $interval(function () {
                if (scope.remainingTime < 1000) {
                    $interval.cancel(promise);
                    scope.present = !scope.present;
                    scope.remainingTime = 0;
                } else {
                    scope.remainingTime -= 1000;
                }
            }, 1000);
            scope.remainingTime = 3000;
        }
    };
    vm.addPerson = function (item, model) {
        if (item.hasOwnProperty('isTag')) {
            delete item.isTag;
            vm.people.push(item);
        }
    }*/

    /*$scope.clear = function() {
        $scope.person.selected = undefined;
        // $scope.address.selected = undefined;
    };

    $scope.person = {};
    $scope.people = [
        { name: 'Adam',      email: 'adam@email.com',      age: 10 },
        { name: 'Amalie',    email: 'amalie@email.com',    age: 12 },
        { name: 'Wladimir',  email: 'wladimir@email.com',  age: 30 },
        { name: 'Samantha',  email: 'samantha@email.com',  age: 31 },
        { name: 'Estefanía', email: 'estefanía@email.com', age: 16 },
        { name: 'Natasha',   email: 'natasha@email.com',   age: 54 },
        { name: 'Nicole',    email: 'nicole@email.com',    age: 43 },
        { name: 'Adrian',    email: 'adrian@email.com',    age: 21 }
    ];*/

    /*$scope.address = {};
    $scope.refreshAddresses = function(address) {
        var params = {address: address, sensor: false};
        return $http.get(
            'http://maps.googleapis.com/maps/api/geocode/json',
            {params: params}
        ).then(function(response) {
            $scope.addresses = response.data.results
        });
    };*/

});