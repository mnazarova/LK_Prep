app.controller("DeputyDeanSessionStatementsController", function($scope, $state, $http, $stateParams) {

    $scope.checkRole("DEPUTY_DEAN");

    $scope.groupId = $stateParams.groupId;
    $scope.sessionStatementForm = {};

    getSemesterNumberSetByGroupIdForDeputyDean();
    function getSemesterNumberSetByGroupIdForDeputyDean() {
        $http({
            method: 'PATCH',
            url: '/getSemesterNumberSetByGroupIdForDeputyDean',
            params: { groupId: $stateParams.groupId }
        }).then(
            function(res) { // success
                $scope.semesterNumberList = res.data;
                getCurSemesterByGroupIdForDeputyDean();
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                if (res.data === 1) {
                    $scope.toasterError('Выбранная группа недоступна для просмотра!');
                    $state.go('deputyDeanSessionGroups');
                }
            }
        )
    }

    function getCurSemesterByGroupIdForDeputyDean() {
        $http({
            method: 'PATCH',
            url: '/getCurSemesterByGroupIdForDeputyDean',
            params: { groupId: $stateParams.groupId }
        }).then(
            function(res) {
                $scope.curSemesterNumber = res.data;
                $scope.sessionStatementForm.semesterNumber = res.data;
                if (res.data < $scope.semesterNumberList[0] && res.data > $scope.semesterNumberList[$scope.semesterNumberList.length-1])
                    $scope.sessionStatementForm.semesterNumber = $scope.semesterNumberList[0];
                $scope.selectedSemesterNumber();
            }
        )
    }

    $scope.selectedSemesterNumber = function () {
        $http({
            method: 'PATCH',
            url: '/getSessionStatementsByGroupIdAndSemesterNumberForDeputyDean',
            params: {
                groupId: $stateParams.groupId,
                semesterNumber: $scope.sessionStatementForm.semesterNumber
            }
        }).then(
            function(res) {
                $scope.sessionStatements = res.data;
            }
        );
    };

});