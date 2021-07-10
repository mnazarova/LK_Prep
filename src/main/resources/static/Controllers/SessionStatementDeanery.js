app.controller("SessionStatementDeaneryController", function($scope, $state, $stateParams, $http) {

    $scope.checkRole("DEANERY");

    $scope.groupId = $stateParams.groupId;

    $scope.selected = {
        id: '',
        name: ''
    };

    $scope.selectedAdmittance = [
        { id: 6, name: 'Сдано' },
        { id: 7, name: 'Не сдано' }
    ];

    getSelectedContentSessionSheet();
    function getSelectedContentSessionSheet() {
        $http({
            method: 'PATCH',
            url: '/getSelectedContentSessionSheet',
            params: { sessionSheetId: $stateParams.sessionSheetId }
        }).then(
            function(res) {
                $scope.selected = res.data;
                // console.log($scope.selected);
            }
        );
    }

    getContentSessionSheetForDeanery();
    function getContentSessionSheetForDeanery() {
        $http({
            method: 'PATCH',
            url: '/getContentSessionSheetForDeanery',
            params: {
                groupId: $stateParams.groupId,
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
                $scope.isAdditional = $scope.contentSession[0].sessionSheet.isAdditional;
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    if (res.data === 1) {
                        $scope.toasterError('Выбранная группа недоступна для просмотра!');
                        $state.go('sessionGroupsDeanery');
                        // $state.go('sessionStatementsDeanery', {groupId: $stateParams.groupId});
                    }
                    else
                        if (res.data === 2) {
                            $scope.toasterError('Выбранная ведомость недоступна для просмотра!');
                            $state.go('sessionStatementsDeanery', {groupId: $stateParams.groupId});
                        }
            }
        );
    }
});