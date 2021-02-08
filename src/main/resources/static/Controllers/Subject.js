app.controller("SubjectController", function($stateParams, $scope, $state, $http) {

    getGroupsByAttestationIdAndTeacherId();
    function getGroupsByAttestationIdAndTeacherId() {
        /*$http.patch('/getGroupsByAttestationAndByTeacher',{params: {'id': 146/!*$stateParams.id*!/}})
            .then(function  (response) {
                $scope.groups = response.data;
        });*/
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationIdAndTeacherId',
            params: { attestationId: $stateParams.id }
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
                    $scope.toasterError('Обратитесь к администратору!');
                else
                    $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');
            }
        );
    }

    $scope.subjectSelectedGroup = function () {
        // console.log($scope.subjectForm.group.$modelValue.groupId)
        if(!$scope.subjectForm.group.$modelValue.groupId)
            return;
        $http({
            method: 'PATCH',
            url: '/subjectSelectedGroup',
            params: {
                groupId: $scope.subjectForm.group.$modelValue.groupId,
                attestationId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.groups = res.data;
                console.log(res.data)
            }
        );
    };

});