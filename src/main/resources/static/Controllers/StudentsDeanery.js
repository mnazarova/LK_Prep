app.controller("StudentsDeaneryController", function($scope, $http, $stateParams, $state) {

    getStudents();
    function getStudents() {
        $http({
            method: 'PATCH',
            url: '/getStudents',
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
                        $scope.toasterError('Список студентов выбранной группы не доступен для просмотра!');
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

    $scope.saveExpelledStudents = function() {
        if (!$scope.changes) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveExpelledStudents',
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