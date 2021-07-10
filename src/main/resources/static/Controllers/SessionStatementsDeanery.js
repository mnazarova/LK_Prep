app.controller("SessionStatementsDeaneryController", function($scope, $state, $http, $stateParams) {

    $scope.checkRole("DEANERY");

    $scope.groupId = $stateParams.groupId;
    $scope.sessionStatementForm = {
        isAdditional: false
    };

    getSemesterNumberSetByGroupId();
    function getSemesterNumberSetByGroupId() {
        $http({
            method: 'PATCH',
            url: '/getSemesterNumberSetByGroupId',
            params: { groupId: $stateParams.groupId }
        }).then(
            function(res) { // success
                $scope.semesterNumberList = res.data;
                getCurSemesterByGroupId();
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    if (res.data === 1) {
                        $scope.toasterError('Выбранная группа недоступна для просмотра!');
                        $state.go('sessionGroupsDeanery');
                    }
            }
        )
    }

    function getCurSemesterByGroupId() {
        $http({
            method: 'PATCH',
            url: '/getCurSemesterByGroupId',
            params: { groupId: $stateParams.groupId }
        }).then(
            function(res) {
                $scope.curSemesterNumber = res.data;
                $scope.sessionStatementForm.semesterNumber = res.data;
                if (res.data < $scope.semesterNumberList[0] && res.data > $scope.semesterNumberList[$scope.semesterNumberList.length-1])
                    $scope.sessionStatementForm.semesterNumber = $scope.semesterNumberList[0];
                $scope.changedSemesterNumberOrIsAdditional();
            }
        )
    }

    $scope.changedSemesterNumberOrIsAdditional = function () {
        $http({
            method: 'PATCH',
            url: '/getSessionStatementsByGroupIdAndSemesterNumberForDeanery',
            params: {
                groupId: $stateParams.groupId,
                semesterNumber: $scope.sessionStatementForm.semesterNumber,
                isAdditional: $scope.sessionStatementForm.isAdditional
            }
        }).then(
            function(res) {
                $scope.sessionStatements = res.data;
            }
        );
    };

});