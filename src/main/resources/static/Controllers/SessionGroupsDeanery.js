app.controller("SessionGroupsDeaneryController", function($scope, $http) {

    $scope.checkRole("DEANERY");

    getSessionGroupsByDeanery();
    function getSessionGroupsByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getSessionGroupsByDeanery',
            params: { groupId: '' }
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                $scope.groupList = res.data;
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
            }
        )
    }

    $scope.selectedOnGroupList = function () {
        $http({
            method: 'PATCH',
            url: '/getSessionGroupsByDeanery',
            params: {
                groupId: $scope.sessionGroupsForm.group.$modelValue.id
            }
        }).then(
            function(res) {
                $scope.groups = [];
                $scope.groups.push(res.data);
            }
        );
    };

});