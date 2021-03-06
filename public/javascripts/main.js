$(window).load(function() {
    // Animate loader off screen
    $(".se-pre-con").fadeOut(2000);
    
});







$(document).ready(function() {
        $('[data-toggle="tooltip"]').tooltip();
        $(".chat-sidebar").attr("class", "chat-sidebar ng-scope slideOutRight animated");
        setTimeout(function() {$(".chat-sidebar").fadeOut(0);});
        $(".mytogglechat").attr("style", "position:fixed; bottom:10px; right:0px; color:rgb(33,150,243); cursor:pointer;");
        $(".mytogglechat").attr("data-original-title", "הצג צ'אט");
        $("#mytogglechat").attr("class", "fa fa-angle-double-left");
    
});

function centerModal() {
    $(this).css('display', 'block');
    var $dialog = $(this).find(".modal-dialog");
    var offset = ($(window).height() - $dialog.height()) / 2;
}
    // Cente
$('#misham').on('show.bs.modal', centerModal);

function startVegas() {
    $('body').vegas({
      overlay: true,
      transition: 'fade',
      transitionDuration: 3000,
      delay: 5000,
      color: 'black',
      timer: false,
      animation: 'random',
      animationDuration: 9000,
      slides: [
        { src: '/assets/images/bgcarousel/bg-car1.jpg'},
        { src: '/assets/images/bgcarousel/bg-car2.jpg'},
        { src: '/assets/images/3dcarousel/3dcar11.jpg'},
        { src: '/assets/images/bgcarousel/bg-car3.jpg'},
        { src: '/assets/images/3dcarousel/3dcar2.jpg'},
        { src: '/assets/images/3dcarousel/3dcar3.jpg'},
        { src: '/assets/images/3dcarousel/3dcar4.jpg'},
        { src: '/assets/images/3dcarousel/3dcar5.jpg'},
        { src: '/assets/images/3dcarousel/3dcar10.jpg'},
        { src: '/assets/images/3dcarousel/3dcar6.jpg'},
        { src: '/assets/images/3dcarousel/3dcar7.jpg'},
        { src: '/assets/images/3dcarousel/3dcar8.jpg'},
        { src: '/assets/images/3dcarousel/3dcar9.jpg'},
        { src: '/assets/images/3dcarousel/3dcar1.jpg'}
       /* { src: 'https://ununsplash.imgix.net/reserve/RONyPwknRQOO3ag4xf3R_Kinsey.jpg?fit=crop&fm=jpg&h=700&q=75&w=1600' },
        { src: 'https://unsplash.imgix.net/photo-1414438992182-69e404046f80?fit=crop&fm=jpg&h=625&q=75&w=1600' },
        { src: 'https://unsplash.imgix.net/photo-1414490929659-9a12b7e31907?fit=crop&fm=jpg&h=800&q=75&w=1600' },
        { src: 'https://unsplash.imgix.net/uploads/14129863345840df499fc/0165574c?fit=crop&fm=jpg&h=600&q=75&w=1600' }*/
      ]
    });
}

var boom = 0;
function togglechat() {
    
   // $(".chat-sidebar").fadeToggle("slow");
    if ($(".chat-sidebar").attr("class") == "chat-sidebar ng-scope" || $(".chat-sidebar").attr("class") == "chat-sidebar ng-scope slideInRight animated") {
        $(".chat-sidebar").attr("class", "chat-sidebar ng-scope slideOutRight animated");
        setTimeout(function() {$(".chat-sidebar").fadeOut("fast");}, 200);
        $(".mytogglechat").attr("style", "position:fixed; bottom:10px; right:0px; color:rgb(33,150,243); cursor:pointer;");
        $(".mytogglechat").attr("data-original-title", "הצג צ'אט");
        $(".mytogglechat").attr("class", "material-icons mdl-badge mdl-badge--overlap mytogglechat slideInLeft animated");
        
        $(".mytogglechat").fadeIn(0);
    } else {
        $(".chat-sidebar").attr("class", "chat-sidebar ng-scope slideInRight animated");
        setTimeout(function() {$(".chat-sidebar").fadeIn("fast");});
        $(".mytogglechat").attr("style", "position:fixed; bottom:10px; right:160px; color:rgb(33,150,243); cursor:pointer; ");
        $(".mytogglechat").attr("data-original-title", "הסתר צ'אט");
        $(".mytogglechat").attr("class", "material-icons mdl-badge mdl-badge--overlap mytogglechat slideInRight animated");
        
     //   $(".mytogglechat").attr("class", "material-icons mdl-badge mdl-badge--overlap mytogglechat slideInRight animated");
        $(".mytogglechat").fadeIn(0);
    }
}

