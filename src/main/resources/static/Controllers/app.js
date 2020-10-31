var app = angular.module("app", [
    'ui.router',
    'toaster',
    'ngAnimate',
    // 'ui.bootstrap',
    // 'request',
    // 'app.help'
]);

/*var app = angular.module("app", [
    'ui.router',
    'app.home'
    /!*'app.menu',
    'app.search'*!/
])*/

    app.controller('AppController',function ($scope, $http, $window, toaster) {
        /* Выпадающее меню */
        $(function() {
            var $menu_popup = $('.menu-popup');
            $menu_popup.slideUp(0);

            $(".menu-trigger, .menu-close").click(function(){
                $menu_popup.slideToggle(300, function(){
                    // console.log(1)
                    if ($menu_popup.is(':hidden')) {
                        $('body').removeClass('body_pointer');
                    } else {
                        $('body').addClass('body_pointer');
                    }
                });
                return false;
            });

            $(document).on('click', function(e){
                // console.log("check")
                if (!$(e.target).closest('.menu').length){
                    // console.log(2)
                    $('body').removeClass('body_pointer');
                    $menu_popup.slideUp(300);
                }
            });

            $(".nav-link").click(function(e){
                $menu_popup.slideUp(300);
                /*if ($menu_popup.is(':hidden')) {
                    console.log(1)
                    $menu_popup.slideUp(300);
                } else {
                    console.log(2)
                    $menu_popup.slideUp(0);
                }*/

                /*if (!$(e.target).closest('.menu').length) {
                    console.log(3)
                }
                else console.log(4)*/

            });
        });

        /*var request = require('request');
        request('http://www.google.com', function (error, response, body) {
            console.error('error:', error); // Print the error if one occurred
            console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
            console.log('body:', body); // Print the HTML for the Google homepage.
        });*/

        $scope.toasterSuccess = function(title, body) {
            toaster.success({title: title, body: body});
            // toaster.success({title: title, body: body, timeout: 15000});
        };

        $scope.toasterWarning = function(body) {
            // toaster.pop('warning', "Hi ", "{template: 'myTemplateWithData.html', data: 'MyData'}", 15000, 'templateWithData');
            toaster.warning("Внимание", body);
        };

        $scope.toasterError = function(body) {
            toaster.error('Ошибка', body);
            // toaster.error('', body);
        };

        $scope.toasterInfo = function(title, body) {
            toaster.pop('note', title, body);
            // toaster.error('', body);
        };

        $scope.toaster = function(){
            // toaster.pop('info', "title", "text");
            // toaster.pop('error', "error", "text");
            // toaster.pop('warning', "warning", "text");
            // toaster.pop('note', "note", "text");
            toaster.pop({type: 'wait', title: "title", body:"text"});
            toaster.pop('success', "title", '<ul><li>Render html</li></ul>', 5000, 'trustedHtml');
            toaster.pop('error', "title", '<ul><li>Render html</li></ul>', null, 'trustedHtml');
            toaster.pop('wait', "title", null, null, 'template');
            toaster.pop('warning', "title", "myTemplate.html", null, 'template');
            toaster.pop('note', "title", "text");
            toaster.pop('success', "title", 'Its address is https://google.com.', 5000, 'trustedHtml', function(toaster) {
                var match = toaster.body.match(/http[s]?:\/\/[^\s]+/);
                if (match) $window.open(match[0]);
                return true;
            });
        };

        $scope.goToLink = function(toaster) {
            var match = toaster.body.match(/http[s]?:\/\/[^\s]+/);
            if (match) $window.open(match[0]);
            return true;
        };

        $scope.clear = function(){
            toaster.clear();
        };


        getDataUser();
        function getDataUser() {
            $http({
                method: 'GET',
                url: '/getDataUser'
            }).then(
                function (res) { // success
                    $scope.ur = res.data;
                },
                function (res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
        }

        /*$rootScope.previousState;
        $rootScope.currentState;
        $rootScope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
            $rootScope.previousState = from.name;
            $rootScope.currentState = to.name;
            console.log('Previous state:'+$rootScope.previousState)
            console.log('Current state:'+$rootScope.currentState)
        });*/

    });

    app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $locationProvider.hashPrefix('');
        $urlRouterProvider.otherwise('/');
        // $urlRouterProvider.when('/users', '/users/list');
        // $urlRouterProvider.when('/flat-owners', '/flat-owners/list');

        /*$routeProvider
            .when('/arrangeCertification',
                {
                    templateUrl: 'Templates/arrangeCertification.html',
                    resolve: resolveController('/Controllers/Help.js')
                });*/

        $stateProvider
            /*.state('home', {
                url: '/',
                templateUrl: 'Templates/index.html',
                controller: 'HomeController'
            })*/
            .state('help', {
                url: '/',
                templateUrl: 'Templates/help.html',
                controller: 'HelpController'
            })
            .state('arrangeCertification', {
                url: '/arrangeCertification',
                templateUrl: 'Templates/arrangeCertification.html',
                controller: 'ArrangeCertificationController'
            })
            .state('statements', {
                url: '/statements',
                templateUrl: 'Templates/statements.html',
                controller: 'StatementsController'
            })
            .state('students', {
                url: '/students',
                templateUrl: 'Templates/students.html',
                controller: 'StudentsController'
            })
            .state('addSubgroup', {
                url: '/addSubgroup',
                templateUrl: 'Templates/addSubgroup.html',
                controller: 'AddSubgroupController'
            })
            .state('link', {
                url: '/link',
                templateUrl: 'Templates/link.html',
                controller: 'LinkController'
            })
            .state('attestation', {
                url: '/attestation',
                templateUrl: 'Templates/attestation.html',
                controller: 'AttestationController'
            })
            .state('subject', {
                url: '/subject/:id',
                templateUrl: 'Templates/subject.html',
                controller: 'SubjectController'
            })
            .state('contentAttestation', {
                url: '/contentAttestation',
                params: {
                    id: null,
                    attestationId: null
                } ,
                templateUrl: 'Templates/contentAttestation.html',
                controller: 'ContentAttestationController'
                // добавить переменную, определяющую группа или подгруппа
            })
            /*.state('fill', {
                url: '/fill/subject/:id',
                templateUrl: 'Templates/subject.html',
                controller: 'SubjectController'
            })*/
            /*.state('privateAccount', {
                url: '/privateAccount',
                templateUrl: 'Templates/privateAccount.html',
                controller: 'PrivateAccountController'
            })*/
            /*.state('statements', {
                url: '/statements',
                templateUrl: 'Templates/statements.html',
                controller: 'StatementsController'
            })*/
            /*.state('/', {
                url: '/',
                templateUrl: 'index.html',
                controller: 'APPController'
            })
           .state('home', {
                url: '/home',
                templateUrl: 'components/Templates/index.html',
                controller: 'HomeController'
            })
            .state('login', {
                url: '/login',
                templateUrl: 'components/Templates/login.html',
                controller: 'LoginController'
            })
            .state('registration', {
                url: '/registration',
                templateUrl: 'components/Templates/registration.html',
                controller: 'RegistrationController'
            })
            .state('help', {
                url: '/help',
                templateUrl: 'Templates/help.html',
                controller: 'Controllers/HelpController'
            })*/

        /*$locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });*/

        // $locationProvider.html5Mode(true);
    }
);