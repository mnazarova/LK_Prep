app.controller("StatementsController", function($scope, $http) {

    getAllAttestation();
    function getAllAttestation() {
        $http({
            method: 'GET',
            url: '/getAllAttestation'
        }).then(
            function(res) { // success
                $scope.allAttestations = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    getWorkingAttestation();
    function getWorkingAttestation() { // только действующие
        $http({
            method: 'GET',
            url: '/getWorkingAttestation'
        }).then(
            function(res) { // success
                $scope.attestations = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

});