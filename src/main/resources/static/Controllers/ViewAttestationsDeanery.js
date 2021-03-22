app.controller("ViewAttestationsDeaneryController", function($scope, $http/*, $state*/) {

    getActiveAttestationByFacultyId();
    function getActiveAttestationByFacultyId() {
        $http({
            method: 'GET',
            url: '/getActiveAttestationByFacultyId'
        }).then(
            function(res) { // success
                $scope.allAttestations = res.data;
                /*$scope.numberAttestation = 0;
                $scope.allAttestations.forEach(function (item) {
                    if (item.active) $scope.numberAttestation++; // только действующие
                });*/
            }
        );
    }
    //$scope.transitionToStatementId = function (attestationId) { $state.go('attestationStatementsDeanery', {id: attestationId});}

});