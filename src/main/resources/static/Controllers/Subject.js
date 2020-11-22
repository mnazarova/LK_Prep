app.controller("SubjectController", function($stateParams, $scope, $state, $http) {

    getGroupsByAttestationAndByTeacher();
    function getGroupsByAttestationAndByTeacher() {
        /*$http.patch('/getGroupsByAttestationAndByTeacher',{params: {'id': 146/!*$stateParams.id*!/}})
            .then(function  (response) {
                $scope.groups = response.data;
        });*/
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationAndByTeacher',
            params: { id: $stateParams.id }
            // transformRequest: angular.identity,
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                // console.log($scope.groups);
                /*$scope.attestations.forEach(function(item, index) {
                    formatDate(item.deadline);
                    // console.log(item, index);
                });*/
            },
            function(res) { // error
                //$state.go("help");
            }
        );
    }

    getSubgroupsByAttestationAndByTeacher();
    function getSubgroupsByAttestationAndByTeacher() {
        $http({
            method: 'PATCH',
            url: '/getSubgroupsByAttestationAndByTeacher',
            params: { id: $stateParams.id }
        }).then(
            function(res) {
                $scope.subgroups = res.data;
                // console.log($scope.subgroups);
                /*$scope.attestations.forEach(function(item, index) {
                    formatDate(item.deadline);
                });*/
            },
            function(res) {
                //$state.go("help");
            }
        );
    }

});