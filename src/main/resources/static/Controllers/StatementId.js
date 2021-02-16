app.controller("StatementIdController", function($stateParams, $scope, $state, $http) {

    getGroupsByAttestationAndByDeanery();
    function getGroupsByAttestationAndByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationAndByDeanery',
            params: { id: $stateParams.id, groupIds: '' }
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                $scope.arrayGroupId = [];
                $scope.groupList = [];
                for (let i = 0, size = $scope.groups.length; i < size; i++) {
                    let groupListElement = {};
                    groupListElement.groupId = $scope.groups[i].groupId;
                    groupListElement.groupNumber = $scope.groups[i].groupNumber;
                    if ($scope.arrayGroupId.indexOf(groupListElement.groupId) === -1) {
                        $scope.arrayGroupId.push(groupListElement.groupId);
                        $scope.groupList.push(groupListElement);
                    }
                }
                // console.log($scope.groups)
                // console.log($scope.groupList)
            }
        )
    }

    $scope.selectedOnGroupList = function () {
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationAndByDeanery',
            params: {
                id: $stateParams.id,
                groupIds: $scope.statementForm.group.$modelValue.groupId
            }
        }).then(
            function(res) {
                $scope.groups = res.data;
                // if (res.status == 204) // NO_CONTENT
                //     $scope.selected_group_NO_CONTENT = true;
            }
        );
    };

});