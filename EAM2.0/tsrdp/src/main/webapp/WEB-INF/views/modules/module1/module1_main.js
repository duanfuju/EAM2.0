define(["text!modules/module1/module1.html","text!modules/module1/module1.css","modules/module1/module1"],function (html,css,module) {
    $(".content-wrapper").html(html);
    module.init();
    console.log(111)
});


