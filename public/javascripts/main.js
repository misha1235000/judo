$(window).load(function() {
    // Animate loader off screen
    $(".se-pre-con").fadeOut("slow");
});
$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
    event.preventDefault();
    $(this).ekkoLightbox();
});

var judoApp = angular.module("judoApp", ['ngRoute']);

judoApp.controller('mainCont', ['$rootScope', '$http', '$rootScope',function($rootScope, $http, $rootScope) {
    $rootScope.picid = 0;
    $rootScope.name;
    $rootScope.allcomments = [];
    $rootScope.news = [];
    $rootScope.permlevel = "";
    $rootScope.comments = [];
    $rootScope.usr = {'username':'','pass':'','firstName':'','lastName':'','email':'','perm':0, 'profilepic':''};
    
    $http.get('/pics').success(function(data) {
        if (data != "" && data != null) {
            for (var i = 0; i < data.length; i++) {
                data[i].time = data[i].time.split(":")[0]+ ":" +data[i].time.split(":")[1];
            }
        }
            $rootScope.pics = data;
        }).error(function() {
            console.log("Error in getting pics.");
        })
    
    
    $http.get('/session').success(function(data) {
       $rootScope.usr = data;
        if ($rootScope.usr.profilepic == "none") {
            $rootScope.usr.profilepic = "/assets/images/profile/unknown.jpg";
        }
       if($rootScope.usr.perm == 1) {
           $rootScope.permlevel = "משתמש רגיל";
       } else if ($rootScope.usr.perm == 2) {
           $rootScope.permlevel = "מאמן";
       } else if ($rootScope.usr.perm == 3) {
           $rootScope.permlevel = "מפתח";
       }
              if (window.location.hash == "#/gallery" && $rootScope.usr.firstName == "") {
            window.location = "/";
        }
    }).error(function() {       if (window.location.hash == "#/gallery" && $rootScope.usr.firstName == "") {
            window.location = "/";
        }});
    $rootScope.login = function() {
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
    
    $rootScope.getUsers = function() {
        $http.get('/users').success(function(data) {
            $rootScope.users = data
            for (var i = 0; i < $rootScope.users.length; i++) {
                switch ($rootScope.users[i].perm) {
                    case 1:
                        $rootScope.users[i].permdisp = "משתמש רגיל";
                    break;
                    case 2:
                        $rootScope.users[i].permdisp = "מאמן המועדון";                    
                    break;
                    case 3:
                        $rootScope.users[i].permdisp = "מפתח מערכת";
                    break;
                }
            }
        }).error(function(){});
    }
    
    $rootScope.logout = function() {
        $http.get('/logout')
        .success(function() {
            location.reload();
        }).error(function () {
        })
    }
    
    $rootScope.addNews = function() {
        $http.get('/news/add/' + $("#message").val()).success(function(data) {
           swal("המודעה נוספה בהצלחה", "" ,"success");
           $rootScope.news.push(data);
         // location.reload();
        }).error(function() {
            console.log("A Problem occured while adding a message");
        });
    }
    
    $rootScope.deleteNew = function(id) {
        $http.get('/news/delete/' + id).success(function(data) {
            for (var i = 0; i < $rootScope.news.length; i++) {
                if ($rootScope.news[i].id == id) {
                    $rootScope.news[i].hidden = true;
                }
            }
            swal("המודעה נמחקה בהצלחה", "" ,"success");
      //      location.reload();
        }).error(function() {
            
        })
    }
    
    $rootScope.showNews = function() {
        $http.get('/news').success(function(data) {
            $rootScope.news = data;
            for(var i = 0; i < $rootScope.news.length; i++) {
                $rootScope.news[i].hidden = false;
            }
        }).error(function() {
            console.log("Error in getting news.");
        })
    }
    
   /* $rootScope.showComments = function(id) {
        $http.get('/comments/' + id).success(function() {
            $rootScope.comments = data;
            for(var i = 0; i < $rootScope.comments.length; i++) {
                $rootScope.comments[i].hidden = false;
            }
        });
    }*/
    $rootScope.addImage = function() {
        var title = $("#titlepic").val();
        var desc = $("#descpic").val();
        if (title == "" || desc == "") {
            swal("אחד מן הפרטים אינו מלא", "", "error");
        } else {
            $http.get("/upload/" + title + "/" + desc).success(function() {
                swal("התמונה הועלתה בהצלחה", "", "success");
            });
        }
    }

    $rootScope.addComment = function() {
        if ($rootScope.picid != 0) {
            if ($("#comment").val() == "") {
                $("#wrongcom").html("התגובה איננה יכולה להיות ריקה.");
            } else {
                var a = $("#comment").val();
                $http.get('/comments/add/' + $rootScope.picid + '/' + a).success(function(data) {
                    $('#comment').val("");

          //          $rootScope.comments.push(data);
                    
                    setTimeout(function() {$('.commentcls').scrollTop(10000); }, 200);
                });
            }
        }
    }
    
    $rootScope.showPics = function() {
        $http.get('/pics').success(function(data) {
            for (var i = 0; i < data.length; i++) {
                data[i].time = data[i].time.split(":")[0]+ ":" +data[i].time.split(":")[1];
            }
            $rootScope.pics = data;
            for (var i = 0; i < 1000; i++) {
                clearInterval(i);
            }
            setInterval(function() {
        $http.get('/comments/getall').success(function(data) { 
                var ischng = false;
                $rootScope.comments = [];
                if ($rootScope.allcomments.length != data.length) {
                    ischng = true;
                }
                $rootScope.allcomments = data;
                for (var i = 0; i < $rootScope.allcomments.length; i++) {
                    if ($rootScope.allcomments[i].picId == $rootScope.picid) {
                        $rootScope.comments.push($rootScope.allcomments[i]);
                    }
                }
                if (ischng) {
                  setTimeout(function() {$('.commentcls').scrollTop(1000000); }, 200);
                }
                
                console.log("BOOM");                
        }); }, 1000);
        }).error(function() {
            console.log("Error in getting pics.");
        })
    }
    $rootScope.updateProfile = function() {
        var emailPattern    = RegExp(/^\w+@\w+\.\w+$/);
        var passwordPattern = RegExp(/^\w{7,15}$/);

        if ($("#updname").val() == "") {
            $("#wrongupd").html("שם פרטי לא יכול להיות ריק");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else if ($("#updlastname").val() == "") {
            $("#wrongupd").html("שם משפחה לא יכול להיות ריק");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else if ($("#updemail").val() == "") {
            $("#wrongupd").html("דואר אלקטרוני לא יכול להיות ריק");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        }  else if ($("#updpass").val() == "") {
            $("#wrongupd").html("סיסמא לא יכולה להיות ריקה");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else if ($("#updpassconf").val() == "") {
            $("#wrongupd").html("אימות הסיסמא אינו יכול להיות ריק");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else if (!emailPattern.test($("#updemail").val())) {
            $("#wrongupd").html("הדואר אלקטרוני אינו תקין");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else if (!passwordPattern.test($("#updpass").val())) {
            $("#wrongupd").html("סיסמא אינה תקינה");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else if ($("#updpass").val() != $("#updpassconf").val()) {
            $("#wrongupd").html("הסיסמא ואימותה אינם זהות");
            setTimeout(function() {$("#wrongupd").html("")}, 2000)
            return false;
        } else {
             $http.get("/profile/update/" + $("#updname").val() + "/" + $("#updlastname").val() + "/" + $("#updemail").val() + "/" + $("#updpass").val() + "/" + $("#updpassconf").val()).success(function(data) {$("#wrongupd").html(data); if($("#wrongupd").html().indexOf("בהצלחה") > -1) {  
                 $rootScope.usr.firstName = $("#updname").val();
                 $rootScope.usr.lastName = $("#updlastname").val();
                 $rootScope.usr.email = $("#updemail").val();
                 $rootScope.usr.pass = $("#updpass").val();
                 swal(data, "", "success");
             }
             setTimeout(function() {$("#wrongupd").html("")}, 2000) }).error(function(){});
        }
    }
        
    $rootScope.changeSrc = function(imgsrc, title, info, picid) {
        $rootScope.picid = picid;
        $(".mysrcyus").attr("src", imgsrc);
        $(".mysrcyus").attr("ng-src", imgsrc);
        $(".mytitleyus").html(title);
        $(".myinfoyus").html(info);
    }
    
    $rootScope.changePerm = function(id, perm) {
        if ($rootScope.usr.id == id) {
            swal("לא ניתן לשנות הרשאה לעצמך", "", "error");
        } else {
            $http.get("/perm/" +id+ "/" + perm).success(function(data) {swal(data,"" ,"success")});
        }
    }
    
    $rootScope.checkValid = function(){
        var emailPattern    = RegExp(/^\w+@\w+\.\w+$/);
        var passwordPattern = RegExp(/^\w{7,15}$/);

        if ($("#regname").val() == "") {
            $("#wrongreg").html("שם פרטי לא יכול להיות ריק");
            setTimeout(function() {$("#wrongreg").html("")}, 2000)
            return false;
        } else if ($("#reglastname").val() == "") {
            $("#wrongreg").html("שם משפחה לא יכול להיות ריק");
            setTimeout(function() {$("#wrongreg").html("")}, 2000)
            return false;
        } else if ($("#regemail").val() == "") {
            $("#wrongreg").html("דואר אלקטרוני לא יכול להיות ריק");
            setTimeout(function() {$("#wrongreg").html("")}, 2000)
            return false;
        } else if ($("#reguser").val() == "") { 
            $("#wrongreg").html("שם משתמש לא יכול להיות ריק");
            setTimeout(function() {$("#wrongreg").html("")}, 2000)
            return false;
        } else if ($("#regpass").val() == "") {
            $("#wrongreg").html("סיסמא לא יכולה להיות ריקה");
            setTimeout(function() {$("#wrongreg").html("")}, 2000)
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
                  if (data.indexOf("בהצלחה") > -1) {
                    swal(data, "", "success");
                      setTimeout(function() {
                        
                        location.reload();
                    }, 2000);
                  } else {
                    swal(data, "", "error");
                  }
                    
              }).error  (function() {
              });
        }
    }
}]);

function init_map(){var myOptions = {zoom:13,center:new google.maps.LatLng(32.0192813,34.75112319999994),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(32.0192813,34.75112319999994)});infowindow = new google.maps.InfoWindow({content:'<strong>ג\'ודו ליאור - בת ים</strong><br>בת ים, החשמונאים 27<br>'});google.maps.event.addListener(marker, 'click', function(){infowindow.open(map,marker);});infowindow.open(map,marker);
google.maps.event.addDomListener(window, 'load', init_map);}

judoApp.config(['$routeProvider', '$locationProvider',function($routeProvider, $locationProvider) {
	$routeProvider.when("/", {
			templateUrl: "/assets/views/index.html"
    }).when("/info", {
			templateUrl: "/assets/views/info.html"
	}).when("/contact", {
            templateUrl: "/assets/views/contact.html"    
    }).when("/gallery", {
            templateUrl: "/assets/views/gallery.html"
    }).when("/login", {
            templateUrl: "/assets/views/login.html"
    }).when("/myuser", {
            templateUrl: "/assets/views/myuser.html"
    }).otherwise({
            redirectTo: '/'
    });
    
}]);