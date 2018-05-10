define(["text!modules/module3/module3.html", "text!modules/module3/module3.css"], function (htmlTemp, cssTemp) {
   var module = {
       init:function(){
           $(".content-wrapper").html(htmlTemp);
           $(".css-attr").html(cssTemp);
           // //打开页面跳出帮助
           // var steplist = $.WebPageGuide({showCloseButton:true});
           // steplist.newGuidStep("#bugg","这是标题1啊","更嫩诶诶额");
           // steplist.newGuidStep(".bugg1","这是标题2啊","更嫩诶诶额");
           // steplist.startGuide();
           // //打开页面跳出帮助
           $(".contentb").click(function(){
               $('.toggle').not($(this).next()).slideUp("normal");
               $(this).next().slideToggle("normal");
           });
       }
   }
    return module;
});


