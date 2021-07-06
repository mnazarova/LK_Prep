app.controller("ContentAttestationController", function($scope, $state, $stateParams, $http, $rootScope) {

    $scope.checkRole("TEACHER");

    $scope.attestationId = $stateParams.attestationId;
    $scope.isHead = $stateParams.isHead === 'true';
    $scope.global = {};
    /*$scope.global.works = null;
    $scope.globalSelected = [
        { key: null, value: 'Устан. всем' },
        { key: true, value: '+' },
        { key: false, value: '-' }
    ];*/

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
                attestationId: $stateParams.attestationId,
                isHead: $scope.isHead
            }
        }).then(
            function(res) { // success
                $scope.contentAttestation = res.data;
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);

                if (!$scope.contentAttestation.length) // == 0
                    return;
                $scope.groupNumber = $scope.contentAttestation[0].certificationAttestation.group.number;
                $scope.attestation = $scope.contentAttestation[0].certificationAttestation.attestation;
                $scope.disciplineName = $scope.contentAttestation[0].certificationAttestation.syllabusContent.discipline.name;

                $scope.setGlobalWorks();
                $scope.setGlobalAttest();
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else {
                    $scope.toasterError('Выбранная ведомость недоступна в текущей аттестации!');
                    $state.go('subject', {id: $stateParams.attestationId});
                }
            }
        );
    }

    /*$scope.replyAll = function() {
        if (angular.equals($scope.contentAttestationCopy, $scope.contentAttestation))
            $scope.toasterWarning('Изменения отсутствуют');
        else
            $scope.contentAttestation = angular.copy($scope.contentAttestationCopy);
    };*/

    $scope.selectedWorks = function () {
        $scope.setGlobalWorks();
        $scope.checkChanges();
    };

    $scope.selectedAttest = function () {
        $scope.setGlobalAttest();
        $scope.checkChanges();
    };

    $scope.changeGlobalWorks = function () {
        $scope.contentAttestation.forEach(function (el) {
            el.works = $scope.global.works;
        });
        $scope.checkChanges();
    };
    $scope.setGlobalWorks = function () {
        let array = $scope.contentAttestation;
        let length = array.length;
        if (length < 1) return;

        let el = array[0].works, i= 1;
        for(; i < length; i++)
            if (el !== array[i].works) break;
        if (i === length)
            $scope.global.works = array[0].works;
        else
        // if (i !== length)
            $scope.global.works = null;
    };

    $scope.changeGlobalAttest = function () {
        $scope.contentAttestation.forEach(function (el) {
            el.attest = $scope.global.attest;
        });
        $scope.checkChanges();
    };
    $scope.setGlobalAttest = function () {
        let length = $scope.contentAttestation.length;
        if (length < 1) return;

        let el = $scope.contentAttestation[0].attest, i= 1;
        for(; i < length; i++)
            if (el !== $scope.contentAttestation[i].attest) break;
        // if (i !== length)
        if (i === length)
            $scope.global.attest = $scope.contentAttestation[0].attest;
        else
            $scope.global.attest = null;
    };


    $scope.checkChanges = function () {
        $scope.changes = !angular.equals($scope.contentAttestationCopy, $scope.contentAttestation); // true если равны
        // console.log($scope.contentAttestationCopy) // console.log($scope.contentAttestation)
    };

    $scope.toListOfStatements = function () {
        if (!$scope.changes)
            $state.go('subject', {id: $scope.attestationId});
        else
            $scope.$root['ModalController'].confirmYesAndNo("Внесённые изменения не будут сохранены. " +
                "Вы уверены, что хотите перейти?", "Да", "Нет", 'btn-outline-danger', 'btn-outline-info',
                function () {
                    $state.go('subject', {id: $scope.attestationId});
                }
            );
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
                $state.reload();
                /*$scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.contentAttestation = angular.copy(res.data);
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);
                $scope.checkChanges();*/


                // console.log(res.data);
                // console.log($scope.changeHard);
                // $scope.changeHard = false;
                // $scope.contentAttestation = {}; // не помогает прорисовать заново tooltip

                /*$scope.contentAttestation.forEach(function (el, index) {
                    var value = el.certificationAttestation;
                    console.log(el);
                    console.log(index);*/
                    // $('#tooltipWorks').tooltip('dispose').tooltip({title: 'Goodbye'}).tooltip('show')

                    /*$(document).ready(function(){
                        $('#tooltipWorks[index]').attr('title', 'new text')
                            .tooltip('dispose') // if you are using BS4 use .tooltip('dispose')
                            .tooltip({ title: 'new text'});
                        $('[data-toggle="tooltip"]').tooltip('show');

                    });*/
                    // $("#tooltipWorks[0]").attr('data-original-title', "newValue");
                    // if (value.dateWorks) {
                        /*$("#tooltipWorks[0]").tooltip({
                            "title": function() {
                            return "Последние изменения внесены бла-бла-бла";
                            // return "Последние изменения внесены {{ca.dateWorks | date: 'dd.MM.yyyy в HH:mm'}} ({{ca.setWorksByTeacher.surname + ' ' + ca.setWorksByTeacher.initials}})";
                                // return "<h2>"+$("#tooltipcontainer").html()+"</h2>";
                            }
                        });*/
                    // }
                // });
            }/*,
            function(res) {
                $scope.toasterError('', 'Данные не были сохранены');
            }*/
        );
    };

    /*let stateChangeWithSaving = $scope.$root.$on('$stateChangeStart', function (event, toState, toParams) {
        console.log(1)
        if ($scope.changes()) {
            console.log(2)
            event.preventDefault();
            //$scope.$root['ModalController'].confirmYesAndNo('Все внесённые изменения будут потеряны. Вы уверены, что хотите перейти?', function () {
              //  stateChangeWithSaving();
                //$state.go(toState, toParams);
            //})
            $scope.$root['ModalController'].confirmYesAndNo("Внесённые изменения не будут сохранены. " +
                "Вы уверены, что хотите перейти?", "Да", "Нет", 'btn-outline-danger', 'btn-outline-info',
                function () {
                    stateChangeWithSaving();
                    $state.go('subject', {id: $scope.attestationId});//$state.go(toState, toParams);
                }
            );
        }
    });*/

});