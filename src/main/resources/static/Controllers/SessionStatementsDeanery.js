app.controller("SessionStatementsDeaneryController", function($scope, $http) {

    $scope.checkRole("DEANERY");

    getSessionStatementsByDeanery();
    function getSessionStatementsByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getSessionStatementsByDeanery',
            params: { groupIds: '' }
        }).then(
            function(res) { // success
                $scope.sessionStatements = res.data;
                $scope.arrayGroupId = [];
                $scope.groupListSession = [];
                for (let i = 0, size = $scope.sessionStatements.length; i < size; i++) {
                    let groupListSessionElement = {};
                    groupListSessionElement.groupId = $scope.sessionStatements[i].groupId;
                    groupListSessionElement.groupNumber = $scope.sessionStatements[i].groupNumber;
                    if ($scope.arrayGroupId.indexOf(groupListSessionElement.groupId) === -1) {
                        $scope.arrayGroupId.push(groupListSessionElement.groupId);
                        $scope.groupListSession.push(groupListSessionElement);
                    }
                }
                // console.log($scope.sessionStatements)
                // console.log($scope.groupListSession)
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
            }
        )
    }

    $scope.selectedOnGroupListSession = function () {
        $http({
            method: 'PATCH',
            url: '/getSessionStatementsByDeanery',
            params: {
                groupIds: $scope.sessionStatementForm.group.$modelValue.groupId
            }
        }).then(
            function(res) {
                $scope.sessionStatements = res.data;
            }
        );
    };

});