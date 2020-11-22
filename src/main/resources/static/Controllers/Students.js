app.controller("StudentsController", function($scope, $http) {

    /*getAllFaculties();
    function getAllFaculties() {
        $http.get('/getAllFaculties')
            .then(
        /!*$http({
            method: 'GET',
            url: '/getAllFaculties'
        }).then(*!/
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
    };*/

    getGroupListByDeanery();
    function getGroupListByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getGroupListByDeanery'
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                // console.log($scope.groupList)
            }
        );
    }

    /*$scope.initChanges = function () {
        $scope.oldExpelled = angular.copy($scope.expelledArray());
        $scope.checkChanges();
    };

    $scope.checkChanges = function () {
        $scope.changes = angular.equals($scope.oldExpelled, $scope.expelledArray());
    };

    $scope.expelledArray = function () {
        var array = [];
        $scope.students.forEach(function(item, index) {
            console.log(item, index);
            // array.push(item.expelled);
        });
        console.log(array);
        return array;
    };*/

    $scope.findStudents = function() {
        if(!$scope.studentsForm.group.$modelValue.id/*$scope.group*/) {
            $scope.toasterWarning('Необходимо выбрать группу');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/findStudents',
            data: angular.toJson($scope.studentsForm.group.$modelValue.id),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) { // success
                $scope.students = res.data;
                $scope.studentsCopy = angular.copy($scope.students);
                // console.log($scope.students)
            }
        );
    };

    $scope.saveExpelledStudents = function() {
        if (angular.equals($scope.studentsCopy, $scope.students)) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveExpelledStudents',
            data: angular.toJson($scope.students),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.studentsCopy = angular.copy($scope.students);
                // $scope.initChanges();
            }
        );
    };

});