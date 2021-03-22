app.controller("ViewAttestationsDeputyDeanController", function($scope, $http) {

    getActiveAttestationByFacultyIdForDeputyDean();
    function getActiveAttestationByFacultyIdForDeputyDean() {
        $http({
            method: 'GET',
            url: '/getActiveAttestationByFacultyIdForDeputyDean'
        }).then(
            function(res) { // success
                $scope.allAttestations = res.data;
            }
        );
    }

});