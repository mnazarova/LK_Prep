app.controller("StatementsController", function($scope, $http) {

    getAllGroups();
    getMedicines();
    function getAllGroups() {
        $http({
            method: 'GET',
            url: '/getAllGroups'
        }).then(
            function(res) { // success
                $scope.groups = res.data;
                // console.log($scope.groups);
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function getMedicines() {
        $http({
            method: 'GET',
            url: '/getMedicines'
        }).then(
            function(res) { // success
                $scope.medicines = res.data;
                // console.log($scope.medicines);
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    $scope.findMedicines = function() {
        $scope.saveMedicineForm = true;

        var medName = $scope.medicine.name.$modelValue;
        var medGroupId = $scope.medicine.group.$modelValue;
        // var medPrice = $scope.medicine.price.$modelValue;

        /*if(!medName && !medGroupId) {
            console.log(1);
            return;
        }*/

        $scope.findDTO = {
            name: medName,
            groupId: medGroupId
        };
        // console.log($scope.findDTO);

        $http({
            method: 'PUT',
            url: '/findMedicines',
            data: angular.toJson($scope.findDTO),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) { // success
                $scope.medicines = res.data;
                // console.log($scope.medicines);
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    };

    $scope.addToBasket = function(medicineId) {
        console.log(medicineId)
        $http({
            method: 'PUT',
            url: '/addToBasket',
            data: angular.toJson(medicineId),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) { // success
                $scope.medicines = res.data;
                getCurrentOrder();
                // console.log($scope.medicines);
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    getCurrentOrder();
    function getCurrentOrder () {
        $http({
            method: 'GET',
            url: '/getCurrentOrder'
        }).then(
            function(res) { // success
                $scope.currentOrder = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    $scope.transitionMedicineId = function(medicineId) {
        console.log(medicineId)
        $http({
            method: 'PUT',
            url: '/transitionMedicineId',
            data: angular.toJson(medicineId),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function(res) { // success
                // $scope.medicines = res.data;
                // getCurrentOrder();
                // console.log($scope.medicines);
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }



    // $scope.clients = [];
    /*$scope.clientForm = {
        // empId: 1,
        surname: "",
        name: "",
        phone: "",
        email: "",
        login: "",
        password: ""
    };

    $scope.addClient = function() {

        var method = "";
        var url = "";
        if ($scope.clientForm.password_1 != $scope.clientForm.password_2) {
            alert("Пароли не совпадают!");
            return;
        }
        if ($scope.clientForm.invalid) {
            alert("Форма заполнена некорректно!");
            return;
        }


        $scope.clientForm.password = $scope.clientForm.password_1;
        delete $scope.clientForm.password_1;
        delete $scope.clientForm.password_2;

        console.log($scope.clientForm)


        $http({
            method: "POST",
            url: '/addClient',
            data: angular.toJson($scope.clientForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    function _success(res) {
        // _refreshEmployeeData();
        clearForm();
    }

    function clearForm() {
        $scope.clientForm.surname = "";
        $scope.clientForm.name = "";
        $scope.clientForm.phone = "";
        $scope.clientForm.email = "";
        $scope.clientForm.login = "";
        $scope.clientForm.password_1 = "";
        $scope.clientForm.password_2 = "";
    }

    function _error(res) {
        var data = res.data;
        var status = res.status;
        var header = res.header;
        var config = res.config;
        alert("Error: " + status + ":" + data);
    }

    // Now load the data from server
    _refreshEmployeeData();

    // HTTP POST/PUT methods for add/edit employee
    // Call: http://localhost:8080/employee
    $scope.submitEmployee = function() {

        var method = "";
        var url = "";

        if ($scope.employeeForm.empId == -1) {
            method = "POST";
            url = '/employee';
        } else {
            method = "PUT";
            url = '/employee';
        }

        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.employeeForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.createEmployee = function() {
        clearForm();
    };

    // HTTP DELETE- delete employee by Id
    // Call: http://localhost:8080/employee/{empId}
    $scope.deleteEmployee = function(employee) {
        $http({
            method: 'DELETE',
            url: '/employee/' + employee.empId
        }).then(_success, _error);
    };

    // In case of edit
    $scope.editEmployee = function(employee) {
        $scope.employeeForm.empId = employee.empId;
        $scope.employeeForm.empNo = employee.empNo;
        $scope.employeeForm.empName = employee.empName;
    };

    // Private Method
    // HTTP GET- get all employees collection
    // Call: http://localhost:8080/employees
    function _refreshEmployeeData() {
        $http({
            method: 'GET',
            url: '/employees'
        }).then(
            function(res) { // success
                $scope.employees = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }*/


});