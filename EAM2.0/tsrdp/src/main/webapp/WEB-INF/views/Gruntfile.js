module.exports = function (grunt) {
    // 项目配置
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        /**js压缩配置*/
        uglify: {
            options: {
                banner: '/*!<%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            common:{
                src:"common.js",
                dest:"common.min.js"
            },

        },
        /**css压缩*/
        cssmin: {
            options: {
                banner: '/*!<%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            zTree:{
                src:"plugins/ztree/css/zTreeStyle/zTreeStyle.css",
                dest:"plugins/ztree/css/zTreeStyle/zTreeStyle.min.css"
            },
            dataTables:{
                src:"plugins/datatables/dataTables.bootstrap.css",
                dest:"plugins/datatables/dataTables.bootstrap.min.css"
            }
        },
        /**需合并的文件配置*/
        concat: {
            options: {
                banner: '/*!<%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            pluginsJs: {
                dest: "dest/plugins/plugins.min.js",
                src: ["plugins/jQuery/jquery-2.2.3.min.js", "plugins/bootstrap/js/bootstrap.min.js",
                    "plugins/datatables/jquery.dataTables.min.js", "plugins/datatables/dataTables.bootstrap.min.js",
                    "plugins/ztree/js/jquery.ztree.all.min.js", "common.min.js"]
            },
            pluginsCss:{
                dest:"dest/plugins/plugins.min.css",
                src:["plugins/bootstrap/css/bootstrap.min.css","plugins/datatables/dataTables.bootstrap.min.css",
                    "plugins/ztree/css/zTreeStyle/zTreeStyle.min.css","plugins/font-awesome/css/font-awesome.min.css"]
            }
        },
        /**js校验脚本错误*/
        jshint: {
            pluginsJs:["common.js"]
        },
        /**css校验*/
        csslint: {
            pluginsCSS:["index.css"]
        }
    });
    // 加载提供任务的插件
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-csslint');
    // 默认任务
    grunt.registerTask('default', ['jshint', 'csslint', 'cssmin', 'uglify', 'concat']);
}