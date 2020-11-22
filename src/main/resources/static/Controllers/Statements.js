app.controller("StatementsController", function($scope, $http, $state) {

    setActiveFalseForAttestation();
    function setActiveFalseForAttestation() {
        $http({
            method: 'POST',
            url: '/setActiveFalseForAttestation'
        }).then(
            function(res) {}
        );
    }

    getAllAttestation();
    function getAllAttestation() {
        $http({
            method: 'GET',
            url: '/getAllAttestation'
        }).then(
            function(res) { // success
                $scope.allAttestations = res.data;
                getWorkingAttestation($scope.allAttestations);
            },
            function(res) {
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function getWorkingAttestation(allAttestations) { // только действующие
        $http({
            method: 'GET',
            url: '/getWorkingAttestation'
        }).then(
            function(res) { // success
                $scope.attestations = res.data;
                /*$scope.attestations.forEach(function(item, index) {
                    // console.log(item, index);
                    var findIndex = allAttestations.findIndex(function(value){return +value.id === +item.id;});
                    // console.log(allAttestations[findIndex])
                    allAttestations[findIndex].actually = true;
                });*/
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    $scope.transitionToStatementId = function (attestationId) {
        $state.go('statementId', {id: attestationId});
    }

});