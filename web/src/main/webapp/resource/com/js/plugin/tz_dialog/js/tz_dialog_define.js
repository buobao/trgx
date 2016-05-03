/*! tz_Dialog V2.0 | (c) 2015, 2016 http://www.tanzhouedu.com/ */
$.tzDialog_define = function(opts){
  var animate_r = tz_animateIn();
  // 创建一个插件模板
  var $dialog = $("<div class='tz_dialog "+animate_r+"'>"+
  "		<div class='t_title' style='width:"+(opts.width+130)+"px'>"+
  "			<div class='t_h3'>"+opts.title+"</div>"+
  "			<a href='#' class='t_close'>X</a>"+
  "		</div>"+
  "		<div class='t_content' style='width:"+(opts.width+130)+"px' >"+
  "			<div class='t_message'>"+
  "             <input class='t_con' type='text' id='tz_dialog_input' placeholder='"+opts.content+"'  style='width:90%;margin-left: 12px;'/>"+
  "				<div class='t_clear'></div>"+
  "			</div>"+
  "			<div class='t_btn'>"+
  "				<input type='button' id='sure' value='确定' />"+
  "				<input type='button' id='cancle' value='取消' />"+
  "			</div>"+
  "		</div>"+
  "	</div>");

  // "<div class='tz_yy'></div>"  阴影部分

  // 追加模板
  $("body").append($dialog).append("<div class='tz_yy'></div>");
  // 动态居中定位插件
  tz_center($dialog);
  // 浏览器窗口改变的时候居中定位
  initEvent($dialog,opts);

}

// 动态居中定位插件
function tz_center($dialog){
  var width = $dialog.width();
  var height = $dialog.height();
  var ww = $(window).width();
  var wh = $(window).height();
  var _left = (ww - width) / 2;
  var _top = (wh - height) / 2;
  $dialog.css({top:_top,left:_left});
}

