app.controller("AttestationStatementDeaneryController", function($scope, $state, $stateParams, $http) {

    $scope.checkRole("DEANERY");

    // $('input:disabled').css('background-color', 'red');
    // console.log($stateParams)
    $scope.attestationId = $stateParams.attestationId;
    $scope.selected = [
        { key: true, value: '+' },
        { key: false, value: '-' }
    ];

    /*$scope.minDate = new Date().setDate((new Date().getDate())+1);
    $scope.deadline = new Date(new Date($scope.minDate).toISOString().split("T")[0]);
    $("#date_input").on("change", function () {
        $(this).css("color", "rgba(0,0,0,0)").siblings(".datepicker_label").css({ "text-align":"center", position: "absolute",left: "10px", top:"14px",width:$(this).width()}).text($(this).val().length == 0 ? "" : ($.datepicker.formatDate($(this).attr("dateformat"), new Date($(this).val()))));
    });*/

    getContentAttestationForDeanery();
    function getContentAttestationForDeanery() {
        $http({
            method: 'PATCH',
            url: '/getContentAttestationForDeanery',
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
                        $scope.toasterError('Выбранная ведомость недоступна для просмотра!');
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