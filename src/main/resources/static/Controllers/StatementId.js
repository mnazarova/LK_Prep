app.controller("StatementIdController", function($stateParams, $scope, $state, $http) {

    $scope.forms = {};

    getGroupListByDeanery();
    function getGroupListByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getGroupListByDeanery'
        }).then(
            function(res) { // success
                $scope.groupList = res.data;
                // console.log($scope.groupList)
            }
        );
    }

    getSubroupListByDeanery();
    function getSubroupListByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getSubroupListByDeanery',
            params: { id: $stateParams.id }
        }).then(
            function(res) {
                $scope.subgroupList = res.data;
            }
        );
    }


    getGroupsByAttestationAndByDeanery();
    function getGroupsByAttestationAndByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getGroupsByAttestationAndByDeanery',
            params: { id: $stateParams.id }
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                // if (res.status == 204) // NO_CONTENT
                //     $scope.groups_NO_CONTENT = true;
            }
        )
    }

    getSubgroupsByAttestationAndByDeanery();
    function getSubgroupsByAttestationAndByDeanery() {
        $http({
            method: 'PATCH',
            url: '/getSubgroupsByAttestationAndByDeanery',
            params: { id: $stateParams.id }
        }).then(
            function(res) { // success
                $scope.subgroups = res.data;
                // if (res.status == 204) // NO_CONTENT
                //     $scope.subgroups_NO_CONTENT = true;
            }
        );
    }

    $scope.selectedOnGroupList = function () {
        if(!$scope.forms.statementsForm.group.$modelValue)
            return;
        $http({
            method: 'PATCH',
            url: '/selectedOnGroupList',
            params: {
                id: $stateParams.id,
                groupId: $scope.forms.statementsForm.group.$modelValue.id
            }
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                // if (res.status == 204) // NO_CONTENT
                //     $scope.selected_group_NO_CONTENT = true;
            }
        );
    };

    $scope.selectedOnSubgroupList = function () {
        if(!$scope.forms.statementsForm.subgroup.$modelValue)
            return;
        $http({
            method: 'PATCH',
            url: '/selectedOnSubgroupList',
            params: {
                id: $stateParams.id,
                subgroupId: $scope.forms.statementsForm.subgroup.$modelValue.id
            }
        }).then(
            function(res) { // success
                $scope.subgroups = res.data;
                // if (res.status == 204) // NO_CONTENT
                //     $scope.selected_subgroup_NO_CONTENT = true;
            }
        );
    }

});