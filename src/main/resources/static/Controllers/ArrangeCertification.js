app.controller("ArrangeCertificationController", function($scope, $http, $state) {

    $scope.minDate = new Date().setDate(new Date().getDate()+1);
    $scope.deadline = new Date(new Date($scope.minDate).toISOString().split("T")[0]);
    // $scope.deadline.setDate($scope.deadline.getDate()+1);
    $scope.deadline.setHours(0);
    // console.log($scope.deadline)

    $scope.createCertification = function() {
        if(!$scope.name) {
            $scope.toasterWarning('Введите название аттестации');
            return;
        }
        if($scope.arrangeCertificationForm.$invalid) {
            $scope.toasterWarning('Введите корректную дату');//('Форма заполнена некорректно');
            return;
        }
        $scope.arrangeCertificationDTO = {
            name: $scope.name,
            deadline: $scope.deadline
        };
        $http({
            method: 'PATCH',
            url: '/createCertification',
            data: angular.toJson($scope.arrangeCertificationDTO),
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