app.controller("AttestationController", function($scope, $http, $state) {

    /*initTooltip();
    function initTooltip() {
        $(function () {
            $('[data-toggle="tooltip"]').tooltip()
        });
        console.log(1)
    }*/

    getAttestation();
    function getAttestation() {
        $http({
            method: 'GET',
            url: '/getAttestation'
        }).then(
            function(res) { // success
                $scope.attestations = res.data;
                /*$scope.attestations.forEach(function(item, index) {
                    formatDate(item.deadline);
                    // console.log(item, index);
                });
                console.log($scope.attestations);*/
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
                $state.go("help");
            }
        );
    }

    $scope.transitionToSubject = function(attestationId) {
        $state.go('subject', {id: attestationId}); // ui-sref="fill.subject({id:attestation.id})"
    };




    /*function formatDate(date) {
        /!*var editingDate = new Date();
        date = editingDate.setDate(date);*!/
        console.log(date);


        var o_date = new Intl.DateTimeFormat;
        console.log(o_date)
        const f_date = (m_ca, m_it) => Object({...m_ca, [m_it.type]: m_it.value});
        var m_date = o_date.formatToParts().reduce(f_date, {});
        console.log(m_date.day + '-' + m_date.month + '-' + m_date.year);
    }*/

    /*$scope.transitionMedicineId = function(medicineId) {
        console.log(medicineId)
        $http({
            method: 'PUT',
            url: '/transitionMedicineId',
            data: angular.toJson(medicineId),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) { // success
                // $scope.medicines = res.data;
                // getCurrentOrder();
                // console.log($scope.medicines);
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }*/

});