var judoApp = angular.module("judoApp", [
  'ngRoute',
  'cloudinary',
//  'photoAlbumControllers',
    'photoAlbumAnimations',
  'photoAlbumServices',
  'ngFileUpload',
  'ui.router'
]);

judoApp.directive('loading', function () {
      return {
        restrict: 'E',
        replace:true,
        template: '<div class="loading"></div>',
        link: function (scope, element, attr) {
              scope.$watch('loading', function (val) {
                  if (val)
                      $(element).show();
                  else
                      $(element).hide();
              });
        }
      }
  })

judoApp.filter('trusted', ['$sce', function ($sce) {
    return function(url) {
        return $sce.trustAsResourceUrl(url);
    };
}]);


// Determine the correct object to use
var notification = window.Notification || window.mozNotification || window.webkitNotification;

// The user needs to allow this
if ('undefined' === typeof notification)
    alert('Web notification not supported');
else
    notification.requestPermission(function(permission){});

// A function handler
function Notify(titleText, bodyText)
{
    if ('undefined' === typeof notification)
        return false;       //Not supported....
    var noty = new notification(
        titleText, {
            body: bodyText,
            dir: 'auto', // or ltr, rtl
            lang: 'EN', //lang used within the notification.
            tag: 'notificationPopup', //An element ID to get/set the content
            icon: '/assets/images/slogo.jpg' //The URL of an image to be used as an icon
        }
    );
    
    setTimeout(function(){noty.close();}, 5000);
    noty.onclick = function () {
        console.log('notification.Click');
    };
    noty.onerror = function () {
        console.log('notification.Error');
    };
    noty.onshow = function () {
        console.log('notification.Show');
    };
    noty.onclose = function () {
        console.log('notification.Close');
    };
    
    return true;
}

