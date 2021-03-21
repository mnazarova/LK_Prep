app.controller("AttestationStatementDeputyDeanController", function($scope, $state, $stateParams, $http) {

    $scope.attestationId = $stateParams.attestationId;
    $scope.selected = [
        { key: true, value: '+' },
        { key: false, value: '-' }
    ];

    getContentAttestationForDeputyDean();
    function getContentAttestationForDeputyDean() {
        $http({
            method: 'PATCH',
            url: '/getContentAttestationForDeputyDean',
            params: {
                id: $stateParams.certificationAttestationId,
                attestationId: $stateParams.attestationId
            }
        }).then(
            function(res) { // success
                $scope.contentAttestation = res.data;

                if (!$scope.contentAttestation.length) // == 0
                    return;
                $scope.groupNumber = $scope.contentAttestation[0].certificationAttestation.group.number;
                $scope.attestation = $scope.contentAttestation[0].certificationAttestation.attestation;
                $scope.disciplineName = $scope.contentAttestation[0].certificationAttestation.syllabusContent.discipline.name;
                // $scope.contentAttestation[0].dateWorks = $scope.deadline;
                // console.log($scope.contentAttestation);
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    if (res.data === 1) {
                        $scope.toasterError('Выбранная ведомость не доступна для просмотра!');
                        $state.go('attestationStatementsDeanery', {id: $scope.attestationId});
                    }
                    else
                        if (res.data === 2)
                            $scope.toasterError('Выбранная ведомость не относится к текущей аттестации!');
                // console.log("Error: " + res.status + " : " + res.data);
                // $state.go("attestation");
            }
        );
    }

});