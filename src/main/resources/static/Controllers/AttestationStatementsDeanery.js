app.controller("AttestationStatementsDeaneryController", function($stateParams, $scope, $state, $http) {

    $scope.checkRole("DEANERY");

    getGroupsByAttestationAndByDeaneryAndByGroupId();
    function getGroupsByAttestationAndByDeaneryAndByGroupId() {
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationAndByDeaneryAndByGroupId',
            params: { id: $stateParams.id, groupIds: '' }
        }).then(
            function(res) { // success
                $scope.statements = res.data;
                $scope.arrayGroupId = [];
                $scope.groupList = [];
                for (let i = 0, size = $scope.statements.length; i < size; i++) {
                    let groupListElement = {};
                    groupListElement.groupId = $scope.statements[i].groupId;
                    groupListElement.groupNumber = $scope.statements[i].groupNumber;
                    if ($scope.arrayGroupId.indexOf(groupListElement.groupId) === -1) {
                        $scope.arrayGroupId.push(groupListElement.groupId);
                        $scope.groupList.push(groupListElement);
                    }
                }
                // console.log($scope.statements)
                // console.log($scope.groupList)
            },
            function(res) { // error
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else
                    if (res.data === 1) {
                        $scope.toasterError('Выбранная аттестация не доступна для просмотра!');
                        $state.go('viewAttestationsDeanery');
                    }
            }
        )
    }

    $scope.selectedOnGroupList = function () {
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationAndByDeaneryAndByGroupId',
            params: {
                id: $stateParams.id,
                groupIds: $scope.statementForm.group.$modelValue.groupId
            }
        }).then(
            function(res) {
                $scope.statements = res.data;
                // if (res.status == 204) // NO_CONTENT
                //     $scope.selected_group_NO_CONTENT = true;
            }
        );
    };

});