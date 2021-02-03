app.controller("AppointController", function($scope, $http) {

    getGroupListByActiveIsTrue();
    function getGroupListByActiveIsTrue() {
        $http({
            method: 'PATCH',
            url: '/getGroupListByActiveIsTrue'
        }).then(
            function(res) {
                $scope.groupList = res.data;
                $scope.groups = res.data;
                // console.log($scope.groups)
            }
        );
    }

    $scope.selectedGroup = function () {
        console.log($scope.appointForm.group.$modelValue.id)
        if(!$scope.appointForm.group.$modelValue)
            return;
        $http({
            method: 'PATCH',
            url: '/selectedGroup',
            params: {
                groupId: $scope.appointForm.group.$modelValue.id
            }
        }).then(
            function(res) {
                $scope.groups = [];
                $scope.groups.push(res.data);
            }
        );
    };

});