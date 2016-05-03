//创建闭包，避免全局污染
(function($) {
    // 在我们插件容器内，定义一个私有方法
    var privateFunction = function() {
        // code here
    };
 
    // 通过字面量创造一个对象，存储我们需要的共有方法
    var methods = {
        // 在字面量对象中定义每个单独的方法
        init: function() {
            plugin.settings = $.extend({}, defaults, options);
            var pageCount = $("input[name="+"]")
            // 为了更好的灵活性，对来自主函数，并进入每个方法中的选择器其中的每个单独的元素都执行代码
            return this.each(function() {
                // 为每个独立的元素创建一个jQuery对象
                var $this = $(this);
                // 执行代码 例如：privateFunction()
            });
        },
        destroy: function() {
            // 对选择器每个元素都执行方法
            return this.each(function() {
                // 执行代码
            });
        }
    };
 
    $.fn.pager = function() {
        //提供默认参数选项


        // 获取我们的方法，遗憾的是，如果我们用function(method){}来实现，这样会毁掉一切的
        var method = arguments[0];
 
        // 检验方法是否存在
        if(methods[method]) {
            // 如果方法存在，存储起来以便使用
            // 注意：我这样做是为了等下更方便地使用each（）
            method = methods[method];
 
        // 如果方法不存在，检验对象是否为一个对象（JSON对象）或者method方法没有被传入
        } else if (typeof method === "object" || !method ) {
            // 如果我们传入的是一个对象参数，或者根本没有参数，init方法会被调用
            method = methods.init;

        } else {
            // 如果方法不存在或者参数没传入，则报出错误。需要调用的方法没有被正确调用
            $.error("Method" +  method + "does not exist on jQuery.pluginName");
            return this;
        }
 
        // 调用我们选中的方法
        // 再一次注意我们是如何将each（）从这里转移到每个单独的方法上的
        return method.call(this);
    };
 
})(jQuery);