app.controller("ListAttestationController", function($scope, $http) {

    $scope.checkRole("SECRETARY");

    getActingAttestation();
    function getActingAttestation() {
        $http({
            method: 'PATCH',
            url: '/getActingAttestation'
        }).then(
            function(res) { // success
                $scope.attestations = res.data;
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                // $state.go("help");
            }
        );
    }

});