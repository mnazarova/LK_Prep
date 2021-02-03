app.controller("AppointIdController", function($scope, $http, $stateParams) {
    console.log($stateParams)

    getDisciplineListByGroupIdAndDepartmentId();
    function getDisciplineListByGroupIdAndDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/getDisciplineListByGroupIdAndDepartmentId',
            params: {
                groupId: $scope.$stateParams
            }
        }).then(
            function(res) {
                $scope.disciplineList = res.data;
                console.log($scope.disciplineList)
            }
        );
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