var app = angular.module("app", [
    'ui.router',
    'blockUI',
    'toaster',
    'ngAnimate',
    'ngSanitize',
    'ui.select',
    'ui.bootstrap'
]);

app.config(function(blockUIConfig) {

    blockUIConfig.message = 'Пожалуйста, подождите...';
    blockUIConfig.delay = 500;
    // blockUIConfig.autoBlock = false;

    /*blockUIConfig.requestFilter = function(config) {

        if (
            config.url.match(/^api\/dashboard($|\/).*!/) ||
            config.url.match(/^api\/billing\/session\/inprogress($|\/).*!/) ||
            config.url.match(/^api\/vckp\/coin\/session($|\/).*!/) ||
            config.url.match(/^ver.txt($|\/).*!/)
        ) {
            return false;
        }

    }*/

});

app.controller('ModalController',function ($scope, $uibModal, /*$uibModalInstance,*/ $sce) {

    /*$scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    };*/

    $scope.open = function(config, options) {
        options = options || {};
        config.body = $sce.trustAsHtml(config.body);

        var modalInstance = $uibModal.open({
            templateUrl: 'modalTemplate.html',
            backdrop: 'static',
            keyboard: false,
            windowClass: options.windowClass || '',
            controller: function ($scope, $uibModalInstance, config) {
                $scope.config = config;
                $scope.close = function () {
                    $uibModalInstance.dismiss('close');
                };
                if (!config.noFooter && config.buttons && config.buttons.length) {
                    for (var i = 0; i < config.buttons.length; i++) {
                        var btn = angular.copy(config.buttons[i]);
                        if (!btn.noClose) {
                            config.buttons[i].click = function () {
                                if (typeof this.action == 'function') {
                                    this.action();
                                }
                                $scope.close();
                            }
                        }
                    }
                }
            },
            resolve: {
                config: function () {
                    return config;
                }
            }
        });
        modalInstance.result.then(function(){}, function(){ /* console.log('Modal dismissed at: ' + new Date()); */ });

    };

    $scope.error = function(message, details) {
        var b = [
            {text: 'OK', cls: 'btn-primary'}
        ];
        if(details) {
            b.unshift({text: 'Подробнее', cls: 'btn-default', action: details});
        }
        $scope.open({
            noHeader: 'false',
            body: '<i class="fa fa-times-circle fa-2x text-danger pull-left"></i><div class="modalText text-justify">' + message + '</div>',
            buttons:b
        });
    };

    $scope.confirm = function(message, yes) {
        $scope.open({
            noHeader: 'true',
            body: '<i class="fa fa-question-circle fa-2x text-info pull-left"></i><div class="modalText text-justify">' + message + '</div>',
            buttons: [
                {text: 'Да', action: yes, cls: 'btn-primary'},
                {text: 'Нет'}
            ]
        });
    };

    $scope.confirmYesAndNo = function(message, textBtnYes, textBtnNo, styleBtnYes, styleBtnNo, yes, no) {
        // if()
        $scope.open({
            noHeader: 'true',
            body: '<i class="fa fa-4x fa-exclamation-triangle text-warning pull-left my-1"></i><div class="modalText text-justify">' + message + '</div>',
            buttons: [
                {text: textBtnNo, action: no, cls: styleBtnNo },//'btn-primary'},
                {text: textBtnYes, action: yes, cls: styleBtnYes }//'btn-primary'}
            ]
        });
    };

    $scope.areYouSure = function(yes) {
        $scope.open({
            noHeader: 'true',
            body: '<i class="fa fa-question-circle fa-2x text-danger pull-left"></i><div class="modalText text-justify">Вы уверены?</div>',
            buttons: [
                {text: 'Да', action: yes, cls: 'btn-danger'},
                {text: 'Нет'}
            ]
        });
    };

    $scope.errorPanel = function(header, message) {
        $scope.open({
            noHeader: 'true',
            body:
                '<div class="panel panel-danger">' +
                '<div class="panel-heading">' + header + '</div>' +
                '<div class="panel-body">' + message + '</div>' +
                '</div>',
            buttons: [
                {text: 'ОК'}
            ]
        });
    };

    $scope.alert = function(message) {
        $scope.open({
            noHeader: 'true',
            body: '<i class="fa fa-info-circle fa-2x text-info pull-left"></i><div class="modalText">'+message+'</div>',
            buttons: [
                {text: 'ОК'}
            ]
        });
    };


    /*$('#myModal').on('shown.bs.modal', function () {
        $('#myInput').trigger('focus')
    })*/
    // $('#modalWindowForLK').on('show.bs.modal', function (event) {



    /*$scope.openModal = function(message, yes, no) {
        console.log("message", message)
        console.log("yes", yes)
        console.log("no", no)
        $('#exampleModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget); // Button that triggered the modal
            // var click = button.data('click');
            // console.log(click)
            var label = button.data('label');
            var content = button.data('content'); // Extract info from data-* attributes
            // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
            // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
            var modal = $(this)
            modal.find('.modal-title').text(content)
            modal.find('.modal-body label').text(label)
            modal.find('.modal-body div').text(content)

            modal.find('.modal-body input').val(content)

            modal.find('#yes').attr('href', $(event.relatedTarget).data('href'));//http://www.google.com'));

            modal.find('#yes').on("click", function (e, yes, no, $state) {

                // this.action();
                console.log("button pressed");
                console.log(e);
                console.log(yes);
                console.log(no);
            })
        })
    };*/





        /*$("#myModal a.btn").on("click", function(e) {
            console.log("button pressed");   // just as an example...
            $("#myModal").modal('hide');     // dismiss the dialog
        });*/

        // $('.debug-url').html('Delete URL: <strong>' + $(this).find('#yes').attr('href') + '</strong>');
        /*$('#save').click(function() {
            save = function () {
                $('#exampleModal').modal('hide');
            }, function () {

            }
        });*/

        /*$("#yes").on("click", function(e){
            e.preventDefault(); // prevent de default action, which is to submit
            // save your value where you want
            data.select = $("#exampleSelect").value();
            // or call the save function here
            // save();
            $(this).prev().click();*/
        // modal.find('.modal-footer a').click("https://www.googole.com")



    $scope.$root['ModalController'] = $scope;

});

