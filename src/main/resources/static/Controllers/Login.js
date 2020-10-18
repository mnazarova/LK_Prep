/*
angular.module("Login",[])
    .controller("LoginController", function($scope, $http, $state) {
        $scope.auth = {
            login: "",
            password: ""
        };

        $scope.addClient = function() {

            var method = "";
            var url = "";
            /!*if (!$scope.auth.login || !$scope.auth.password) {
                alert("Форма заполнена некорректно!");
                return;
            }*!/
            console.log($scope.auth);


            /!*$http({
                method: "POST",
                url: '/login',
                data: angular.toJson($scope.auth),
                headers: {
                    'Content-Type': 'application/json'
                }
            // }).then(_success, _error);*!/
            // $state.go('registration');
        };

        function _success(res) {
            // _refreshEmployeeData();
            _clearFormData();
        }

        function _clearFormData() {
            $scope.auth.login = "";
            $scope.auth.password = "";
        }











        /!*$scope.createEmployee = function() {
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
        function _error(res) {
            var data = res.data;
            var status = res.status;
            var header = res.header;
            var config = res.config;
            alert("Error: " + status + ":" + data);
        }*!/
    });*/
