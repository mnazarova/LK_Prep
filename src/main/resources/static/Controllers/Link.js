app.controller("LinkController", function($scope, $http) {

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
                    },
                    function(res) { // error
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
                        if (!$scope.students.length)
                            $scope.toasterWarning('В выбранной подгруппе нет студентов или все были отчислены');
                    },
                    function(res) { // error
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
            }
        }

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
        $scope.viewTeacherForm = false;
    };

    $scope.checkPartNameSubgroup = function() {
        $scope.subgroups = null;
        $scope.viewTeacherForm = false;
    };

    $scope.checkSubgroupId = function() {
        $scope.students = null;
        $scope.viewTeacherForm = false;
    };

    $scope.checkGroup = function() {
        $scope.students = null;
        $scope.viewTeacherForm = false;
    };

    $scope.addDiscipline = function() {
        $scope.viewTeacherForm = true;
        getAttestation(); //
        getGroupsOfSelectedStudents(); //
        // getDisciplines
    };


    function getAttestation() {
        $http({
            method: 'GET',
            url: '/getAttestation'
        }).then(
            function(res) {
                $scope.attestations = res.data;
                $scope.attestation = res.data[0].id;
            },
            function(res) {
                // console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function getGroupsOfSelectedStudents() {
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
                $scope.semesterNumber = res.data;
                console.log($scope.semesterNumber)
            }
        );
    }

    $scope.getDisciplines = function (selectedGroupId, selectedSemesterNumber) {
        console.log($scope.semesterNumber)
        $http({
            method: 'PATCH',
            url: '/getDisciplines',
            params: {
                groupId: selectedGroupId,
                semesterNumber: selectedSemesterNumber
            }
        }).then(
            function(res) {
                $scope.disciplines = res.data;
                console.log($scope.disciplines)
            }
        );
    }

});