app.controller('AppController',function ($scope, $state, $http, $window, toaster) {

    /* Подключение tooltip на всех страницах */
    /*$(function () { $('[data-toggle="tooltip"]').tooltip(); });*/
    $(document).ready(function() {
        $("body").tooltip({ selector: '[data-toggle=tooltip]' });
    });

    /* Подключение select2 */
    $(function() {
        $('select').select2();
    });

    $(document).ready(function(placeholder) {
        $('js-example-placeholder-single').select2({
            placeholder: placeholder,
            allowClear: true
        });
    });

    $(document).ready(function(placeholder) {
        $('js-example-basic-single').select2({
            placeholder: placeholder,
            allowClear: true
        });
    });

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
            function (res) {
                $scope.counter = 0;
                $scope.allRoles = [];
                $scope.allRoles = res.data;

                if ($scope.allRoles.length)
                    $scope.ur = $scope.allRoles[$scope.counter];

                if ($scope.allRoles.length > 1) {
                    $scope.multipleRoles = true;
                    setNameNextRole();
                }
            }
        );
    }

    function setNameNextRole() {
        if (!$scope.allRoles[$scope.counter + 1]) // узнать название след роли, но не переключаться
            $scope.nextRole = $scope.allRoles[0];
        else // если след роль есть ($scope.counter + 1)
            $scope.nextRole = $scope.allRoles[$scope.counter + 1];
        if ($scope.nextRole === 'DEANERY')
            $scope.nameNextRole = "Деканат";
        else
            if ($scope.nextRole === 'TEACHER')
                $scope.nameNextRole = "Преподаватель";
            else
                if ($scope.nextRole === 'SECRETARY')
                    $scope.nameNextRole = "Секретарь";
                else
                    if ($scope.nextRole === 'DEPUTY_DEAN')
                        $scope.nameNextRole = "Зам. декана";
    }

    $scope.changeRole = function (count, transition) {
        // console.log(count);
        $scope.counter = count;
        if (!$scope.allRoles[$scope.counter])
            $scope.counter = 0;
        $scope.ur = $scope.allRoles[$scope.counter];
        setNameNextRole();
        if(transition)
            $state.go("help");
    };

    $scope.checkRole = function (roleName) {
        // console.log($scope.allRoles)
        if ($scope.ur === roleName)
            return;
        // console.log(roleName)
        $scope.index = -1;
        $scope.allRoles.forEach(function(el, index) {
            if (el === roleName)
                $scope.index = index;
        });
        if($scope.index === -1)
            return;
        $scope.changeRole($scope.index, false);
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

app.config(function ($stateProvider, $urlRouterProvider, $locationProvider, $sceProvider) {

    $sceProvider.enabled(false);// для uib-tooltip

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
        .state('assignment', {
            url: '/assignment',
            templateUrl: 'Templates/assignment.html',
            controller: 'AssignmentController'
        })
        .state('arrangeCertification', {
            url: '/arrangeCertification',
            templateUrl: 'Templates/arrangeCertification.html',
            controller: 'ArrangeCertificationController'
        })
        .state('viewAttestationsDeanery', { // все аттестации
            url: '/viewAttestationsDeanery',
            templateUrl: 'Templates/viewAttestationsDeanery.html',
            controller: 'ViewAttestationsDeaneryController'
        })
        .state('attestationStatementsDeanery', {
            url: '/attestationStatementsDeanery/:id',
            templateUrl: 'Templates/attestationStatementsDeanery.html',
            controller: 'AttestationStatementsDeaneryController'
        })
        .state('attestationStatementDeanery', { // просмотр аттестационной ведомости представителем деканата
            url: '/attestationStatementDeanery/:attestationId/:certificationAttestationId',
            /*params: {
                id: null,
                attestationId: null
            },*/
            templateUrl: 'Templates/attestationStatementDeanery.html',
            controller: 'AttestationStatementDeaneryController'
        })
        .state('sessionStatementsDeanery', {
            url: '/sessionStatementsDeanery',
            templateUrl: 'Templates/sessionStatementsDeanery.html',
            controller: 'SessionStatementsDeaneryController'
        })
        .state('sessionStatementDeanery', { // просмотр сессионной ведомости представителем деканата
            url: '/sessionStatementDeanery/:sessionSheetId',
            templateUrl: 'Templates/sessionStatementDeanery.html',
            controller: 'SessionStatementDeaneryController'
        })
        .state('groupsDeanery', {
            url: '/groupsDeanery',
            templateUrl: 'Templates/groupsDeanery.html',
            controller: 'GroupsDeaneryController'
        })
        .state('studentsDeanery', {
            url: '/groupsDeanery/:groupId',
            templateUrl: 'Templates/studentsDeanery.html',
            controller: 'StudentsDeaneryController'
        })
        /* DEPUTY_DEAN */
        .state('viewAttestationsDeputyDean', { // все аттестации
            url: '/viewAttestationsDeputyDean',
            templateUrl: 'Templates/viewAttestationsDeputyDean.html',
            controller: 'ViewAttestationsDeputyDeanController'
        })
        .state('attestationStatementsDeputyDean', {
            url: '/attestationStatementsDeputyDean/:id',
            templateUrl: 'Templates/attestationStatementsDeputyDean.html',
            controller: 'AttestationStatementsDeputyDeanController'
        })
        .state('attestationStatementDeputyDean', { // просмотр аттестационной ведомости представителем деканата
            url: '/attestationStatementDeputyDean/:attestationId/:certificationAttestationId',
           templateUrl: 'Templates/attestationStatementDeputyDean.html',
            controller: 'AttestationStatementDeputyDeanController'
        })
        .state('deputyDeanSessionStatements', {
            url: '/deputyDeanSessionStatements',
            templateUrl: 'Templates/deputyDeanSessionStatements.html',
            controller: 'DeputyDeanSessionStatementsController'
        })
        .state('deputyDeanSessionStatement', { // просмотр сессионной ведомости представителем деканата
            url: '/deputyDeanSessionStatement/:sessionSheetId',
            templateUrl: 'Templates/deputyDeanSessionStatement.html',
            controller: 'DeputyDeanSessionStatementController'
        })
        .state('groupsDeputyDean', {
            url: '/groupsDeputyDean',
            templateUrl: 'Templates/groupsDeputyDean.html',
            controller: 'GroupsDeputyDeanController'
        })
        .state('studentsDeputyDean', {
            url: '/groupsDeputyDean/:groupId',
            templateUrl: 'Templates/studentsDeputyDean.html',
            controller: 'StudentsDeputyDeanController'
        })
        /*SECRETARY*/
        .state('linkSession', {
            url: '/linkSession',
            templateUrl: 'Templates/linkSession.html',
            controller: 'LinkSessionController'
        })
        .state('linkSessionId', {
            url: '/linkSession/:id',
            templateUrl: 'Templates/linkSessionId.html',
            controller: 'LinkSessionIdController'
        })
        .state('listAttestation', {
            url: '/listAttestation',
            templateUrl: 'Templates/listAttestation.html',
            controller: 'ListAttestationController'
        })
        .state('linkAttestation', {
            url: '/linkAttestation/:attestationId',
            templateUrl: 'Templates/linkAttestation.html',
            controller: 'LinkAttestationController'
        })
        .state('linkAttestationId', {
            url: '/linkAttestation/:attestationId/:id',
            templateUrl: 'Templates/linkAttestationId.html',
            controller: 'LinkAttestationIdController'
        })
        /*TEACHER*/
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
            url: '/contentAttestation/:isHead/:attestationId/:id',
            /*params: {
                isHead: false
            },*/
            /*params: {
                id: null,
                attestationId: null
            } ,*/
            templateUrl: 'Templates/contentAttestation.html',
            controller: 'ContentAttestationController'
        })
        .state('session', {
            url: '/session',
            templateUrl: 'Templates/session.html',
            controller: 'SessionController'
        })
        .state('contentSession', {
            url: '/contentSession/:isHead/:id',
            templateUrl: 'Templates/contentSession.html',
            controller: 'ContentSessionController'
        })

    /*$locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });*/

    // $locationProvider.html5Mode(true);
});


