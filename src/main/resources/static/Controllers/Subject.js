app.controller("SubjectController", function($stateParams, $scope, $state, $http) {

    $scope.checkRole("TEACHER");

    getGroupsByAttestationIdAndTeacherIdAndGroupId();
    function getGroupsByAttestationIdAndTeacherIdAndGroupId() {
        /*$http.patch('/getGroupsByAttestationAndByTeacher',{params: {'id': 146/!*$stateParams.id*!/}})
            .then(function  (response) {
                $scope.groups = response.data;
        });*/
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationIdAndTeacherIdAndGroupId',
            params: {
                attestationId: $stateParams.id,
                groupId: ''
            }
            // transformRequest: angular.identity,
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
                // console.log($scope.groupList)
            },
            function(res) { // error
                // console.log(res.data)
                if (res.data === 0)
                    $scope.toasterError('Проблема с Вашей учётной записью. Пожалуйста, обратитесь к администратору!');
                else {
                    $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');
                    $state.go('attestation');
                }
            }
        );
    }

    $scope.subjectSelectedGroup = function () {
        // console.log($scope.subjectForm.group.$modelValue.groupId)
        if(!$scope.subjectForm.group.$modelValue.groupId)
            return;
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationIdAndTeacherIdAndGroupId',
            params: {
                groupId: $scope.subjectForm.group.$modelValue.groupId,
                attestationId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.groups = res.data;
                // console.log(res.data)
            }
        );
    };


    /* Для зав. каф.*/
    getGroupsForHeadOfDepartment();
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
                /*if (res.data === 0)
                    $scope.toasterError('Обратитесь к администратору!');
                else
                    $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');*/
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
    };

});