app.controller("ContentSessionController", function($scope, $state, $stateParams, $http) {

    $scope.attestationId = $stateParams.attestationId;
    $scope.isHead = $stateParams.isHead === 'true';
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
                // console.log($scope.contentAttestation);
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else {
                    $scope.toasterError('Выбранная ведомость не доступна в текущей аттестации!');
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
        // $scope.changeHard = true;
        $http({
            method: 'PATCH',
            url: '/saveContentAttestation',
            data: angular.toJson($scope.contentAttestation),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                console.log(res.data);
                // console.log($scope.changeHard);
                // $scope.changeHard = false;
                // $scope.contentAttestation = {}; // не помогает прорисовать заново tooltip

                $scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.contentAttestation = angular.copy(res.data);
                $scope.contentAttestationCopy = angular.copy($scope.contentAttestation);
                $scope.checkChanges();


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
            },
            function(res) {
                $scope.toasterError('', 'Данные не были сохранены');
            }
        );
    };

});