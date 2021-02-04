app.controller("AppointIdController", function($scope, $http, $stateParams) {
    $scope.showStudents = [];

    getGroupById();
    function getGroupById() {
        $http({
            method: 'PATCH',
            url: '/getGroupById',
            params: {
                groupId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.group = res.data;
            }
        );
    }

    getSyllabusContentListByGroupIdAndDepartmentId();
    function getSyllabusContentListByGroupIdAndDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/getSyllabusContentListByGroupIdAndDepartmentId',
            params: {
                groupId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.syllabusContentList = res.data;
                console.log($scope.syllabusContentList)
            }
        );
    }

    getTeacherListByDepartmentId();
    function getTeacherListByDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/getTeacherListByDepartmentId'
        }).then(
            function(res) {
                $scope.teachers = res.data;
                $scope.teacher = {};
                console.log($scope.teachers)
            }
        );
    }

    $scope.toggleShowStudents = function (index) {
        $scope.showStudents[index] = !$scope.showStudents[index];
    };

    $scope.saveTeachersByDiscipline = function (index) {
        console.log(index)
    }

    /*$scope.selectedOnGroupList = function () {
        // if(!$scope.statementsForm.group.$modelValue)
        //     return;
        $http({
            method: 'PATCH',
            url: '/selectedOnGroupList',
            params: {
                id: $scope.statementsForm.group.$modelValue.id,
                groupId: $scope.forms.statementsForm.group.$modelValue.id
            }
        }).then(
            function(res) {
                $scope.groups = res.data;
            }
        );
    };*/

});