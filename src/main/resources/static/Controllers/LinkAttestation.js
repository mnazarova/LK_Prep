app.controller("LinkAttestationController", function($scope, $http) {

    $scope.checkRole("SECRETARY");

    getActingAttestation();
    function getActingAttestation() {
        $http({
            method: 'PATCH',
            url: '/getActingAttestation'
        }).then(
            function(res) { // success
                $scope.attestations = res.data;

                $scope.attestation = {};

                $scope.attestation.selected = res.data[0];
                $scope.changeAttestationSelected();
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
                attestationId: $scope.attestation.selected.id
            }
        }).then(
            function(res) {
                $scope.groups = [];
                $scope.groups.push(res.data);
            }
        );
    };

    $scope.changeAttestationSelected = function () {
        if(!$scope.attestation.selected.id)
            return;
        $http({
            method: 'PATCH',
            url: '/changeAttestationSelected',
            params: {
                attestationId: $scope.attestation.selected.id
            }
        }).then(
            function(res) {
                $scope.groupList = res.data;
                $scope.groups = res.data;
            }
        );
    };


    // attestationGetGroupListByActiveIsTrue();
    /*function attestationGetGroupListByActiveIsTrue() { вместо этого метода changeAttestationSelected
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