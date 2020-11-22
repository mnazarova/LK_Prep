app.controller("ContentAttestationController", function($scope, $state, $stateParams, $http) {

    $scope.attestationId = $stateParams.attestationId;
    $scope.selected = [
        { key: true, value: '+' },
        { key: false, value: '-' }
    ];

    getContentAttestation();
    function getContentAttestation() {
        $http({
            method: 'PATCH',
            url: '/getContentAttestation',
            params: {
                id: $stateParams.id,
                attestationId: $stateParams.attestationId
            }
        }).then(
            function(res) { // success
                $scope.contentAttestation = res.data;
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);
                // console.log($scope.contentAttestation);
            },
            function(res) { // error
                // console.log("Error: " + res.status + " : " + res.data);
                $state.go("attestation");
            }
        );
    }

    $scope.replyAll = function() {
        if (angular.equals($scope.contentAttestationCopy, $scope.contentAttestation))
            $scope.toasterWarning('Изменения отсутствуют');
        else
            $scope.contentAttestation = angular.copy($scope.contentAttestationCopy);
            // getContentAttestation();
    };

    $scope.saveContentAttestation = function() {
        if (angular.equals($scope.contentAttestationCopy, $scope.contentAttestation)) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveContentAttestation',
            data: angular.toJson($scope.contentAttestation),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);
            },
            function(res) {
                $scope.toasterError('', 'Данные не были сохранены');
            }
        );
    };

});