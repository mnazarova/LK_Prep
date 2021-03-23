app.controller("DeputyDeanSessionStatementController", function($scope, $state, $stateParams, $http) {

    $scope.checkRole("DEPUTY_DEAN");

    $scope.selected = {
        id: '',
        name: ''
    };

    $scope.selectedAdmittance = [
        { id: 6, name: 'Сдано' },
        { id: 7, name: 'Не сдано' }
    ];

    getSelectedContentSessionSheetForDeputyDean();
    function getSelectedContentSessionSheetForDeputyDean() {
        $http({
            method: 'PATCH',
            url: '/getSelectedContentSessionSheetForDeputyDean',
            params: { sessionSheetId: $stateParams.sessionSheetId }
        }).then(
            function(res) {
                $scope.selected = res.data;
                // console.log($scope.selected);
            }
        );
    }

    getContentSessionSheetForDeputyDean();
    function getContentSessionSheetForDeputyDean() {
        $http({
            method: 'PATCH',
            url: '/getContentSessionSheetForDeputyDean',
            params: {
                sessionSheetId: $stateParams.sessionSheetId,
            }
        }).then(
            function(res) { // success
                $scope.contentSession = res.data;

                if (!$scope.contentSession.length) // == 0
                    return;
                $scope.groupNumber = $scope.contentSession[0].sessionSheet.group.number;
                $scope.disciplineName = $scope.contentSession[0].sessionSheet.syllabusContent.discipline.name;
                $scope.splitAttestationForm = $scope.contentSession[0].sessionSheet.splitAttestationForm;
                $scope.deadlineDiscipline = $scope.contentSession[0].sessionSheet.syllabusContent.deadline;
                // console.log($scope.contentSession);
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    if (res.data === 1) {
                        $scope.toasterError('Выбранная ведомость не доступна для просмотра!');
                        $state.go('sessionStatementsDeanery');
                    }
                    /*else
                        if (res.data === 2)
                            $scope.toasterError('Выбранная ведомость не относится к текущей аттестации!');*/
            }
        );
    }
});