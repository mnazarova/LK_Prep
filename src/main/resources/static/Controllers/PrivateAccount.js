// angular.module("PrivateAccount",[])
    app.controller("PrivateAccountController", function($scope, $http) {

        getDataUser();

        function getDataUser() {
            $http({
                method: 'GET',
                url: '/getDataUser'
            }).then(
                function(res) { // success
                    $scope.user = res.data;
                    $scope.ur = res.data.roles[0];
                    getData();
                    // console.log($scope.user);
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
        }

        function getData() {
            if($scope.ur == 'MANAGER') {
                $http({
                    method: 'GET',
                    url: '/getDataEmployee'
                }).then(
                    function (res) { // success
                        $scope.client = res.data;
                        // console.log($scope.client);
                    },
                    function (res) { // error
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
            }
            else {
                $http({
                    method: 'GET',
                    url: '/getDataClient'
                }).then(
                    function (res) { // success
                        $scope.client = res.data;
                        // console.log($scope.client);
                    },
                    function (res) { // error
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
            }
         }

        function updClient() {
            $http({
                method: 'PUT',
                url: '/updateClient',
                data: angular.toJson($scope.client),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(
                function(res) { // success
                    getData();
                    $scope.client = res.data;
                    // console.log($scope.client);
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
        }

        $scope.updateClient = function() {
            $scope.success = false;
            $scope.saveEditForm = true;
            if($scope.editUser.surname.$invalid || $scope.editUser.name.$invalid ||
                $scope.editUser.phone.$invalid || $scope.editUser.login.$invalid)
                return;

            if($scope.ur == 'USER' && $scope.editUser.email.$invalid)
                return;

            if($scope.editUser.password_1.$modelValue && $scope.editUser.password_2.$modelValue &&
                $scope.editUser.password_1.$modelValue != $scope.editUser.password_2.$modelValue)
                return;


            $scope.userDTO = {
                username: $scope.user.username,
                password: $scope.user.password_1
            };
            // console.log($scope.userDTO);
            $http({
                method: 'PUT',
                url: '/updateUser',
                data: angular.toJson($scope.userDTO),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(
                function(res) { // success
                    getDataUser();
                    $scope.user = res.data;
                    $scope.success = true;
                    // console.log($scope.client);
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );

            if($scope.ur == 'MANAGER') {
                $http({
                    method: 'PUT',
                    url: '/updateEmployee',
                    data: angular.toJson($scope.client),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(
                    function(res) { // success
                        getData();
                        $scope.client = res.data;
                        // console.log($scope.client);
                    },
                    function(res) { // error
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
            }
            else
                updClient();
        };


        getCurrentOrder();
        function getCurrentOrder () {
            if($scope.ur == 'MANAGER')
                return;
            $http({
                method: 'GET',
                url: '/getCurrentOrder'
            }).then(
                function(res) { // success
                    $scope.medicines = res.data;
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
        }
    });

/*
// Controller Part
angular.module("PrivateAccount",[])
    .controller("PrivateAccountController", function($scope, $http) {


    $scope.employees = [];
    $scope.employeeForm = {
        empId: 1,
        empNo: "",
        empName: ""
    };

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
        _clearFormData();
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
    }

    function _success(res) {
        _refreshEmployeeData();
        _clearFormData();
    }

    function _error(res) {
        var data = res.data;
        var status = res.status;
        var header = res.header;
        var config = res.config;
        alert("Error: " + status + ":" + data);
    }

    // Clear the form
    function _clearFormData() {
        $scope.employeeForm.empId = -1;
        $scope.employeeForm.empNo = "";
        $scope.employeeForm.empName = ""
    }
});*/
