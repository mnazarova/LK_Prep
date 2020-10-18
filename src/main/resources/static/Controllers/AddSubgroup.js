app.controller("AddSubgroupController", function($scope, $state, $http) {

    getAllFaculties();
    function getAllFaculties() {
        $http.get('/getAllFaculties')
            .then(
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

    $scope.addStudents = function() {
        if(!$scope.group) {
            $scope.toasterWarning('Необходимо выбрать группу');
            return null;
        }
        $http({
            method: 'PATCH',
            url: '/findNotExpelledStudents',
            data: angular.toJson($scope.group),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {

                $scope.commonListStudents = $scope.commonListStudents || [];
                var isRepeated = false;
                for (var i=0, n=res.data.length;i<n;i++) {
                    var curStudent = res.data[i];
                    var index1 = $scope.commonListStudents.findIndex(function (value) {
                        return +value.id === +curStudent.id;
                    });
                    var index2 = $scope.listSelectedStudents.findIndex(function (value) {
                        return +value.id === +curStudent.id;
                    });
                    if(+index1 === -1 && +index2 === -1) // студента нет ни в одном списке
                        $scope.commonListStudents.push(res.data[i]);
                    else
                        isRepeated = true;
                }
                if(isRepeated)
                    $scope.toasterWarning("Студенты, которые уже присутствуют в одном из списков, повторно добавлены не были!");

            },
            function(res) {
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };


    $scope.checkExistenceAdded = function() {
        $scope.existenceAdded = false;
        $scope.commonListStudents.forEach(function (value) {
            if(value.add)
                $scope.existenceAdded = true;
        });
    };

    $scope.listSelectedStudents = $scope.listSelectedStudents || [];
    $scope.sendStudents = function() {
        for(var i=$scope.commonListStudents.length-1;i>=0;i--)
            if($scope.commonListStudents[i].add) {
                delete $scope.commonListStudents[i].add;
                $scope.listSelectedStudents.push($scope.commonListStudents[i]);
                $scope.commonListStudents.splice(i,1);
            }
        $scope.checkExistenceAdded();
        /*$scope.commonListStudents.forEach(function (value, index) {
            if(value.add) {
                $scope.listSelectedStudents.push(value);
                $scope.commonListStudents.splice(index,1);
                console.log(index)
            }
        });*/
    };

    $scope.revert = function(index) {
        /*var index = $scope.listSelectedStudents.findIndex(function (value) {
            return +value.id === +studentId;
        });*/
        $scope.commonListStudents.push($scope.listSelectedStudents[index]);
        $scope.listSelectedStudents.splice(index,1);
    };

    $scope.createSubgroup = function() {
        if(!createForm.nameSubgroup.value) {
            $scope.showCreateForm = true;
            $scope.toasterWarning("Необходимо ввести название новой подгруппы" +
                "  (например, 'И973_2' или 'И973(5)')");
            return;
        }
        $scope.subgroup = {
            name: createForm.nameSubgroup.value,
            students: $scope.listSelectedStudents
        };
        $http({
            method: 'PATCH', // если падает 400
            url: '/createSubgroup',
            data: angular.toJson($scope.subgroup),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Подгруппа успешно создана');
                $state.go('link');
            },
            function(res) {
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };

});