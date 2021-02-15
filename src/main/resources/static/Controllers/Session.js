app.controller("SessionController", function($stateParams, $scope, $state, $http) {

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
                console.log($scope.groups)
                // console.log($scope.groupList)
            },
            function(res) {
                // console.log(res.data)
                if (res.data === 0)
                    $scope.toasterError('Проблема с учётной записью. Обратитесь к администратору!');
            }
        );
    }

    $scope.subjectSelectedGroup = function () {
        if(!$scope.subjectForm.group.$modelValue.groupId)
            return;
        $http({
            method: 'PATCH',
            url: '/getSessionDTOGroupsByTeacherIdAndGroupId',
            params: {  groupId: $scope.subjectForm.group.$modelValue.groupId }
        }).then(
            function(res) {
                $scope.groups = res.data;
                console.log(res.data)
            }
        );
    };


    /* Для зав. каф.*/
    /*getGroupsForHeadOfDepartment();
    function getGroupsForHeadOfDepartment() {
        $http({
            method: 'PATCH',
            url: '/getGroupsForHeadOfDepartment',
            params: {
                attestationId: $stateParams.id,
                groupId: ''
            }
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
                /!*if (res.data === 0)
                    $scope.toasterError('Обратитесь к администратору!');
                else
                    $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');*!/
            }
        );
    }

    $scope.subjectSelectedGroupForHead = function () {
        // console.log($scope.subjectForm)
        if(!$scope.subjectForm.groupForHead.$modelValue.groupId)
            return;
        $http({
            method: 'PATCH',
            url: '/getGroupsForHeadOfDepartment',
            params: {
                attestationId: $stateParams.id,
                groupId: $scope.subjectForm.groupForHead.$modelValue.groupId
            }
        }).then(
            function(res) {
                $scope.groupsForHead = res.data;
            }
        );
    };*/

});