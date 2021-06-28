app.controller("SessionController", function($stateParams, $scope, $state, $http) {

    $scope.checkRole("TEACHER");

    getSessionDTOGroupsByTeacherIdAndGroupId();
    function getSessionDTOGroupsByTeacherIdAndGroupId() {
        $http({
            method: 'PATCH',
            url: '/getSessionDTOGroupsByTeacherIdAndGroupId',
            params: { groupId: '' }
        }).then(
            function(res) {
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
                //console.log($scope.groups)
                // console.log($scope.groupList)
            },
            function(res) {
                // console.log(res.data)
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
            }
        );
    }

    $scope.selectedSessionDTOGroup = function () {
        if(!$scope.sessionDTOForm.group.$modelValue.groupId)
            return;
        $http({
            method: 'PATCH',
            url: '/getSessionDTOGroupsByTeacherIdAndGroupId',
            params: {  groupId: $scope.sessionDTOForm.group.$modelValue.groupId }
        }).then(
            function(res) {
                $scope.groups = res.data;
                //console.log(res.data)
            }
        );
    };


    /* Для зав. каф.*/
    getSessionDTOGroupsForHeadOfDepartment();
    function getSessionDTOGroupsForHeadOfDepartment() {
        $http({
            method: 'PATCH',
            url: '/getSessionDTOGroupsForHeadOfDepartment',
            params: { groupId: '' }
        }).then(
            function(res) {
                $scope.groupsForHead = res.data;
                $scope.arrayGroupForHeadId = [];
                $scope.groupForHeadList = [];
                for (let i = 0, size = $scope.groupsForHead.length; i < size; i++) {
                    let groupForHeadListElement = {};
                    groupForHeadListElement.groupId = $scope.groupsForHead[i].groupId;
                    groupForHeadListElement.groupNumber = $scope.groupsForHead[i].groupNumber;
                    if ($scope.arrayGroupForHeadId.indexOf(groupForHeadListElement.groupId) === -1) {
                        $scope.arrayGroupForHeadId.push(groupForHeadListElement.groupId);
                        $scope.groupForHeadList.push(groupForHeadListElement);
                    }
                }
            },
            function(res) {
                /*if (res.data === 0)
                    $scope.toasterError('Обратитесь к администратору!');
                else
                    $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');*/
            }
        );
    }

    $scope.selectedSessionDTOGroupForHead = function () {
        // console.log($scope.sessionDTOForm)
        if(!$scope.sessionDTOForm.groupForHead.$modelValue.groupId)
            return;
        $http({
            method: 'PATCH',
            url: '/getSessionDTOGroupsForHeadOfDepartment',
            params: { groupId: $scope.sessionDTOForm.groupForHead.$modelValue.groupId }
        }).then(
            function(res) {
                $scope.groupsForHead = res.data;
            }
        );
    };

});