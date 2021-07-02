app.controller("LinkAttestationController", function($scope, $http, $stateParams) {

    $scope.checkRole("SECRETARY");
    $scope.attestationId = $stateParams.attestationId;

    getGroupsByAttestationId();
    function getGroupsByAttestationId() {
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationId',
            params: {
                attestationId: $stateParams.attestationId
            }
        }).then(
            function(res) {
                $scope.groupList = res.data;
                $scope.groups = res.data;
            }
        );
    }

    $scope.attestationSelectedGroup = function () {
        if(!$scope.linkAttestationForm.group.$modelValue)
            return;
        $http({
            method: 'PATCH',
            url: '/attestationSelectedGroup',
            params: {
                groupId: $scope.linkAttestationForm.group.$modelValue.id,
                attestationId: $stateParams.attestationId
            }
        }).then(
            function(res) {
                $scope.groups = [];
                $scope.groups.push(res.data);
            }
        );
    };


    // attestationGetGroupListByActiveIsTrue();
    /*function attestationGetGroupListByActiveIsTrue() { вместо этого метода getGroups
        $http({
            method: 'PATCH',
            url: '/attestationGetGroupListByActiveIsTrue'
        }).then(
            function(res) {
                $scope.groupList = res.data;
                $scope.groups = res.data;
            }
        );
    }*/

});