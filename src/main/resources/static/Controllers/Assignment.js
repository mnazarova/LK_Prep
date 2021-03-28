app.controller("AssignmentController", function($scope, $http) {

    $scope.checkRole("DEANERY");

    $scope.teachers = [];

    $scope.changes = [];
    $scope.departments = {};
    $scope.oldDepartments = {};

    $scope.changesDeputyDean = [];
    $scope.departmentsWithGroups = {};
    $scope.oldDepartmentsWithGroups = {};

    // getDepartmentsByFacultyId();
    getDepartmentsWithGroupsByFacultyId();
    function getDepartmentsWithGroupsByFacultyId() {
        $http({
            method: 'PATCH',
            url: '/getDepartmentsWithGroupsByFacultyId'
        }).then(
            function(res) {
                $scope.departments = res.data;
                $scope.oldDepartments = angular.copy($scope.departments);

                $scope.departments.forEach(function(item, index) { /*item.headDepartment=*/
                    getTeachersByDepartmentId(item.id, index);
                });
                // console.log($scope.departments);

                initDepartmentsWithGroups();
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
        $scope.changes[index] = !angular.equals($scope.departments[index], $scope.oldDepartments[index]); // console.log($scope.departments[index]); console.log($scope.oldDepartments[index])
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
                $scope.departmentsWithGroups[index].headDepartment =  $scope.oldDepartmentsWithGroups[index].headDepartment = $scope.oldDepartments[index].headDepartment;
                checkChanges(index);
            }
        );
    };


    /* Deputy Dean */
    function initDepartmentsWithGroups() {
        $scope.departmentsWithGroups = angular.copy($scope.departments);
        for (let index=0, size = Object.keys($scope.departmentsWithGroups).length; index < size; index++) {
            // $scope.setGlobalKrOrKpTeacher(index, $scope.syllabusContentList);
            $scope.setGlobalDeputyDean(index, $scope.departmentsWithGroups);
            $scope.oldDepartmentsWithGroups[index] = angular.copy($scope.departmentsWithGroups[index]);
            $scope.checkChangesDeputyDean(index);
        }
    }

    $scope.checkChangesDeputyDean = function (index) {
        $scope.changesDeputyDean[index] = !angular.equals($scope.departmentsWithGroups[index], $scope.oldDepartmentsWithGroups[index]);
        // console.log($scope.departmentsWithGroups[index])
        // console.log($scope.oldDepartmentsWithGroups[index])
    };

    $scope.selectedDeputyDean = function (index) {
        let setValue = $scope.departmentsWithGroups[index].deputyDean;
        $scope.departmentsWithGroups[index].groupsAssignmentDeputyDean.forEach(function (el) {
            el.deputyDean = setValue;
        });
        $scope.checkChangesDeputyDean(index);
    };
    $scope.setGlobalDeputyDean = function(index, depWithGroupsList) {
        let array = depWithGroupsList[index].groupsAssignmentDeputyDean;
        let length = array.length;
        if (length < 1 || !array[0].deputyDean)
            return;
        let el = array[0].deputyDean.id, i= 1;
        for(; i < length; i++)
            if (!array[i].deputyDean || el !== array[i].deputyDean.id) break;
        if (i === length) depWithGroupsList[index].deputyDean = array[0].deputyDean;
        else depWithGroupsList[index].deputyDean = null;
    };

    $scope.selectedDeputyDeanByGroup = function (index) {
        $scope.setGlobalDeputyDean(index, $scope.departmentsWithGroups);
        $scope.checkChangesDeputyDean(index);
    };

    $scope.saveDeputyDean = function (index) {
        if (!$scope.changesDeputyDean[index]) { $scope.toasterWarning('Изменения отсутствуют'); return; }
        $http({
            method: 'PATCH',
            url: '/saveDeputyDean',
            /*params: {
                groupId: $stateParams.id
            },*/
            data: angular.toJson($scope.departmentsWithGroups[index]),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Зам. декана на выбранной кафедре успешно назначен(ы)');
                $scope.oldDepartmentsWithGroups[index] = angular.copy(res.data);
                $scope.setGlobalDeputyDean(index, $scope.oldDepartmentsWithGroups);
                // console.log($scope.oldDepartmentsWithGroups[index])
                $scope.departments[index].groupsAssignmentDeputyDean = $scope.oldDepartments[index].groupsAssignmentDeputyDean = $scope.oldDepartmentsWithGroups[index].groupsAssignmentDeputyDean;
                $scope.checkChangesDeputyDean(index);
            }
        );
    }

});