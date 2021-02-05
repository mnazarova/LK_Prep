app.controller("AppointIdController", function($scope, $http, $stateParams) {
    $scope.showStudents = [];
    $scope.changes = [];
    $scope.syllabusContentList = {};
    $scope.oldSyllabusContentList = {};

    getGroupById();
    function getGroupById() {
        $http({
            method: 'PATCH',
            url: '/getGroupById',
            params: {
                groupId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.group = res.data;
            }
        );
    }

    getTeacherListByDepartmentId();
    function getTeacherListByDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/getTeacherListByDepartmentId'
        }).then(
            function(res) {
                $scope.teachers = res.data;
                $scope.teacher = {};
                console.log($scope.teachers)
            }
        );
    }

    getSyllabusContentListWithoutAttestationByGroupIdAndDepartmentId();
    function getSyllabusContentListWithoutAttestationByGroupIdAndDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/getSyllabusContentListWithoutAttestationByGroupIdAndDepartmentId',
            params: {
                groupId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.syllabusContentList = res.data;
                initTable();
            }
        );
    }

    function initTable() {
        // console.log($scope.syllabusContentList);
        for (let index=0, size = Object.keys($scope.syllabusContentList).length; index < size; index++) {
            $scope.setGlobalAdmittanceTeacher(index);
            $scope.setGlobalExamTeacher(index);
            $scope.setGlobalKrOrKpTeacher(index);
            $scope.oldSyllabusContentList[index] = angular.copy($scope.syllabusContentList[index]);
            $scope.checkChanges(index);
        }
    }

    $scope.checkChanges = function (index) {
        $scope.changes[index] = !angular.equals($scope.oldSyllabusContentList[index], $scope.syllabusContentList[index]); // true если равны
        console.log($scope.changes[index])
        // console.log($scope.oldSyllabusContentList[index])
        // console.log($scope.syllabusContentList[index])
    };

    $scope.toggleShowStudents = function (index) {
        $scope.showStudents[index] = !$scope.showStudents[index];
    };

    // Содержание учебного плана
    $scope.selectedAdmittanceTeacher = function (index) {
        let setValue = $scope.syllabusContentList[index].admittanceTeacher;
        $scope.syllabusContentList[index].connectTeacherStudentDTOList.forEach(function (el) {
            el.admittanceTeacher = setValue;
        });
        $scope.checkChanges(index);
    };
    $scope.setGlobalAdmittanceTeacher = function (index) {
        let array = $scope.syllabusContentList[index].connectTeacherStudentDTOList;
        let length = array.length;
        if (length < 1 || !array[0].admittanceTeacher)
            return;
        let el = array[0].admittanceTeacher.id, i= 1;
        for(; i < length; i++)
            if (el !== array[i].admittanceTeacher.id) break;
        if (i === length) $scope.syllabusContentList[index].admittanceTeacher = array[0].admittanceTeacher;
        else $scope.syllabusContentList[index].admittanceTeacher = null;
    };

    $scope.selectedExamTeacher = function (index) {
        let setValue = $scope.syllabusContentList[index].examTeacher;
        $scope.syllabusContentList[index].connectTeacherStudentDTOList.forEach(function (el) {
            el.examTeacher = setValue;
        });
        $scope.checkChanges(index);
    };
    $scope.setGlobalExamTeacher = function (index) {
        let array = $scope.syllabusContentList[index].connectTeacherStudentDTOList;
        let length = array.length;
        if (length < 1 || !array[0].examTeacher)
            return;
        let el = array[0].examTeacher.id, i= 1;
        for(; i < length; i++)
            if (el !== array[i].examTeacher.id) break;
        if (i === length) $scope.syllabusContentList[index].examTeacher = array[0].examTeacher;
        else $scope.syllabusContentList[index].examTeacher = null; // break

        // $scope.checkTeacher($scope.syllabusContentList[index].connectTeacherStudentDTOList, index, examTeacher)
    };
    /*$scope.checkTeacher = function (array, index, nameTeacher) {
        let length = array.length;
        if (length < 1 || !array[0].nameTeacher)
            return;
        let el = array[0].nameTeacher.id, i= 1;
        for(; i < length; i++)
            if (el !== array[i].nameTeacher.id) break;
        if (i === length) $scope.syllabusContentList[index].nameTeacher = array[0].nameTeacher;
        else $scope.syllabusContentList[index].nameTeacher = null; // break
    };*/

    $scope.selectedKrOrKpTeacher = function (index) {
        let setValue = $scope.syllabusContentList[index].krOrKpTeacher;
        $scope.syllabusContentList[index].connectTeacherStudentDTOList.forEach(function (el) {
            el.krOrKpTeacher = setValue;
        });
        $scope.checkChanges(index);
    };
    $scope.setGlobalKrOrKpTeacher = function (index) {
        let array = $scope.syllabusContentList[index].connectTeacherStudentDTOList, length = array.length;
        if (length < 1 || !array[0].krOrKpTeacher)
            return;
        let el = array[0].krOrKpTeacher.id, i= 1;
        for(; i < length; i++)
            if (el !== array[i].krOrKpTeacher.id) break;
        if (i === length) $scope.syllabusContentList[index].krOrKpTeacher = array[0].krOrKpTeacher;
        else $scope.syllabusContentList[index].krOrKpTeacher = null;
    };

    // Студенты
    $scope.selectedAdmittanceTeacherWithDTO = function (index) {
        $scope.setGlobalAdmittanceTeacher(index);
        $scope.checkChanges(index);
    };

    $scope.selectedExamTeacherWithDTO = function (index) {
        $scope.setGlobalExamTeacher(index);
        $scope.checkChanges(index);
    };

    $scope.selectedKrOrKpTeacherWithDTO = function (index) {
        $scope.setGlobalKrOrKpTeacher(index);
        $scope.checkChanges(index);
    };
    // Студенты


    $scope.saveTeachersByDiscipline = function (index) {
        console.log(index)
        console.log($scope.syllabusContentList[index])
        // console.log($scope.syllabusContentList[index].id)
        // console.log($scope.syllabusContentList[index].connectTeacherStudentDTOList[0])
        $http({
            method: 'PATCH',
            url: '/saveTeachersByDiscipline',
            /*params: {
                syllabusContentId: $scope.syllabusContentList[index].id,
                connectTeacherStudentDTOList: $scope.syllabusContentList[index].connectTeacherStudentDTOList[0]
            },*/
            data: angular.toJson($scope.syllabusContentList[index]),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                /*$scope.setGlobalAdmittanceTeacher(index);
            $scope.setGlobalExamTeacher(index);
            $scope.setGlobalKrOrKpTeacher(index);
            $scope.oldSyllabusContentList[index] = angular.copy(//$scope.syllabusContentList[index]);
            $scope.checkChanges(index);*/
            }
        );
    }

    /*$scope.selectedOnGroupList = function () {
        // if(!$scope.statementsForm.group.$modelValue)
        //     return;
        $http({
            method: 'PATCH',
            url: '/selectedOnGroupList',
            params: {
                id: $scope.statementsForm.group.$modelValue.id,
                groupId: $scope.forms.statementsForm.group.$modelValue.id
            }
        }).then(
            function(res) {
                $scope.groups = res.data;
            }
        );
    };*/

});