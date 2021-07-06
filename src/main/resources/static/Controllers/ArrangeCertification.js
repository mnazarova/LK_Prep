app.controller("ArrangeCertificationController", function($scope, $http, $state) {

    $scope.checkRole("DEANERY");

    $scope.arrangeCertification = {
        name: '',
        deadline: '',
        distant: true
    };
    $scope.minDate = new Date().setDate(new Date().getDate()+1);
    $scope.arrangeCertification.deadline = new Date(new Date($scope.minDate).toISOString().split("T")[0]);
    $scope.arrangeCertification.deadline.setDate($scope.arrangeCertification.deadline.getDate()+1);
    $scope.arrangeCertification.deadline.setHours(0);

    $scope.createCertification = function() {
        if(!$scope.arrangeCertification.name) {
            $scope.toasterWarning('Введите название аттестации');
            return;
        }
        if($scope.arrangeCertificationForm.$invalid) {
            $scope.toasterWarning('Введите корректную дату');//('Форма заполнена некорректно');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/createCertification',
            data: angular.toJson($scope.arrangeCertification),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Аттестация успешно создана');
                $state.go('viewAttestationsDeanery');
            },
            function(res) {
                $scope.toasterError('Аттестация не была создана');
                // console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };
});