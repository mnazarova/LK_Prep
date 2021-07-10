app.controller("ContentSessionController", function($scope, $state, $stateParams, $http) {

    $scope.checkRole("TEACHER");

    $scope.isHead = $stateParams.isHead === 'true';
    $scope.global = {};

    $scope.contentSession = [];
    $scope.selected = [];
    $scope.selected = {
        id: '',
        name: ''
    };

    $scope.selectedForAdmittance = [
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
                // console.log($scope.allSelectedForExams);

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
                $scope.isAdditional = $scope.contentSession[0].sessionSheet.isAdditional;

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

                $scope.setGlobalAdmittance();
                $scope.setGlobalEvaluation();
                // console.log($scope.contentSession);
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else {
                    $scope.toasterError('Выбранная ведомость недоступна для просмотра!');
                    $state.go('session');
                }
            }
        );
    }

    $scope.checkChangesAdmittance = function (item, index) {
        if (item === 7) { // не сдано
            $scope.selected[index] = [];
            $scope.selected[index].push($scope.allSelectedForExams[0]);
        }
        if (item === 6) { // сдано
            $scope.selected[index] = angular.copy($scope.allSelectedForExams);
            $scope.selected[index].splice(0, 1);
        }
        if(!$scope.contentSession[index].evaluation) { // == null
            $scope.contentSession[index].evaluation = JSON.parse(angular.toJson($scope.selected[index].find(function (element) {
                return element.id === $scope.selected[index][0].id;
            }))); // console.log($scope.contentSession[index].evaluation)
        }
        else
            $scope.contentSession[index].evaluation.id = $scope.selected[index][0].id;
        // $scope.contentSession[index].evaluation = JSON.parse(angular.toJson($scope.selected[index][0]));
        // console.log($scope.contentSession[index].evaluation)
        // console.log($scope.selected[index][0])
    };

    $scope.selectedAdmittance = function (item, index) {
        $scope.checkChangesAdmittance(item, index);
        $scope.setGlobalAdmittance();
        $scope.checkChanges();
    };
    $scope.changeGlobalAdmittance = function () {
        $scope.contentSession.forEach(function (el, index) {
            if(!el.admittance) // == null
                el.admittance = JSON.parse(angular.toJson($scope.selectedForAdmittance.find(function (element) {
                     // console.log(element.id) // console.log($scope.global.admittance)
                    return element.id === $scope.global.admittance;
                })));
            else
                el.admittance.id = $scope.global.admittance;
            $scope.checkChangesAdmittance(el.admittance.id, index);
        });
        $scope.global.evaluation = $scope.selected[0][0].id;
        $scope.checkChanges();
    };
    $scope.setGlobalAdmittance = function () {
        let array = $scope.contentSession;
        let length = array.length;
        if (length < 1 || !array[0].admittance) return;

        let el = array[0].admittance.id, i= 1;
        for(; i < length; i++)
            if (!array[i].admittance || el !== array[i].admittance.id) break;
        if (i === length) $scope.global.admittance = array[0].admittance.id;
        else {
            $scope.global.admittance = null;
            $scope.global.evaluation = null;
        }
    };

    $scope.selectedEvaluation = function () {
        // console.log($scope.contentSession)
        $scope.setGlobalEvaluation();
        $scope.checkChanges();
    };
    $scope.changeGlobalEvaluation = function () {
        $scope.contentSession.forEach(function (el, index) {
            if(!el.evaluation) // == null
                $scope.contentSession[index].evaluation = JSON.parse(angular.toJson($scope.selected[index].find(function (element) {
                    return element.id === $scope.global.evaluation;
                })));
            else
                $scope.contentSession[index].evaluation.id = $scope.global.evaluation;
        });
        // console.log($scope.contentSession)
        $scope.checkChanges();
    };
    $scope.setGlobalEvaluation = function () {
        let array = $scope.contentSession;
        let length = array.length;
        if (length < 1 || !array[0].evaluation) return;

        let el = array[0].evaluation.id, i= 1;
        for(; i < length; i++)
            if (!array[i].evaluation || el !== array[i].evaluation.id) break;
        if (i === length) $scope.global.evaluation = array[0].evaluation.id;
        else $scope.global.evaluation = null;
    };

    $scope.checkChanges = function () {
        // console.log($scope.contentSessionCopy)
        // console.log($scope.contentSession)
        // console.log($scope.changes)
        $scope.changes = !angular.equals($scope.contentSessionCopy, $scope.contentSession); // true если равны
        // console.log($scope.changes)
    };

    $scope.toListOfStatements = function () {
        if (!$scope.changes)
            $state.go('session');
        else
            $scope.$root['ModalController'].confirmYesAndNo("Внесённые изменения не будут сохранены. " +
                "Вы уверены, что хотите перейти?", "Да", "Нет", 'btn-outline-danger', 'btn-outline-info',
                function () {
                    $state.go('session');
                }
            );
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