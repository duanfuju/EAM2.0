/**
 * Created by Shangod on 2016/12/9.
 *
 *
 * 插件介绍：
 * 基于jquery的时间计算插件
 * 计算传递时间date的信息，返回值为对象，该对象包含以下信息：
 * timeObj = {
                date: date,       date的完整信息
                total: total,     date的所在月总天数
                year: year,       date的所在年份
                month: month,     date的所在月份
                day: day,         date所在的星期
                dayNum: dayNum,   date所在的天号
                weekStart: weekStart,    date所在周的开始时间
                weekEnd: weekEnd,        date所在周的结束时间（可能跨月跨年）
                monthStart: monthStart,  date所在月份的开始时间
                monthEnd: monthEnd       date所在月份的结束时间
            }
 * 使用方法：
 * 按先后顺序引入jquery，jquery.timerCount.js
 * (1)声明一个事件
 *      var date=new Date("2016/12/9");
 * (2)引用函数计算，结果保存在对象中
 *      var obj=$.timerCount(date);
 */
;
(function ($) {
    $.extend({
        "timerCount": function (date,type) {
            function Base(){}
            Base.prototype={
                init: function (date,type) {
                    var that=this;
                    var timeObj=this.render(date);
                    if(type==0){
                        timeObj.date=that.timeFormat(timeObj.date,0);
                        timeObj.monthStart=that.timeFormat(timeObj.monthStart,0);
                        timeObj.monthEnd=that.timeFormat(timeObj.monthEnd,0);
                        timeObj.seasonStart=that.timeFormat(timeObj.seasonStart,0);
                        timeObj.seasonEnd=that.timeFormat(timeObj.seasonEnd,0);
                        timeObj.weekStart=that.timeFormat(timeObj.weekStart,0);
                        timeObj.weekEnd=that.timeFormat(timeObj.weekEnd,0);
                    }
                    return timeObj;
                },
                render: function (date) {
                    var that=this;
                    var isDateType=that.isDateType(date);
                    if(isDateType){
                        return that.count(date);
                    }else{
                        var newDate=that.dateCreate(date);
                        if(newDate!='Invalid Date'){
                            return that.count(newDate);
                        }else{
                            alert('时间格式不正确');
                        }
                    }


                },
                //兼容性的日期创建
                dateCreate: function (date) {
                    if((typeof date)=='string'){
                        var newDate = new Date(date.replace(/-/g, "/"));

                    }else{
                        var newDate=new Date(date);
                    }
                    return newDate;
                },
                //存时间的基础信息，避免重复计算
                timeBase:{
                    date:"",
                    dateTime:"",
                    year:"",
                    month:"",
                    dayNum:"",
                    day:"",
                    total:"",
                    timeStart:""
                },
                //整体的计算函数
                count: function (date) {
                    var that=this;
                    var isLeapYear=that.isLeapYear(date);
                    that.timeBase.dateTime=date.getTime();
                    that.timeBase.year=date.getFullYear();
                    that.timeBase.month=date.getMonth();
                    that.timeBase.dayNum=date.getDate();
                    if(date.getDay()==0){
                        that.timeBase.day=7;
                    }else{
                        that.timeBase.day=date.getDay();
                    }
                    that.timeBase.total=that.getCountDays(date);
                    that.timeBase.timeStart=that.timeBase.year+'-'+(that.timeBase.month+1)+'-'+that.timeBase.dayNum;
                    var weekInfo=that.weekInfo(date);//计算周的基础信息
                    var monthInfo=that.monthInfo(date);//计算月的基础信息
                    var seasonInfo=that.seasonInfo(date);//计算季度的基础信息



                    var idate=(that.timeBase.year)+'-'+(that.timeBase.month+1)+'-'+(that.timeBase.dayNum);  //date的信息（字符串）
                    var iisLeapYear=isLeapYear;//是否是闰年
                    var itotal=that.timeBase.total;//date所在月的总天数（数字）
                    var iyear=that.timeBase.year;//date所在的年（数字）
                    var imonth=(that.timeBase.month+1);//date所在的月（数字）
                    var iday=that.timeBase.day;//date是星期几（数字）
                    var idayNum= that.timeBase.dayNum;//date的日期号（数字）
                    var iweekStart= weekInfo.weekStart;//date所在周的开始时间（字符串）
                    var iweekEnd= weekInfo.weekEnd;//date所在周的结束时间（字符串）
                    var imonthStart= monthInfo.monthStart;//date所在月的开始时间（字符串）
                    var imonthEnd= monthInfo.monthEnd;//date所在月的结束时间（字符串）
                    var iseasonStart=seasonInfo.seasonStart;//date所在季度的开始时间（字符串）
                    var iseasonEnd=seasonInfo.seasonEnd;//date所在季度的结束时间（字符串）

                    var timeObj={
                        date:date ,  //date的信息（字符串）
                        isLeapYear:iisLeapYear,//是否是闰年
                        total:itotal,//date所在月的总天数（数字）
                        year:iyear,//date所在的年（数字）
                        month:imonth,//date所在的月（数字）
                        day: iday,//date是星期几（数字）
                        dayNum: idayNum,//date的日期号（数字）
                        weekStart: iweekStart,//date所在周的开始时间（字符串）
                        weekEnd: iweekEnd,//date所在周的结束时间（字符串）
                        monthStart: imonthStart,//date所在月的开始时间（字符串）
                        monthEnd: imonthEnd,//date所在月的结束时间（字符串）
                        seasonStart:iseasonStart,//date所在季度的开始时间（字符串）
                        seasonEnd:iseasonEnd//date所在季度的结束时间（字符串）
                    };
                    return timeObj;


                },
                //时间格式化
                timeFormat: function (date,type) {
                    /*
                     * 时间初始化函数
                     * */
                    //var newDate=new Date(date);
                    //ie不支持“2017-03-07”格式的创建，为了兼容性需要做以下调整
                    if((typeof date)=='string'){
                        var newDate = new Date(date.replace(/-/g, "/"));

                    }else{
                        var newDate=new Date(date);
                    }
                    var timeArr=[];
                    timeArr[0]=newDate.getFullYear();
                    timeArr[1]=newDate.getMonth()+1;
                    timeArr[2]=newDate.getDate();
                    if(type===0){
                        timeArr[1]=('00'+timeArr[1]).slice(-2);
                        timeArr[2]=('00'+timeArr[2]).slice(-2);
                    }
                    return timeArr.join('-');
                },
                //判断参数是否是时间格式的对象
                isDateType: function (date) {
                    return Date.prototype.isPrototypeOf(date);
                },


                //判断是否是闰年
                isLeapYear: function (date) {
                    return (0 == date.getYear() % 4 && ((date.getYear() % 100 != 0) || (date.getYear() % 400 == 0)));
                },
                //计算一个月的天数
                getCountDays: function (date) {
                    var curDate = new Date();
                    /* 获取当前月份 */
                    var curMonth = curDate.getMonth();
                    /*  生成实际的月份: 由于curMonth会比实际月份小1, 故需加1 */
                    curDate.setMonth(curMonth + 1);
                    /* 将日期设置为0, 相当于往前调了一天 */
                    curDate.setDate(0);
                    /* 返回当月的天数 */
                    return curDate.getDate();
                },


                //计算周的信息
                weekInfo: function (date) {
                    var that=this;
                    var weekInfo={
                        weekStart: '',
                        weekEnd: ''
                    };
                    var startDateTime=that.timeBase.dateTime-(that.timeBase.day-1)*24*60*60*1000;
                    var endDateTime=that.timeBase.dateTime+(7-that.timeBase.day)*24*60*60*1000;
                    var startDate=that.timeFormat(startDateTime);
                    var endDate=that.timeFormat(endDateTime);

                    weekInfo.weekStart=startDate;
                    weekInfo.weekEnd=endDate;
                    return weekInfo;
                },
                //计算月的信息
                monthInfo: function (date) {
                    var that=this;
                    var monthInfo={
                        monthStart: '',
                        monthEnd: ''
                    };
                    var start=1;
                    var end=that.timeBase.total;
                    var startDate=that.timeFormat((that.dateCreate(that.timeBase.timeStart)).setDate(start));
                    var endDate=that.timeFormat((that.dateCreate(that.timeBase.timeStart)).setDate(end));
                    monthInfo.monthStart=startDate;
                    monthInfo.monthEnd=endDate;
                    return monthInfo;
                },
                //计算季度的信息
                seasonInfo: function (date) {
                    var that=this;
                    var seasonInfo={
                        seasonStart:'',
                        seasonEnd:''
                    };
                    var curYear=that.timeBase.year;
                    var curMonth=that.timeBase.month+1;
                    if(curMonth<4){
                        seasonInfo.seasonStart=curYear+'-1-1';
                        seasonInfo.seasonEnd=curYear+'-3-31';
                    }else if(curMonth<7){
                        seasonInfo.seasonStart=curYear+'-4-1';
                        seasonInfo.seasonEnd=curYear+'-6-30';
                    }else if(curMonth<10){
                        seasonInfo.seasonStart=curYear+'-7-1';
                        seasonInfo.seasonEnd=curYear+'-9-30';
                    }else{
                        seasonInfo.seasonStart=curYear+'-10-1';
                        seasonInfo.seasonEnd=curYear+'-12-31';
                    }
                    return seasonInfo;
                }


            };
            var base=new Base();
            return base.init(date,type);
        }
    });
})(jQuery);