/*app.config(function ($provide, $httpProvider) {

    $httpProvider.useApplyAsync(true);

    $provide.factory('errHttpInterceptor', function ($q, $rootScope, HttpErrorHandlerService) {
        return {
            'responseError': function (rejection) {
                if (rejection.status === 504) {
                    $rootScope.APPController.goLogin();
                } else if (rejection.status === 401) {
                    $rootScope.APPController.goLogout();
                } else if (rejection.status === 403) {
                    var forbiddenUrl = rejection.config.url;
                    if (forbiddenUrl === 'api/login') {
                        // KAP-1834 user is disabled
                        $rootScope.ModalController.error('<b>Учетная запись заблокирована. Вход в систему невозможен. </b>' +
                            'Для получения доступа к системе необходимо обратиться к администратору АСУ.');
                    } else {
                        $rootScope.ModalController.error('Ошибка доступа');
                        $rootScope.APPController.goHome();
                    }
                } else if (rejection.status === 500 || rejection.status === 415) {
                    var customHandler = HttpErrorHandlerService.get(rejection.config.method, rejection.config.url);
                    if (customHandler) {
                        customHandler();
                    } else {
                        var msg = 'Произошла непредвиденная ошибка.',
                            title = '<b>' + rejection.status + ' ' + rejection.statusText + '</b>';
                        if (rejection.data) {
                            if (rejection.data.indexOf("flat account is closed") > -1) {
                                title = '<b>Операция невозможна!</b>';
                                msg = "Лицевой счёт закрыт или уже разделён.";
                            } else if (rejection.config.url === 'api/auctioncontract/sign') {
                                title = '<b>' + 'Договор был подписан ранее.' + '</b>';
                                msg = 'Субсидия уже создана.';
                            }
                        }
                        $rootScope.$root['ModalController'].error(title + '<br />' + msg,
                            function () {
                                var w = window.open();
                                w.document.write(rejection.data);
                                w.stop();
                            }
                        );
                    }
                } else if (rejection.status === 503) {
                    $rootScope.ModalController.error('Операция временно недоступна');
                } else if (rejection.data && rejection.data.message) {
                    $rootScope.$root['ModalController'].error('<b>' + rejection.status + ' ' + rejection.statusText + '</b><br />' + rejection.data.message);
                }
                return $q.reject(rejection);
            }
        }
    });

    $httpProvider.interceptors.push('errHttpInterceptor');
});*/

/** AngularJS default filter with the following expression: "person in people | filter: {name: $select.search, age: $select.search}"
 performs an AND between 'name: $select.search' and 'age: $select.search'. We want to perform an OR. */
app.filter('propsFilter', function() {
    return function(items, props) {
        var out = [];

        if (angular.isArray(items)) {
            var keys = Object.keys(props);

            items.forEach(function(item) {
                var itemMatches = false;

                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
});