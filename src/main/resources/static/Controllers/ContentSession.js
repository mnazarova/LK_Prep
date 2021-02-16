app.controller("ContentSessionController", function($scope, $state, $stateParams, $http) {

    $scope.isHead = $stateParams.isHead === 'true';
    /*$scope.selected = [
        { key: true, value: '+' },
        { key: false, value: '-' }
    ];*/
    $scope.selected = {
        id: '',
        name: ''
    };

    getSelected();
    function getSelected() {
        $http({
            method: 'PATCH',
            url: '/getSelected',
            params: { sessionSheetId: $stateParams.id }
        }).then(
            function(res) {
                $scope.selected = res.data;
                // console.log(res.data);
                // console.log($scope.selected);
            }/*,
            function(res) { // error
                $scope.toasterError('', 'Данные не были сохранены');
            }*/
        );
    }

    getContentSession();
    function getContentSession() {
        $http({
            method: 'PATCH',
            url: '/getContentSession',
            params: {
                sessionSheetId: $stateParams.id,
                isHead: $scope.isHead
            }
        }).then(
            function(res) {
                $scope.contentSession = res.data;
                $scope.contentSessionCopy = angular.copy($scope.contentSession);
                if (!$scope.contentSession.length) // == 0
                    return;
                $scope.groupNumber = $scope.contentSession[0].sessionSheet.group.number;
                $scope.disciplineName = $scope.contentSession[0].sessionSheet.syllabusContent.discipline.name;
                $scope.splitAttestationFormName = $scope.contentSession[0].sessionSheet.splitAttestationForm.name;
                $scope.deadlineDiscipline = $scope.contentSession[0].sessionSheet.syllabusContent.deadline;
                // console.log($scope.contentSession);
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else {
                    $scope.toasterError('Выбранная ведомость не доступна!');
                    $state.go('session');
                }
            }
        );
    }

    $scope.checkChanges = function () {
        $scope.changes = !angular.equals($scope.contentSessionCopy, $scope.contentSession); // true если равны
    };

    $scope.saveContentSession = function() {
        // console.log($scope.contentSession)
        if (!$scope.changes) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveContentSession',
            data: angular.toJson($scope.contentSession),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                // console.log(res.data);
                $scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.contentSession = angular.copy(res.data);
                // console.log(res.data);
                $scope.contentSessionCopy = angular.copy($scope.contentSession);
                $scope.checkChanges();
            },
            function(res) {
                $scope.toasterError('', 'Данные не были сохранены');
            }
        );
    };

});