app.controller("LinkSessionController", function($scope, $http) {

    sessionGetGroupListByActiveIsTrue();
    function sessionGetGroupListByActiveIsTrue() {
        $http({
            method: 'PATCH',
            url: '/sessionGetGroupListByActiveIsTrue'
        }).then(
            function(res) {
                $scope.groupList = res.data;
                $scope.groups = res.data;
                // console.log($scope.groups)
            }
        );
    }

    $scope.sessionSelectedGroup = function () {
        // console.log($scope.linkSessionForm.group.$modelValue.id)
        if(!$scope.linkSessionForm.group.$modelValue)
            return;
        $http({
            method: 'PATCH',
            url: '/sessionSelectedGroup',
            params: {
                groupId: $scope.linkSessionForm.group.$modelValue.id
            }
        }).then(
            function(res) {
                $scope.groups = [];
                $scope.groups.push(res.data);
            }
        );
    };

});