$(window).load(function() {
    // Animate loader off screen
    $(".se-pre-con").fadeOut("slow");
});
$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
    event.preventDefault();
    $(this).ekkoLightbox();
});

var judoApp = angular.module("judoApp", [
  'ngRoute',
  'cloudinary',
//  'photoAlbumControllers',
    'photoAlbumAnimations',
  'photoAlbumServices',
  'ngFileUpload'
]);

judoApp.controller('mainCont', ['$rootScope', '$http', '$routeParams', '$location', 'Upload', 'cloudinary', function($rootScope, $http, $routeParams, $location, $upload, cloudinary) {
    $rootScope.checkFill = function() {     
                if ($("#picaddinf").val() != "" && $("#picaddttl").val() != "") {
                    document.getElementById("picaddbtn").disabled = false;
                } else {
                    document.getElementById("picaddbtn").disabled = true;
                }
    }
      /* Uploading with Angular File Upload */
    var d = new Date();
    $rootScope.title = "תמונה(" + d.getDate() + " - " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + ") -";
    $rootScope.$watch('files', function() {
    $rootScope.uploadFiles = function(files){
      $rootScope.files = files;
      if (!$rootScope.files) return;
      angular.forEach(files, function(file){
        if (file && !file.$error) {
          file.upload = $upload.upload({
            url: "https://api.cloudinary.com/v1_1/" + cloudinary.config().cloud_name + "/upload",
            data: {
              upload_preset: cloudinary.config().upload_preset,
              tags: 'myphotoalbum',
  //            context: 'photo=' + $("#picaddttl").val(),
              context: 'photo=' + $("#picaddttl").val() + "~" + $("#picaddinf").val(),
              file: file
            }
          }).progress(function (e) {
            file.progress = Math.round((e.loaded * 100.0) / e.total);
            file.status = "Uploading... " + file.progress + "%";
          }).success(function (data, status, headers, config) {
            $rootScope.photos = $rootScope.photos || [];
            data.context = {custom: {photo: $rootScope.title}};
            file.result = data;
            $("#progbars").attr("class", "col-md-4 progress-bar progress-bar-success progress-bar-striped");
            $(".infoim").html("<img class='fadeInDown animated' src='/assets/images/success.png' height='250px' style='padding-top:50%;'/>");
            $rootScope.photos.push(data);
          }).error(function (data, status, headers, config) {
            file.result = data;
          });
        }
      });
    };
    });

    /* Modify the look and fill of the dropzone when files are being dragged over it */
    $rootScope.dragOverClass = function($event) {
      var items = $event.dataTransfer.items;
      var hasFile = false;
      if (items != null) {
        for (var i = 0 ; i < items.length; i++) {
          if (items[i].kind == 'file') {
            hasFile = true;
            break;
          }
        }
      } else {
        hasFile = true;
      }
      return hasFile ? "dragover" : "dragover-err";
    };
    
    
    
    $rootScope.picid = 0;
    $rootScope.name;
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
                    var help = [];
                    help[0] = data;
                    for (var i = 0; i < $rootScope.comments.length; i++) {
                        help[i + 1] = $rootScope.comments[i];
                    }
                    $rootScope.comments = help;
          //          $rootScope.comments.push(data);
                    
                    setTimeout(function() {$('.commentcls').scrollTop(0); }, 200);
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
        window.setInterval(function() {
        $http.get('/comments/get/' + picid).success(function(data) {
            var helpdata = [];
            for (var i = 0; i < data.length; i++) {
                helpdata[i] = data[data.length - i - 1];
            }
            data = helpdata;
            if ($rootScope.comments.length != data.length) {
                $rootScope.comments = data;
                            setTimeout(function() {$('.commentcls').scrollTop(0); }, 200);
            for(var i = 0; i < $rootScope.comments.length; i++) {
                $rootScope.comments[i].hidden = false;
                $rootScope.comments[i]
            }
            console.log("BOOM");
            }
        }); }, 1000);
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
            templateUrl: "/assets/views/gallery.html",
       resolve: {
        photoList: function ($q, $rootScope, album) {
          if (!$rootScope.serviceCalled) {
            return album.photos({}, function (v) {
              $rootScope.serviceCalled = true;
              $rootScope.photos = v.resources;
            });
          } else {
            return $q.when(true);
          }
        }
      }
    }).when("/login", {
            templateUrl: "/assets/views/login.html"
    }).when("/myuser", {
            templateUrl: "/assets/views/myuser.html"
    }).when("/pics", {
            templateUrl: "/assets/views/photos.html"
    }).otherwise({
            redirectTo: '/'
    });
    
}]);