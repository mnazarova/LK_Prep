app.controller("StatementsController", function($scope, $http, $state) {

    getAllAttestation();
    function getAllAttestation() {
        $http({
            method: 'GET',
            url: '/getAllAttestation'
        }).then(
            function(res) { // success
                $scope.allAttestations = res.data;
                $scope.numberAttestation = 0;
                $scope.allAttestations.forEach(function (item) {
                    // console.log(item, index);
                    if (item.active)
                        $scope.numberAttestation++; // только действующие
                });
            },
            function(res) {
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    $scope.transitionToStatementId = function (attestationId) {
        $state.go('statementId', {id: attestationId});
    }

});