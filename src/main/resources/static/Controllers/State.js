app.controller("StateController", function($scope, $state, $stateParams, $http) {

    // $('input:disabled').css('background-color', 'red');
    $scope.statementsId = $stateParams.statementsId;
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
            params: { id: $stateParams.id }
        }).then(
            function(res) { // success
                $scope.contentAttestation = res.data;
                // $scope.contentAttestation[0].dateWorks = $scope.deadline;
                // console.log($scope.contentAttestation);
            },
            function(res) { // error
                // console.log("Error: " + res.status + " : " + res.data);
                // $state.go("attestation");
            }
        );
    }
});