// 浏览器窗口改变的时候居中定位
function initEvent($dialog,opts){
  $(window).resize(function(){
    tz_center($dialog);
  });

  // 确定按钮
  $dialog.find("#sure").click(function(){
    var animateOut = tz_animateOut();
    $dialog.removeClass("animated").addClass(animateOut);
    $dialog.next().remove();
    $dialog.fadeOut(500,function(){
      $(this).remove();
    });
    if(opts.callback){
      opts.callback(true);
    }
  });

  // 取消按钮
  $dialog.find("#cancle").click(function(){
    var animateOut = tz_animateOut();
    $dialog.removeClass("animated").addClass(animateOut);
    $dialog.next().remove();
    $dialog.fadeOut(500,function(){
      $(this).remove();
    });
    if(opts.callback){
      opts.callback(false);
    }
  });


  // 拖拽
  $dialog.find(".t_title").mousedown(function(i){

    var dialogbox = $(this).parent(".tz_dialog");

    // 鼠标点击的x和y坐标位置
    var a = i.clientX;
    var b = i.clientY;
    //当期距离左顶点和顶部的距离
    var left = dialogbox.offset().left;
    var top = dialogbox.offset().top;

    // 设置边界(最大限制，边距)
    var maxLeft = $(window).width() - dialogbox.width();
    var maxTop = $(window).height() - dialogbox.height();

    // 默认状态打开
    var flag = true;
    // 开始拖拽
    $(document).mousemove(function(e){
      if(flag){
        var x = e.clientX;
        var y = e.clientY;
        // 计算拖拽完成后的坐标位置
        var _left = x - a + left;
        var _top = y - b + top;

        if(_left <= 0){_left = 0;}
        if(_top <= 0){_top = 0;}

        // 限制溢出的边界
        if(_left >= maxLeft){_left = maxLeft;}
        if(_top >= maxTop){_top = maxTop;}

        $(".tz_dialog").css({left:_left,top:_top});
      }
    }).mouseup(function(){ // 松开鼠标
      flag = false; // 关闭
    });

  });

  // 点击关闭
  $dialog.find(".t_close").click(function(e){
    // 直接删除
    // $(".tz_dialog").remove();

    // $(".tz_dialog").hide(2000);

    /*
     $(".tz_dialog").slideUp(1000,function(){
     $(this).remove();
     });
     */

    /*
     $(".tz_dialog").fadeOut(1000,function(){
     $(this).remove();
     });
     */
    //如果提供了事件对象，则这是一个非IE浏览器
    if ( e && e.preventDefault ) {
      //阻止默认浏览器动作(W3C)
      e.preventDefault();
    }else{
      //IE中阻止函数器默认动作的方式
      window.event.returnValue = false;
    }
    var animateOut = tz_animateOut();
    // 删除原有的动画，添加淡出的动画效果
    $(".tz_dialog").removeClass("animated").addClass(animateOut);
    // 关闭阴影层
    $(".tz_yy").remove();
    // 关闭层
    $(".tz_dialog").fadeOut(500,function(){$(this).remove();});



  });

}
// 触发加载tz_dialog插件，随机动画
function tz_animateIn(index){
  var animateIn = [];
  animateIn.push("animated bounce");//0
  animateIn.push("animated tada");//1
  animateIn.push("animated swing");//2
  animateIn.push("animated wobble");//3
  animateIn.push("animated flip");//4
  animateIn.push("animated flipInX");//5
  animateIn.push("animated flipInY");//6
  animateIn.push("animated fadeIn");//7
  animateIn.push("animated fadeInUp");//8
  animateIn.push("animated fadeInDown");//9
  animateIn.push("animated fadeInLeft");//10
  animateIn.push("animated fadeInRight");//11
  animateIn.push("animated fadeInUpBig");//12
  animateIn.push("animated fadeInDownBig");//13
  animateIn.push("animated fadeInLeftBig");//14
  animateIn.push("animated fadeInRightBig");//15
  animateIn.push("animated bounceIn");//16
  animateIn.push("animated bounceInUp");//17
  animateIn.push("animated bounceInDown");//18
  animateIn.push("animated bounceInLeft");//19
  animateIn.push("animated bounceInRight");//20
  animateIn.push("animated rotateIn");//21
  animateIn.push("animated rotateInUpLeft");//22
  animateIn.push("animated rotateInDownLeft");//23
  animateIn.push("animated rotateInUpRight");//24
  animateIn.push("animated rotateInDownRight");//25
  animateIn.push("animated rollIn");//26
  if(!index){
    var len = animateIn.length;
    var r = Math.floor(Math.random()*(len - 1)+1);
    return animateIn[r];
  } else {
    return animateIn[index];
  }
}

// 动画消失
function tz_animateOut(index){
  var animateOut = [];
  animateOut.push("animated fadeOut");//2
  animateOut.push("animated fadeOutUp");//3
  animateOut.push("animated fadeOutDown");//4
  animateOut.push("animated fadeOutLeft");//5
  animateOut.push("animated fadeOutRight");//6
  animateOut.push("animated fadeOutUpBig");//7
  animateOut.push("animated fadeOutDownBig");//8
  animateOut.push("animated fadeOutLeftBig");//9
  animateOut.push("animated fadeOutRightBig");//10
  animateOut.push("animated bounceOut");//11
  animateOut.push("animated bounceOutUp");//12
  animateOut.push("animated bounceOutDown");//13
  animateOut.push("animated bounceOutLeft");//14
  animateOut.push("animated bounceOutRight");//15
  animateOut.push("animated rotateOut");//16
  animateOut.push("animated rotateOutUpLeft");//17
  animateOut.push("animated rotateOutDownLeft");//18
  animateOut.push("animated rotateOutDownRight");//19
  animateOut.push("animated rollOut");//21

  if(!index){
    var len = animateOut.length;
    var r = Math.floor(Math.random()*(len-1)+1);
    return animateOut[r];
  } else {
    return animateOut[index];
  }

}
