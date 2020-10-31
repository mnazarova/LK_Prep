//angular.module("Orders",[])
    app.controller("ContentAttestationController", function($scope, $state, $stateParams, $http) {

        $scope.attestationId = $stateParams.attestationId;

        getContentAttestation();
        function getContentAttestation() {
            $http({
                method: 'PATCH',
                url: '/getContentAttestation',
                params: { id: $stateParams.id }
            }).then(
                function(res) { // success
                    $scope.contentAttestation = res.data;
                    // console.log($scope.contentAttestation);
                },
                function(res) { // error
                    // console.log("Error: " + res.status + " : " + res.data);
                    $state.go("attestation");
                }
            );
        }

        $scope.replyAll = function() {
            getContentAttestation();
        };

        $scope.saveContentAttestation = function() {
            // console.log($scope.contentAttestation)
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
                    // $scope.initChanges();
                },
                function(res) {
                    // console.log("Error: " + res.status + " : " + res.data);
                    $scope.toasterError('', 'Данные не были сохранены');
                }
            );
        };

    });