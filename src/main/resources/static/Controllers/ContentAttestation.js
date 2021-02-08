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
                certificationAttestationId: $stateParams.id,
                attestationId: $stateParams.attestationId
            }
        }).then(
            function(res) { // success
                $scope.contentAttestation = res.data;
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);

                $scope.groupNumber = $scope.contentAttestation[0].certificationAttestation.group.number;
                $scope.attestation = $scope.contentAttestation[0].certificationAttestation.attestation;
                $scope.disciplineName = $scope.contentAttestation[0].certificationAttestation.syllabusContent.discipline.name;
                // console.log($scope.contentAttestation);
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    $state.go('subject', {id: $stateParams.attestationId});
                    // $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');
            }
        );
    }

    /*$scope.replyAll = function() {
        if (angular.equals($scope.contentAttestationCopy, $scope.contentAttestation))
            $scope.toasterWarning('Изменения отсутствуют');
        else
            $scope.contentAttestation = angular.copy($scope.contentAttestationCopy);
    };*/

    $scope.checkChanges = function () {
        $scope.changes = !angular.equals($scope.contentAttestationCopy, $scope.contentAttestation); // true если равны
        // console.log($scope.contentAttestationCopy)
        // console.log($scope.contentAttestation)
    };

    $scope.saveContentAttestation = function() {
        if (!$scope.changes) {
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
                // console.log(res.data)
                $scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.contentAttestation = angular.copy(res.data);
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);
                $scope.checkChanges();
            },
            function(res) {
                $scope.toasterError('', 'Данные не были сохранены');
            }
        );
    };

});