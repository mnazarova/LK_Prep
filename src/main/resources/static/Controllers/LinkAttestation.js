app.controller("LinkAttestationController", function($scope, $http) {

    attestationGetGroupListByActiveIsTrue();
    function attestationGetGroupListByActiveIsTrue() {
        $http({
            method: 'PATCH',
            url: '/attestationGetGroupListByActiveIsTrue'
        }).then(
            function(res) {
                $scope.groupList = res.data;
                $scope.groups = res.data;
            }
        );
    }

    getActingAttestation();
    function getActingAttestation() {
        $http({
            method: 'PATCH',
            url: '/getActingAttestation'
        }).then(
            function(res) { // success
                $scope.attestations = res.data;
                $scope.attestation = {};
            }
        );
    }

    $scope.attestationSelectedGroup = function () {
        // console.log($scope.linkAttestationForm.group.$modelValue.id)
        if(!$scope.linkAttestationForm.group.$modelValue)
            return;
        $http({
            method: 'PATCH',
            url: '/attestationSelectedGroup',
            params: {
                groupId: $scope.linkAttestationForm.group.$modelValue.id
            }
        }).then(
            function(res) {
                $scope.groups = [];
                $scope.groups.push(res.data);
            }
        );
    };

});