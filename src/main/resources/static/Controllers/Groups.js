app.controller("GroupsController", function($scope, $http, $filter) {

    $scope.minDate = new Date().setDate(new Date().getDate()+1);
    $scope.deadline = new Date(new Date($scope.minDate).toISOString().split("T")[0]);
    $scope.deadline.setHours(0);

    // $scope.groupsForm = {};
    $scope.semesterNumberSet = [];
    $scope.estimatedCurSemesterNumber = [];

    $scope.changes = [];
    $scope.groups = {};
    $scope.oldGroups = {};

    getGroupsByDeaneryId();
    function getGroupsByDeaneryId() {
        $http({
            method: 'PATCH',
            url: '/getGroupsByDeaneryId'
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                $scope.oldGroups = angular.copy(res.data);
                console.log($scope.groups)
                $scope.groups.forEach(function(item, index) {
                    getSemesterNumberSetByGroupId(item.id, index);
                    getEstimatedCurSemesterNumber(item.id, index);
                    // console.log('before', item.deadline)
                    if (item.deadline)
                        item.deadline = new Date(item.deadline);
                    // console.log('after', item.deadline)
                });
            }
        );
    }

    function getSemesterNumberSetByGroupId(groupId, index) {
        $http({
            method: 'PATCH',
            url: '/getSemesterNumberSetByGroupId',
            params: { groupId: groupId }
        }).then(
            function(res) {
                $scope.semesterNumberSet[index] = res.data;
            }
        );
    }

    function getEstimatedCurSemesterNumber(groupId, index) {
        $http({
            method: 'PATCH',
            url: '/getEstimatedCurSemesterNumber',
            params: { groupId: groupId }
        }).then(
            function(res) {
                $scope.estimatedCurSemesterNumber[index] = res.data;
            }
        );
    }

    function checkChanges(index) {
        $scope.changes[index] = !angular.equals($scope.groups[index], $scope.oldGroups[index]);
        console.log($scope.groups[index])
        console.log($scope.oldGroups[index])
    }

    $scope.changeActive = function (index) {
        checkChanges(index);
    };

    $scope.selectedCurSemester = function (index) {
        checkChanges(index);
    };

    $scope.selectedDeadline = function (index) {
        checkChanges(index);
        console.log($scope.groupsForm)
    };

    $scope.saveGroup = function (index) {
        if (!$scope.changes[index]) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        /*if ($scope.groupsForm.deadline[index].$dirty && $scope.groupsForm.deadline[index].$invalid || $scope.groups[index].deadline < $scope.minDate) {
            $scope.toasterWarning('Введена некорректная дата');
            return;
        }*/
        $http({
            method: 'PATCH',
            url: '/saveGroup',
            data: angular.toJson($scope.groups[index]),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Изменения по выбранной группе успешно сохранены');
                if (res.data.deadline)
                    res.data.deadline = new Date(res.data.deadline);
                $scope.oldGroups[index] = angular.copy(res.data);
                checkChanges(index);
            }
        );
    };

    /*$scope.findStudents = function() {
        if(!$scope.studentsForm.group.$modelValue.id/!*$scope.group*!/) {
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
    };*/

});