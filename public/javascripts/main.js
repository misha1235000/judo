var judoApp = angular.module("judoApp", ['ngRoute']);

judoApp.controller('mainCont', ['$scope', '$http', function($scope, $http) {
    $scope.name;
    $scope.permlevel = "";
    $scope.usr = {'username':'','pass':'','firstName':'','lastName':'','email':'','perm':0};
    
    $http.get('/session').success(function(data) {
       $scope.usr = data;
       if($scope.usr.perm == 1) {
           $scope.permlevel = "משתמש רגיל";
       } else if ($scope.usr.perm == 2) {
           $scope.permlevel = "מאמן";
       } else if ($scope.usr.perm == 3) {
           $scope.permlevel = "מפתח";
       }
    }).error(function() {});
    
    $scope.login = function() {
    if ($("#username").val() == "") {
        $("#wronglog").html("שם משתמש לא יכול להיות ריק");
    } else if ($("#pass").val() == "") {
        $("#wronglog").html("סיסמא לא יכולה להיות ריקה");
    }
    $http.get('/login/' + $("#username").val() + '/' + $("#pass").val())
    .success(function(data) {
        if (data == "" || data == undefined || data == null) {
            $("#wronglog").html("שם משתמש או סיסמא אינם נכונים");
        } else {
            location.reload();
        }
    }).error(function () {
        })
    }
    
    $scope.logout = function() {
        $http.get('/logout')
        .success(function() {
            location.reload();
        }).error(function () {
        })
    }
    
    $scope.addNews = function() {
        $http.get('/news/add/' + $("#message").val()).success(function(data) {
           $scope.news.push(data);
         // location.reload();
        }).error(function() {
            console.log("A Problem occured while adding a message");
        });
    }
    
    $scope.deleteNew = function(id) {
        $http.get('/news/delete/' + id).success(function(data) {
      //      location.reload();
        }).error(function() {
            
        })
    }
    
    $scope.showNews = function() {
        $http.get('/news').success(function(data) {
            $scope.news = data;
        }).error(function() {
            console.log("Error in getting news.");
        })
    }
    
    $scope.showPics = function() {
        $http.get('/pics').success(function(data) {
            $scope.pics = data;
        }).error(function() {
            console.log("Error in getting pics.");
        })
    }
    
    $scope.checkValid = function(){
        var emailPattern    = RegExp(/^\w+@\w+\.\w+$/);
        var passwordPattern = RegExp(/^\w{7,15}$/);

        if ($("#regname").val() == "") {
            $("#wrongreg").html("שם פרטי לא יכול להיות ריק");
            return false;
        } else if ($("#reglastname").val() == "") {
            $("#wrongreg").html("שם משפחה לא יכול להיות ריק");
            return false;
        } else if ($("#regemail").val() == "") {
            $("#wrongreg").html("דואר אלקטרוני לא יכול להיות ריק");
            return false;
        } else if ($("#reguser").val() == "") { 
            $("#wrongreg").html("שם משתמש לא יכול להיות ריק");
            return false;
        } else if ($("#regpass").val() == "") {
            $("#wrongreg").html("סיסמא לא יכולה להיות ריקה");
            return false;
        } else if ($("#regpassconf").val() == "") {
            $("#wrongreg").html("אימות הסיסמא אינו יכול להיות ריק");
            return false;
        } else if (!emailPattern.test($("#regemail").val())) {
            $("#wrongreg").html("הדואר אלקטרוני אינו תקין");
            return false;
        } else if (!passwordPattern.test($("#regpass").val())) {
            $("#wrongreg").html("סיסמא אינה תקינה");
            return false;
        } else if ($("#regpass").val() != $("#regpassconf").val()) {
            $("#wrongreg").html("הסיסמא ואימותה אינם זהות");
            return false;
        } else {
         /*   $http.post("/register", [{'name':$("#regname").val()},
                                     {'lastname':$("#reglastname").val()},
                                     {'email': $("#regemail").val()},
                                     {'username':$("#reguser").val()},
                                     {'pass': $("#regpass").val()},
                                     {'passconf':$("#regpassconf").val()}])*/
              $http.get("/register/" + $("#regname").val() + "/" + $("#reglastname").val() + "/" + $("#regemail").val() + "/" + $("#reguser").val() + "/" + $("#regpass").val() + "/" + $("#regpassconf").val())
                .success(function(data) {
                    alert(data);
                    setTimeout(function() {
                        location.reload();
                    }, 3000);
              }).error  (function() {
              });
        }
    }
}]);

function init_map(){var myOptions = {zoom:13,center:new google.maps.LatLng(32.0192813,34.75112319999994),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(32.0192813,34.75112319999994)});infowindow = new google.maps.InfoWindow({content:'<strong>ג\'ודו ליאור - בת ים</strong><br>בת ים, החשמונאים 27<br>'});google.maps.event.addListener(marker, 'click', function(){infowindow.open(map,marker);});infowindow.open(map,marker);
google.maps.event.addDomListener(window, 'load', init_map);}

judoApp.config(['$routeProvider',function($routeProvider) {
	$routeProvider.when("/", {
			templateUrl: "/assets/views/index.html"
    }).when("/info", {
			templateUrl: "/assets/views/info.html"
	}).when("/contact", {
            templateUrl: "/assets/views/contact.html"
    
    }).when("/buildpics", {
            templateUrl: "/assets/views/buildingpics.html"
    }).when("/login", {
            templateUrl: "/assets/views/login.html"
    }).when("/myuser", {
            templateUrl: "/assets/views/myuser.html"
    }).otherwise({
            redirectTo: '/'
    });
}]);