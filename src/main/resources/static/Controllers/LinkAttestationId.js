app.controller("LinkAttestationIdController", function($scope, $http, $stateParams) {
    $scope.showStudents = [];
    $scope.changes = [];
    $scope.syllabusContentList = {};
    $scope.oldSyllabusContentList = {};

    attestationGetGroupById();
    function attestationGetGroupById() {
        $http({
            method: 'PATCH',
            url: '/attestationGetGroupById',
            params: { groupId: $stateParams.id }
        }).then( function(res) {$scope.group = res.data;});
    }

    attestationGetAttestationById();
    function attestationGetAttestationById() {
        $http({
            method: 'PATCH',
            url: '/attestationGetAttestationById',
            params: { attestationId: $stateParams.attestationId }
        }).then( function(res) {$scope.attestation = res.data;});
    }

    attestationGetTeacherListByDepartmentId();
    function attestationGetTeacherListByDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/attestationGetTeacherListByDepartmentId'
        }).then(
            function(res) {
                $scope.teachers = res.data;
                $scope.teacher = {};
            }
        );
    }

    getSyllabusContentListWithAttestationByGroupIdAndDepartmentId();
    function getSyllabusContentListWithAttestationByGroupIdAndDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/getSyllabusContentListWithAttestationByGroupIdAndDepartmentId',
            params: {
                groupId: $stateParams.id,
                attestationId: $stateParams.attestationId
            }
        }).then(
            function(res) {
                if (!res.data)
                    $scope.toasterError('В группе отсутствуют студенты. Обратитесь к администратору!');

                $scope.syllabusContentList = res.data;
                initTable();
            },
            function(res) { // error
                // console.log(res.data)
                if (res.data === 0)
                    $scope.toasterError('Отсутствует номер текущего семестра группы.');
                else
                    if (res.data === 2)
                        $scope.toasterError('В выбранную аттестацию запрещено вносить изменения!');
                    else
                        $scope.toasterError('Обратитесь к администратору!');
            }
        );
    }

    function initTable() {
        // console.log($scope.syllabusContentList);
        for (let index=0, size = Object.keys($scope.syllabusContentList).length; index < size; index++) {
            $scope.setGlobalAttestationTeacher(index, $scope.syllabusContentList);
            // $scope.setGlobalAdmittanceTeacher(index, $scope.syllabusContentList);
            // $scope.setGlobalExamTeacher(index, $scope.syllabusContentList);
            // $scope.setGlobalKrOrKpTeacher(index, $scope.syllabusContentList);
            $scope.oldSyllabusContentList[index] = angular.copy($scope.syllabusContentList[index]);
            $scope.checkChanges(index);
        }
    }

    $scope.checkChanges = function (index) {
        $scope.changes[index] = !angular.equals($scope.oldSyllabusContentList[index], $scope.syllabusContentList[index]); // true если равны
        // console.log($scope.changes[index])
        // console.log($scope.oldSyllabusContentList[index])
        // console.log($scope.syllabusContentList[index])
    };

    $scope.toggleShowStudents = function (index) {
        $scope.showStudents[index] = !$scope.showStudents[index];
    };

    // Содержание учебного плана
    $scope.selectedAttestationTeacher = function (index) {
        let setValue = $scope.syllabusContentList[index].attestationTeacher;
        $scope.syllabusContentList[index].connectTeacherStudentDTOList.forEach(function (el) {
            el.attestationTeacher = setValue;
        });
        $scope.checkChanges(index);
    };
    $scope.setGlobalAttestationTeacher = function (index, scList) {
        let array = scList[index].connectTeacherStudentDTOList;
        let length = array.length;
        if (length < 1 || !array[0].attestationTeacher)
            return;
        let el = array[0].attestationTeacher.id, i= 1;
        for(; i < length; i++)
            if (!array[i].attestationTeacher || el !== array[i].attestationTeacher.id) break;
        if (i === length) scList[index].attestationTeacher = array[0].attestationTeacher;
        else scList[index].attestationTeacher = null;
    };

    /*$scope.selectedAdmittanceTeacher = function (index) {
        let setValue = $scope.syllabusContentList[index].admittanceTeacher;
        $scope.syllabusContentList[index].connectTeacherStudentDTOList.forEach(function (el) {
            el.admittanceTeacher = setValue;
        });
        $scope.checkChanges(index);
    };
    $scope.setGlobalAdmittanceTeacher = function (index, scList) {
        let array = scList[index].connectTeacherStudentDTOList;
        let length = array.length;
        if (length < 1 || !array[0].admittanceTeacher)
            return;
        let el = array[0].admittanceTeacher.id, i= 1;
        for(; i < length; i++)
            if (el !== array[i].admittanceTeacher.id) break;
        if (i === length) scList[index].admittanceTeacher = array[0].admittanceTeacher;
        else scList[index].admittanceTeacher = null;
    };*/


    // Студенты
    $scope.selectedAttestationTeacherWithDTO = function (index) {
        $scope.setGlobalAttestationTeacher(index, $scope.syllabusContentList);
        $scope.checkChanges(index);
    };


    $scope.saveAttestationTeachers = function (index) {

        if (!$scope.changes[index]) {
            $scope.toasterWarning('По выбранной дисциплине изменения отсутствуют');
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveAttestationTeachers',
            params: {
                groupId: $stateParams.id,
                attestationId: $stateParams.attestationId
                // syllabusContentId: $scope.syllabusContentList[index].id,
                // connectTeacherStudentDTOList: $scope.syllabusContentList[index].connectTeacherStudentDTOList[0]
            },
            data: angular.toJson($scope.syllabusContentList[index]),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                // console.log(res.data)
                $scope.toasterSuccess('', 'Изменения по выбранной дисциплине успешно сохранены');
                $scope.oldSyllabusContentList[index] = angular.copy(res.data);
                $scope.setGlobalAttestationTeacher(index, $scope.oldSyllabusContentList);
                $scope.checkChanges(index);
            }
        );
    }
});