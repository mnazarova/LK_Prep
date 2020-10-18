angular.module("Registration",[])
    .controller("RegistrationController", function($scope, $http) {
        $scope.userForm = {
            /*name: "",
            email: "",*/
            login: "",
            password: ""
        };

        $scope.addUser = function() {

            var method = "";
            var url = "";
            if ($scope.userForm.password_1 != $scope.userForm.password_2) {
                alert("Пароли не совпадают!");
                return;
            }
            if ($scope.userForm.invalid) {
                alert("Форма заполнена некорректно!");
                return;
            }


            $scope.userForm.password = $scope.userForm.password_1;
            delete $scope.userForm.password_1;
            delete $scope.userForm.password_2;

            console.log($scope.userForm)


            $http({
                method: "POST",
                url: '/addUser',
                data: angular.toJson($scope.userForm),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(_success, _error);
        };

        function _success(res) {
            console.log("_success result=");
            console.log(res);
            // _refreshEmployeeData();
            clearForm();
        }

        function clearForm() {
            /*$scope.userForm.surname = "";
            $scope.userForm.name = "";
            $scope.userForm.phone = "";
            $scope.userForm.email = "";*/
            $scope.userForm.login = "";
            $scope.userForm.password_1 = "";
            $scope.userForm.password_2 = "";
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
        }


    });