app.controller("AssignmentController", function($scope, $http) {
    $scope.teachers = [];
    $scope.changes = [];
    $scope.departments = {};
    $scope.oldDepartments = {};

    getDepartmentsByFacultyId();
    function getDepartmentsByFacultyId() {
        $http({
            method: 'PATCH',
            url: '/getDepartmentsByFacultyId'
        }).then(
            function(res) {
                $scope.departments = res.data;
                $scope.oldDepartments = angular.copy($scope.departments);

                $scope.departments.forEach(function(item, index) { /*item.headDepartment=*/
                    getTeachersByDepartmentId(item.id, index);
                });
                // console.log($scope.departments);
                // console.log('$scope.teachers', $scope.teachers);
            }
        );
    }

    function getTeachersByDepartmentId(departmentId, index) {
        $http({
            method: 'PATCH',
            url: '/getTeachersByDepartmentId',
            params: { departmentId: departmentId }
        }).then(
            function(res) {
                $scope.teacher = {};
                $scope.teachers[index] = res.data;
                checkChanges(index);
                // return $scope.teachers[index];
                // $scope.departments[index].teachers[index] = res.data;
            }
        );
    }

    function checkChanges(index) {
        $scope.changes[index] = !angular.equals($scope.departments[index], $scope.oldDepartments[index]);
        // console.log($scope.departments[index])
        // console.log($scope.oldDepartments[index])
    }

    $scope.selectedHeadDepartment = function (index) {
        checkChanges(index);
    };

    $scope.saveHeadDepartment = function (index) {
        if (!$scope.changes[index]) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveHeadDepartment',
            data: angular.toJson($scope.departments[index]),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Зав. каф. успешно назначен');
                $scope.oldDepartments[index] = angular.copy(res.data);
                checkChanges(index);
            }
        );
    }

});