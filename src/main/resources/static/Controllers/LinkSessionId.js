app.controller("LinkSessionIdController", function($scope, $http, $state, $stateParams) {

    $scope.checkRole("SECRETARY");

    $scope.showStudents = [];
    $scope.changes = [];
    $scope.syllabusContentList = {};
    $scope.oldSyllabusContentList = {};

    sessionGetGroupById();
    function sessionGetGroupById() {
        $http({
            method: 'PATCH',
            url: '/sessionGetGroupById',
            params: {
                groupId: $stateParams.id
            }
        }).then(
            function(res) {
                $scope.group = res.data;
            }
        );
    }

    sessionGetTeacherListByDepartmentId();
    function sessionGetTeacherListByDepartmentId() {
        $http({
            method: 'PATCH',
            url: '/sessionGetTeacherListByDepartmentId'
        }).then(
            function(res) {
                $scope.teachers = res.data;
                $scope.teacher = {};
                // console.log($scope.teachers)
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
                if (!res.data) {
                    $scope.toasterError('В группе отсутствуют студенты. Обратитесь к администратору!');
                    $state.go('linkSession');
                }

                $scope.syllabusContentList = res.data;
                initTable();
            },
            function(res) { // error
                // console.log(res.data)
                if (res.data === 0)
                    $scope.toasterError('Отсутствует номер текущего семестра группы.');
                else
                    $scope.toasterError('Обратитесь к администратору!');
            }
        );
    }

    function initTable() {
        // console.log($scope.syllabusContentList);
        for (let index=0, size = Object.keys($scope.syllabusContentList).length; index < size; index++) {
            $scope.setGlobalAdmittanceTeacher(index, $scope.syllabusContentList);
            $scope.setGlobalExamTeacher(index, $scope.syllabusContentList);
            $scope.setGlobalKrOrKpTeacher(index, $scope.syllabusContentList);
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
    $scope.selectedAdmittanceTeacher = function (index) {
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
            if (!array[i].admittanceTeacher || el !== array[i].admittanceTeacher.id) break;
        if (i === length) scList[index].admittanceTeacher = array[0].admittanceTeacher;
        else scList[index].admittanceTeacher = null;
    };

    $scope.selectedExamTeacher = function (index) {
        let setValue = $scope.syllabusContentList[index].examTeacher;
        $scope.syllabusContentList[index].connectTeacherStudentDTOList.forEach(function (el) {
            el.examTeacher = setValue;
        });
        $scope.checkChanges(index);
    };
    $scope.setGlobalExamTeacher = function (index, scList) {
        let array = scList[index].connectTeacherStudentDTOList;
        let length = array.length;
        // console.log(array[0])
        if (length < 1 || !array[0].examTeacher)
            return;
        let el = array[0].examTeacher.id, i= 1;
        for(; i < length; i++) {
            // console.log(!array[i].examTeacher)
            if (!array[i].examTeacher || el !== array[i].examTeacher.id) break;
        }
        if (i === length) scList[index].examTeacher = array[0].examTeacher;
        else scList[index].examTeacher = null; // break

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
    $scope.setGlobalKrOrKpTeacher = function (index, scList) {
        let array = scList[index].connectTeacherStudentDTOList, length = array.length;
        if (length < 1 || !array[0].krOrKpTeacher)
            return;
        let el = array[0].krOrKpTeacher.id, i= 1;
        for(; i < length; i++)
            if (!array[i].krOrKpTeacher || el !== array[i].krOrKpTeacher.id) break;
        if (i === length) scList[index].krOrKpTeacher = array[0].krOrKpTeacher;
        else scList[index].krOrKpTeacher = null;
    };

    // Студенты
    $scope.selectedAdmittanceTeacherWithDTO = function (index) {
        $scope.setGlobalAdmittanceTeacher(index, $scope.syllabusContentList);
        $scope.checkChanges(index);
    };

    $scope.selectedExamTeacherWithDTO = function (index) {
        $scope.setGlobalExamTeacher(index, $scope.syllabusContentList);
        $scope.checkChanges(index);
    };

    $scope.selectedKrOrKpTeacherWithDTO = function (index) {
        $scope.setGlobalKrOrKpTeacher(index, $scope.syllabusContentList);
        $scope.checkChanges(index);
    };
    // Студенты


    $scope.saveTeachersByDiscipline = function (index) {

        if (!$scope.changes[index]) {
            $scope.toasterWarning('По выбранной дисциплине изменения отсутствуют');
            return;
        }

        $http({
            method: 'PATCH',
            url: '/saveTeachersByDiscipline',
            params: {
                groupId: $stateParams.id
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
                $scope.setGlobalAdmittanceTeacher(index, $scope.oldSyllabusContentList);
                $scope.setGlobalExamTeacher(index, $scope.oldSyllabusContentList);
                $scope.setGlobalKrOrKpTeacher(index, $scope.oldSyllabusContentList);
                $scope.checkChanges(index);
            }
        );
    }
});