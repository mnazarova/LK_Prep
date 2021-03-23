app.controller("ContentSessionController", function($scope, $state, $stateParams, $http) {

    $scope.checkRole("TEACHER");

    $scope.isHead = $stateParams.isHead === 'true';

    $scope.contentSession = [];
    $scope.selected = [];
    $scope.selected = {
        id: '',
        name: ''
    };

    $scope.selectedAdmittance = [
        { id: 6, name: 'Сдано' },
        { id: 7, name: 'Не сдано' }
    ];

    getSelected();
    function getSelected() {
        $http({
            method: 'PATCH',
            url: '/getSelected',
            params: { sessionSheetId: $stateParams.id }
        }).then(
            function(res) {
                $scope.selected = res.data;
                $scope.allSelectedForExams = angular.copy(res.data);
                // console.log($scope.selected);

                getContentSession();
            }/*,
            function(res) { // error
                $scope.toasterError('', 'Данные не были сохранены');
            }*/
        );
    }

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
                $scope.splitAttestationForm = $scope.contentSession[0].sessionSheet.splitAttestationForm;
                $scope.deadlineDiscipline = $scope.contentSession[0].sessionSheet.syllabusContent.deadline;

                $scope.contentSession.forEach(function(item, index) {
                    // console.log(item);
                    if (item.admittance) {
                        // console.log(item.admittance)
                        if (item.admittance.id === 7) { // не сдано
                            $scope.selected[index] = [];
                            $scope.selected[index].push($scope.allSelectedForExams[0]);
                        }
                        if (item.admittance.id === 6) { // сдано
                            $scope.selected[index] = angular.copy($scope.allSelectedForExams);
                            $scope.selected[index].splice(0, 1);
                        }
                    }
                    else
                        $scope.selected[index] = angular.copy($scope.allSelectedForExams);
                });

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

    $scope.checkChangesAdmittance = function (item, index) {
        if (item === 7) { // не сдано
            $scope.selected[index] = [];
            $scope.selected[index].push($scope.allSelectedForExams[0]);
        }
        if (item === 6) { // сдано
            $scope.selected[index] = angular.copy($scope.allSelectedForExams);
            $scope.selected[index].splice(0, 1);
        }
        $scope.contentSession[index].evaluation = $scope.selected[index][0];
        console.log($scope.contentSession[index])
        console.log($scope.selected)
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
            params: { sessionSheetId: $stateParams.id },
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