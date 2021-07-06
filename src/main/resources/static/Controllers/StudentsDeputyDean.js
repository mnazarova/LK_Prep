app.controller("StudentsDeputyDeanController", function($scope, $http, $stateParams, $state) {

    $scope.checkRole("DEPUTY_DEAN");

    getStudentsForDeputyDean();
    function getStudentsForDeputyDean() {
        $http({
            method: 'PATCH',
            url: '/getStudentsForDeputyDean',
            params: { groupId: $stateParams.groupId }
        }).then(
            function(res) {
                $scope.students = res.data;
                $scope.studentsCopy = angular.copy($scope.students);
                if (!$scope.students.length) // == 0
                    return;
                $scope.groupNumber = $scope.students[0].group.number;
                $scope.checkChanges();
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    if (res.data === 1) {
                        $scope.toasterError('Список студентов выбранной группы недоступен для просмотра!');
                        $state.go('groups');
                    }
            }
        );
    }

    $scope.checkChanges = function () {
        $scope.changes = !angular.equals($scope.students, $scope.studentsCopy); // true если равны
        // console.log($scope.students)
        // console.log($scope.studentsCopy)
    };

    $scope.changeExpelled = function () {
        $scope.checkChanges();
    };

    $scope.saveExpelledStudentsForDeputyDean = function() {
        if (!$scope.changes) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveExpelledStudentsForDeputyDean',
            params: { groupId: $stateParams.groupId },
            data: angular.toJson($scope.students),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Данные успешно сохранены');
                $scope.studentsCopy = angular.copy(res.data);
                $scope.checkChanges();
            },
            function(res) {
                $scope.toasterError('', 'Данные не были сохранены');
            }
        );
    };

});