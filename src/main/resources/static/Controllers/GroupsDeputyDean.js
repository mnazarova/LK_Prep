app.controller("GroupsDeputyDeanController", function($scope, $http, $filter) {

    $scope.minDate = new Date().setDate(new Date().getDate()+1);
    $scope.deadline = new Date(new Date($scope.minDate).toISOString().split("T")[0]);
    $scope.deadline.setHours(0);

    // $scope.groupsForm = {};
    $scope.semesterNumberSet = [];
    $scope.estimatedCurSemesterNumber = [];

    $scope.changes = [];
    $scope.syllabuses = {};
    $scope.oldSyllabuses = {};

    getSyllabusesByDeputyDeanId();
    function getSyllabusesByDeputyDeanId() {
        $http({
            method: 'PATCH',
            url: '/getSyllabusesByDeputyDeanId'
        }).then(
            function(res) { // success
                $scope.syllabuses = res.data;
                let lengthSyllabuses = $scope.syllabuses.length;
                for(let i = 0; i < lengthSyllabuses; i++) {
                    setGlobalValue(i, $scope.syllabuses);
                    $scope.oldSyllabuses[i] = angular.copy($scope.syllabuses[i]);
                    checkChanges(i);

                    getSemesterNumberSetBySyllabusIdForDeputyDean($scope.syllabuses[i].id, i);
                    getEstimatedCurSemesterNumberForDeputyDean($scope.syllabuses[i].id, i);
                }
                // console.log($scope.semesterNumberSet)
                // console.log($scope.estimatedCurSemesterNumber)
            }
        );
    }

    function setGlobalValue(index, sList) {
        if (sList[index].groups) sList[index].globalActive = sList[index].groups[0].active;
        setGlobalCurSemester(index, sList);

        // console.log('before', sList[index].deadline)
        if (sList[index].deadline) sList[index].deadline = new Date(sList[index].deadline);
        // console.log('after', sList[index].deadline)
    }
    function setGlobalCurSemester(index, sList) {
        let array = sList[index].groups;
        let length = array.length;
        if (length < 1 || !array[0].curSemester)
            return;
        let el = array[0].curSemester, i= 1;
        for(; i < length; i++)
            if (!array[i].curSemester || el !== array[i].curSemester) break;
        if (i === length) sList[index].globalCurSemester = array[0].curSemester;
        else sList[index].globalCurSemester = null;
    }

    function getSemesterNumberSetBySyllabusIdForDeputyDean(syllabusId, index) {
        $http({
            method: 'PATCH',
            url: '/getSemesterNumberSetBySyllabusIdForDeputyDean',
            params: { syllabusId: syllabusId }
        }).then(
            function(res) {
                $scope.semesterNumberSet[index] = res.data;
            }
        );
    }

    function getEstimatedCurSemesterNumberForDeputyDean(syllabusId, index) {
        $http({ method: 'PATCH', url: '/getEstimatedCurSemesterNumberForDeputyDean', params: { syllabusId: syllabusId }
        }).then( function(res) { $scope.estimatedCurSemesterNumber[index] = res.data; } );
    }

    function checkChanges(index) {
        $scope.changes[index] = !angular.equals($scope.syllabuses[index], $scope.oldSyllabuses[index]);
        // console.log($scope.syllabuses[index])
        // console.log($scope.oldSyllabuses[index])
    }

    $scope.changeGlobalActive = function (index) {
        let valueActive = $scope.syllabuses[index].globalActive;
        $scope.syllabuses[index].groups.forEach(function(item) {
            item.active = valueActive;
        });
        checkChanges(index);
    };

    $scope.selectedGlobalCurSemester = function (index) {
        let valueCurSemester = $scope.syllabuses[index].globalCurSemester;
        $scope.syllabuses[index].groups.forEach(function(item) {
            item.curSemester = valueCurSemester;
        });
        checkChanges(index);
    };




    $scope.selectedDeadline = function (index) {
        checkChanges(index);
        // console.log($scope.groupsForm)
    };

    $scope.saveSyllabusForDeputyDean = function (index) {
        if (!$scope.changes[index]) {
            $scope.toasterWarning('Изменения отсутствуют');
            return;
        }
        if ($scope.groupsForm['deadline_' + index].$error.min || $scope.groupsForm['deadline_' + index].$dirty && $scope.groupsForm['deadline_' + index].$invalid) {
            $scope.toasterWarning('Введена некорректная дата');
            // $scope.toasterWarning('Введена некорректная дата! Минимально допустимое значение: ' + $scope.minDate);
            return;
        }
        $http({
            method: 'PATCH',
            url: '/saveSyllabusForDeputyDean',
            data: angular.toJson($scope.syllabuses[index]),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) {
                $scope.toasterSuccess('', 'Изменения по выбранной группе успешно сохранены');
                $scope.oldSyllabuses[index] = angular.copy(res.data);
                setGlobalValue(index, $scope.oldSyllabuses);
                checkChanges(index);
            }
        );
    };

});