judoApp.controller('mainCont', ['$rootScope', '$http', '$routeParams', '$location', 'Upload', 'cloudinary', function($rootScope, $http, $routeParams, $location, $upload, cloudinary) {
    $rootScope.loaded = false;
    var regex = RegExp(/^[a-zA-Z0-9.?!@#$%()*_+-\/-\ ;~א-תףךץ]+$/);
            $rootScope.ChatInputChange = function(e) {
                if (!e) {
                    var e = window.event;
                }
                if (e.keyCode == 13) {
                    $rootScope.changedChat();
                }
            }
            

          /*  setTimeout(function() {
            if ($('body').attr("class").includes("vegas") == false && window.location.hash == "#/") {
                startVegas();
    //          $('body').vegas('jump', 1);
            }
        }, 2500);
        
        setTimeout(function() {
            if ($('body').attr("class").includes("vegas") == false && window.location.hash == "#/") {
                startVegas();
//              $('body').vegas('jump', 1);
            }
        }, 6500);
        
        
        setTimeout(function(){
            if ($('body').attr("class").includes("vegas") == true && window.location.hash == "#/") {
                $('body').vegas('pause');
            }
        }, 3500);
    
        setTimeout(function(){
            if ($('body').attr("class").includes("vegas") == true && window.location.hash == "#/") {
                $('body').vegas('play');
            }
        }, 4000);
    
        setTimeout(function(){
            if ($('body').attr("class").includes("vegas") == true && window.location.hash == "#/") {
                if ($('body').vegas('current') == 0) {
                    $('body').vegas('pause');
                }
            }
        }, 7500);
    
        setTimeout(function(){
            if ($('body').attr("class").includes("vegas") == true && window.location.hash == "#/") {
                if ($('body').vegas('current') == 0) {
                    $('body').vegas('play');
                }
            }
        }, 8000);
    
    setTimeout(function() {
        if ($('body').attr("class").includes("vegas") == true && window.location.hash == "#/") {
            if ($('body').vegas('current') == 0) {
                $('body').vegas('pause');
                $('body').vegas('play');
            }
        }
    }, 10000);*/
            
            $rootScope.changedChat = function() {
                if (regex.test($("#mycht").val()) && $("#mycht").val()) {
                    var sent = $("#mycht").val();
                    $("#mycht").val("");
                    $http.post("/chat/send", {'msgto':$rootScope.currid, 'msg':sent}).success(function(data){
                        $rootScope.chat.push(data);
                        setTimeout(function(){$(".popup-messages").scrollTop(5000);}, 200);
                    });
                }
            }
            $rootScope.currname = "";
            $rootScope.currid = 0;
            //this is used to close a popup
            $rootScope.close_popup = function(id)
            {
                $(".popup-box").fadeOut("slow");
   //             $(".popup-box").attr("style", "display:none");
                $rootScope.chat = [];
                $rootScope.currid = 0;
                $rootScope.currname = "";
            }
            
            //creates markup for a new popup. Adds the id to popups array.
            $rootScope.register_popup = function(id, name, lastname, chatprofile)
            {
                $rootScope.chat = [];
                $(".popup-messages").scrollTop(0);
                $rootScope.loading = true;
                $http.post('/chat/read', {'msgto':id}).success(function() {
                    for (var i = 0; i < $rootScope.users.length; i++) {
                        if ($rootScope.users[i].id == id) {
                            $rootScope.users[i].sent = undefined;
                        }
                    }
                });
                $rootScope.currname = name + " " + lastname;
                $rootScope.currid = id;
           //     $(".popup-box").attr("style", "display:block");
                $(".popup-box").fadeIn("slow");
                $("#mycht").val("");
                $("#mycht").focus();
                $http.post("/chat", {'msgfrom':$rootScope.usr.id,'msgto':id}).success(function(data) {
                    $rootScope.chat = data;
                    $rootScope.loading = false;
                    setTimeout(function(){$(".popup-messages").scrollTop(9000);}, 1);
                    for (var i = 0; i < data.length; i++) {
                        if ($rootScope.chat[i].id == $rootScope.usr.id) {
                            $(".mydsn" + $rootScope.chat[i].id).attr("style","/* margin-top: 10px; *//* right: 5px; */padding-right:0px !important;border-radius:20px;background-color: gray;font-weight: bold;color: black;height: 100%;");
                        } else {
                            $(".mydsn" + $rootScope.chat[i].id).attr("style","/* margin-top: 10px; *//* right: 5px; */padding-right:0px !important;border-radius:20px;background-color: green;font-weight: bold;color: black;height: 100%;");
                        }
                    }
                    
                });
            }               
            
    
    
    
    $rootScope.rstbtnn = function() {
        document.getElementById("picaddbtn").disabled = true;    
    };
    $rootScope.checkFill = function() {     
                if ($("#picaddinf").val() != "") {
                    document.getElementById("picaddbtn").disabled = false;
                } else {
                    document.getElementById("picaddbtn").disabled = true;
                }
    }
      /* Uploading with Angular File Upload */
    var d = new Date();
    $rootScope.title = "";
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
              context: 'photo=' + "none" + "~" + $("#picaddinf").val(),
              file: file
            }
          }).progress(function (e) {
            file.progress = Math.round((e.loaded * 100.0) / e.total);
            file.status = "Uploading... " + file.progress + "%";
          }).success(function (data, status, headers, config) {
                var isvideo = 0;
              if (data.resource_type == "video") {
                  isvideo = 1;
              }
            $http.post("/upload/post", {'title': "none", 'desc': $("#picaddinf").val(), 'src': data.url, 'isvideo': isvideo}).success(function() {
                setTimeout(function() {
                    window.location = "/#gallery";
                }, 1000);
           //    swal("good", "", "success");
            });
            $rootScope.photos = $rootScope.photos || [];
            data.context = {custom: {photo: "none" + "~" + $("#picaddinf").val()}};
            file.result = data;
            $("#progbars").attr("class", "col-md-4 progress-bar progress-bar-success progress-bar-striped");
            $(".infoim").html("<img class='fadeInDown animated' src='/assets/images/success.png' style='width: 100%; padding-top: 50%;'/>");
            $rootScope.photos.push(data);
          }).error(function (data, status, headers, config) {
            file.result = data;
          });
        }
      });
    };
    });

        $rootScope.uploadprof = function(files){
      $rootScope.files = files;
      if (!$rootScope.files) return;
      angular.forEach(files, function(file){
        if (file && !file.$error) {
          file.upload = $upload.upload({
            url: "https://api.cloudinary.com/v1_1/" + cloudinary.config().cloud_name + "/upload",
            data: {
              upload_preset: cloudinary.config().upload_preset,
              tags: 'myphotoalbum',
              file: file
            }
          }).progress(function (e) {
            file.progress = Math.round((e.loaded * 100.0) / e.total);
            file.status = "Uploading... " + file.progress + "%";
          }).success(function (data, status, headers, config) {
            $http.post("/upload/pic", {'src': data.url}).success(function() {
                setTimeout(function() {
                    window.location = "/#";
                }, 1000);
           //    swal("good", "", "success");
            });
            $rootScope.photos = $rootScope.photos || [];
            file.result = data;
            $rootScope.photos.push(data);
          }).error(function (data, status, headers, config) {
            file.result = data;
          });
        }
      });
    };
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
    $rootScope.currusr = [];
    $rootScope.permlevel = "";
    $rootScope.comments = [];
    $rootScope.modpic = "";
    $rootScope.usr = {'username':'','pass':'','firstName':'','lastName':'','email':'','perm':0, 'profilepic':''}

    $(".mytogglechat").click(function() {
    });
    
    $http.get('/session').success(function(data) {
        
       $rootScope.loaded = true;
       $rootScope.usr = data;
    if ($rootScope.usr.firstName != '') {
    $rootScope.getUsers();
    $http.get('/news').success(function(data) {
    $rootScope.news = data;
    for(var i = 0; i < $rootScope.news.length; i++) {
        $rootScope.news[i].hidden = false;
    }
    }).error(function() {
        console.log("Error in getting news.");
    });
        window.setInterval(function() {
            $http.post("/news/listen", {'amount': $rootScope.news.length}).success(function(data) {
                if (data != "bad") {
                    if ($rootScope.news[$rootScope.news.length - 1].id != data.id) {
                        $rootScope.news.push(data);
                        Notify(data.authorname, data.message);
                    }
                }
            });
            $http.post("/chat/listen", {}).success(function(data) {
                if (data != "") {
                    for (var i = 0; i < $rootScope.users.length; i++) {
                        for (var j = 0; j < data.length; j++) {
                            if ($rootScope.users[i].id == data[j].msgfrom) {
                                $rootScope.users[i].sent = data[j].amount;
                                if ($rootScope.users[i].id == $rootScope.currid) {
                                    $http.post("/chat", {'msgfrom':$rootScope.usr.id,'msgto':$rootScope.users[i].id}).success(function(data) {
                                    if (data != undefined && data != "") {
                                        $rootScope.chat = data;
                                        setTimeout(function(){$(".popup-messages").scrollTop(9000);}, 1);
                                        $http.post('/chat/read', {'msgto':$rootScope.currid}).success(function() {
                                            for (var i = 0; i < $rootScope.users.length; i++) {
                                                if ($rootScope.users[i].id == $rootScope.currid) {
                                                    $rootScope.users[i].sent = undefined;
                                                }
                                            }
                                        });
                                    }
                });
                                }
                            } else {
                                $rootScope.users[i].sent = undefined;
                            }
                        }
                    }
                    
                    $("#chatsign").attr("data-badge", "");
                    $rootScope.newMsgs = 1;
                } else {
                    $("#chatsign").removeAttr("data-badge");
                    $rootScope.newMsgs = 0;
                }
            });
        }, 5000);
    } else {
        /*for (var i = 0; i < 1000; i++) {
            window.clearInterval(i);
        }*/
    }
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
              if ((window.location.hash == "#/gallery" || window.location.hash=="#/users") && $rootScope.usr.firstName == "") {
            window.location = "/";
        }
     //   if (window.location.hash == "#/" && $rootScope.usr.firstName == "") {
     //       setTimeout(function(){$('#loginm').modal('show');}, 3000);
     //   }
        
    }).error(function() {       if ((window.location.hash == "#/gallery" || window.location.hash=="#/users") && $rootScope.usr.firstName == "") {
            window.location = "/";
        }});
    $rootScope.login = function() {
    if ($("#username").val() == "") {
        $("#wronglog").html("שם משתמש לא יכול להיות ריק");
    } else if ($("#pass").val() == "") {
        $("#wronglog").html("סיסמא לא יכולה להיות ריקה");
    }
            
        
    $http.post('/auth/login', {'username':$('#username').val(), 'pass':$('#pass').val()})
    .success(function(data) {
        if (data == "" || data == undefined || data == null) {
            $("#wronglog").html("שם משתמש או סיסמא אינם נכונים");
        } else {
            window.location="/#gallery";
        }
    }).error(function () {
        })
    }
    
    if ($rootScope.pics == undefined) {
    $http.get('/users').success(function(data) {
        $rootScope.users = data;
            $http.get('/pics').success(function(data) {
        if (data != "" && data != null) {
            for (var i = 0; i < data.length; i++) {
                data[i].time = data[i].time.split(":")[0]+ ":" +data[i].time.split(":")[1];
            }
        }
            if ($rootScope.pics == undefined) {
                $rootScope.pics = data;
                for (var i = 0; i < $rootScope.pics.length; i++) {
                    for (var j = 0; j < $rootScope.users.length; j++) {
                        if ($rootScope.pics[i].posterId == $rootScope.users[j].id) {
                            $rootScope.pics[i].posterimg = $rootScope.users[j].profilepic;
                        }
                    }
                }
            }
        }).error(function() {
            console.log("Error in getting pics.");
        })
    })
    }
    
    $rootScope.deletePic = function(id) {
                swal({   title: "אתה בטוח?",   text: "מהרגע שהתמונה נמחקת לא ניתן להחזירה",   type: "warning",   showCancelButton: true,   confirmButtonColor: "#DD6B55",   confirmButtonText: "מחק",   closeOnConfirm: false }, function(){$http.post("/pics/delete", {'id':id}).success(function() {
           location.reload();
        }); 
        }); 

    }
    
    $rootScope.getUsers = function() {
        
        if ($rootScope.usr.perm > 0) {
        $http.get('/users').success(function(data) {
            $rootScope.users = data
            $("#usr" + $rootScope.usr.id).attr("style", "background-color:green;");
            for (var i = 0; i < $rootScope.users.length; i++) {
                if ($rootScope.users[i].profilepic == "none") {
                    $rootScope.users[i].profilepic = "/assets/images/profile/unknown.jpg"
                }
                
                switch ($rootScope.users[i].perm) {
                    case 1:
                        $rootScope.users[i].permdisp = "person";
                        $rootScope.users[i].permdisplay = "משתמש רגיל";
                    break;
                    case 2:
                        $rootScope.users[i].permdisp = "person_add";   
                        $rootScope.users[i].permdisplay = "מאמן"
                    break;
                    case 3:
                        $rootScope.users[i].permdisp = "lock";
                        $rootScope.users[i].permdisplay = "מפתח מערכת";
                    break;
                }
            }
        }).error(function(){});
        }
    }
    
    $rootScope.logout = function() {
        $http.get('/auth/logout')
        .success(function() {
            location.reload();
        }).error(function () {
        })
    }
    
    $rootScope.addNews = function() {
        $http.post('/news/add', {'message':$("#message").val()}).success(function(data) {
          var snackbarContainer = document.querySelector('#demo-toast-example');
          var showToastButton = document.querySelector('#demo-show-toast');
          var msgdata = {message: 'המודעה נוספה בהצלחה'};
          snackbarContainer.MaterialSnackbar.showSnackbar(msgdata);
           $rootScope.news.push(data);
         // location.reload();
        }).error(function() {
            console.log("A Problem occured while adding a message");
        });
    }
    
    $rootScope.deleteNew = function(id) {
        $http.post('/news/delete', {id: id}).success(function(data) {
            for (var i = 0; i < $rootScope.news.length; i++) {
                if ($rootScope.news[i].id == id) {
                    $rootScope.news[i].hidden = true;
                }
            }
          var snackbarContainer = document.querySelector('#demo-toast-example');
          var showToastButton = document.querySelector('#demo-show-toast');
          var data = {message: 'המודעה נמחקה בהצלחה'};
          snackbarContainer.MaterialSnackbar.showSnackbar(data);
           // swal("המודעה נמחקה בהצלחה", "" ,"success");
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
            $http.get("/upload", {'title': "none", 'desc': $("picaddinf").val(), 'src': ""} + "/" + desc).success(function() {
                swal("התמונה הועלתה בהצלחה", "", "success");
            });
        }
    }

    $rootScope.addComment = function() {
        if ($rootScope.picid != 0 && $rootScope.picid != undefined) {
            if ($("#comment").val() == "") {
                $("#wrongcom").html("התגובה איננה יכולה להיות ריקה.");
            } else {
                var a = $("#comment").val();
                $http.post('/comments/add', {'id': $rootScope.picid, 'comment': a}).success(function(data) {
                    $('#comment').val("");
                    var help = [];
                    help[0] = data;
                    for (var i = 0; i < $rootScope.comments.length; i++) {
                        help[i + 1] = $rootScope.comments[i];
                    }
                    $rootScope.comments = help;
          //          $rootScope.comments.push(data);
                    
                    setTimeout(function() {$('.commentcls').scrollTop(0); }, 0);
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
             $http.post("/users/update", {'name':$('#updname').val(), 'lastname':$('#updlastname').val(), 'email': $('#updemail').val(),'pass':$('#updpass').val(),'passconf': $('#updpassconf').val()}
                      ).success(function(data) {$("#wrongupd").html(data); if($("#wrongupd").html().indexOf("בהצלחה") > -1) {  
                 $rootScope.usr.firstName = $("#updname").val();
                 $rootScope.usr.lastName = $("#updlastname").val();
                 $rootScope.usr.email = $("#updemail").val();
                 $rootScope.usr.pass = $("#updpass").val();
                 swal(data, "", "success");
             }
             setTimeout(function() {$("#wrongupd").html("")}, 2000) }).error(function(){});
        }
    }
    $rootScope.picid = 0;
    $('#currimg').on('hide.bs.modal', function() {
       $rootScope.picid = 0; 
       $rootScope.comments = [];
       for (var i = 0; i < 1000; i++) {
           clearInterval(i);
       }
                window.setInterval(function() {
            $http.post("/news/listen", {'amount': $rootScope.news.length}).success(function(data) {
                if (data != "bad") {
                    if ($rootScope.news[$rootScope.news.length - 1].id != data.id) {
                        $rootScope.news.push(data);
                        Notify(data.authorname, data.message);
                    }
                }
            });
            $http.post("/chat/listen", {}).success(function(data) {
                if (data != "") {
                    for (var i = 0; i < $rootScope.users.length; i++) {
                        for (var j = 0; j < data.length; j++) {
                            if ($rootScope.users[i].id == data[j].msgfrom) {
                                $rootScope.users[i].sent = data[j].amount;
                                if ($rootScope.users[i].id == $rootScope.currid) {
                                    $http.post("/chat", {'msgfrom':$rootScope.usr.id,'msgto':$rootScope.users[i].id}).success(function(data) {
                                    if (data != undefined && data != "") {
                                        $rootScope.chat = data;
                                        setTimeout(function(){$(".popup-messages").scrollTop(9000);}, 1);
                                        $http.post('/chat/read', {'msgto':$rootScope.currid}).success(function() {
                                            for (var i = 0; i < $rootScope.users.length; i++) {
                                                if ($rootScope.users[i].id == $rootScope.currid) {
                                                    $rootScope.users[i].sent = undefined;
                                                }
                                            }
                                        });
                                    }
                });
                                }
                            } else {
                                $rootScope.users[i].sent = undefined;
                            }
                        }
                    }
                    
                    $("#chatsign").attr("data-badge", "");
                    $rootScope.newMsgs = 1;
                } else {
                    $("#chatsign").removeAttr("data-badge");
                    $rootScope.newMsgs = 0;
                }
            });
        }, 5000);
        $http.get('/comments/listen/' + $rootScope.picid.toString()).success(function(data) {});
    });
    
    $('#postprof').on('hide.bs.modal', function() {
       $rootScope.currusr = [];
    });
    
    $rootScope.removeusr = function(id) {
        swal({   title: "אתה בטוח?",   text: "מהרגע שהמשתמש נמחק לא ניתן להחזירו",   type: "warning",   showCancelButton: true,   confirmButtonColor: "#DD6B55",   confirmButtonText: "מחק",   closeOnConfirm: false }, function(){ $http.post("/users/delete", {'id': id}).success(function(data) {
            if (parseInt(data) > 0) {
                swal("נמחק", "המשתמש נמחק", "success");
            }
        });  
        }); 
    }
    
    $rootScope.changeModPic = function(src) {
        $rootScope.modpic = src;
    }
    
    $rootScope.changePoster = function(id) {
        $http.get("/users/" + id).success(function(data) {
            $rootScope.currusr = data;
            if ($rootScope.currusr.profilepic == "none") {
            $rootScope.currusr.profilepic = "/assets/images/profile/unknown.jpg";
        }
                
            switch ($rootScope.currusr.perm) {
                    case 1:
                        $rootScope.currusr.perm = "משתמש רגיל";
                    break;
                    case 2:
                        $rootScope.currusr.perm = "מאמן המועדון";                    
                    break;
                    case 3:
                        $rootScope.currusr.perm = "מפתח מערכת";
                    break;
                }
        });
    }
    
    $rootScope.changeSrc = function(imgsrc, title, info, picid) {
        $rootScope.comments = [];
        $rootScope.postloading = true;
        $rootScope.picid = picid;
        $http.get('/comments/' + $rootScope.picid).success(function(data) {
                var helpdata = [];
                for (var i = 0; i < data.length; i++) {
                    helpdata[i] = data[data.length - i - 1];
                }
                data = helpdata;
                   $rootScope.comments = data;
                    $rootScope.postloading = false;
                   setTimeout(function() {$('.commentcls').scrollTop(0); }, 0);
               });
        $(".mysrcyus").attr("src", imgsrc);
        $(".mysrcyus").attr("ng-src", imgsrc);
        $(".mytitleyus").html(title);
        $(".myinfoyus").html(info);
        var inter = setInterval(function() {
        $http.get('/comments/listen/' + picid.toString()).success(function(data) {
           if (data == "new") {
               $http.get('/comments/' + $rootScope.picid).success(function(data) {
                var helpdata = [];
                for (var i = 0; i < data.length; i++) {
                    helpdata[i] = data[data.length - i - 1];
                }
                data = helpdata;
                   $rootScope.comments = data;
                   setTimeout(function() {$('.commentcls').scrollTop(0); }, 90);
               });
            }
        });
        }, 800);
    }
    
    $rootScope.changePerm = function(id, perm) {
        if ($rootScope.usr.id == id) {
            swal("לא ניתן לשנות הרשאה לעצמך", "", "error");
        } else {
            $http.get("/auth/perm/" +id+ "/" + perm).success(function(data) {swal(data,"" ,"success")});
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
            $http.post("/auth/register", {'name':$('#regname').val(), 'lastname':$('#reglastname').val(), 'email': $('#regemail').val(),'username':$('#reguser').val(),'pass': $('#regpass').val(), 'passconf':$('#regpassconf').val()})
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

function init_map(){var myOptions = {zoom:13,center:new google.maps.LatLng(32.01,33),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(32.0192813,34.75112319999994)});infowindow = new google.maps.InfoWindow({content:'<strong>ג\'ודו ליאור - בת ים</strong><br>בת ים, החשמונאים 27<br>'});google.maps.event.addListener(marker, 'click', function(){infowindow.open(map,marker);});infowindow.open(map,marker);
//google.maps.event.addDomListener(window, 'load', init_map);
}

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
    }).when("/pics", {
            templateUrl: "/assets/views/photos.html"
    }).when("/users", {
            templateUrl: "/assets/views/users.html"
    }).otherwise({
            redirectTo: '/'
    });
    
}]);