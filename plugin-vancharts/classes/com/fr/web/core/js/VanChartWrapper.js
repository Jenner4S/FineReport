/**
 * Created by eason on 15/10/19.
 */
/**
 * Created by eason on 15/8/26.
 */
VanChartWrapper = function(options, dom){
    this.options = options;
    this.dom = dom;
    this.chartID = options.chartID;
    this.autoRefreshTime = options.autoRefreshTime || 0;

    this.width = options.width || dom.width();// 补充从dom获取.
    this.height = options.height || dom.height();
    this.sheetIndex = options.sheetIndex || 0;
    this.ecName = options.ecName || '';

    FR.Chart.WebUtils._installChart(this, this.chartID);
};

VanChartWrapper.prototype = {

    constructor:VanChartWrapper,

    inits:function(){

        this.vanCharts = VanCharts.init(this.dom[0]);

        this.vanCharts.setOptions(this.options.chartAttr);

        this._autoRefresh();
    },

    resize:function(){
        this.vanCharts.resize();
    },

    refresh:function(){

    },

    refreshData:function(options){
        this.vanCharts.resize(options);
    },

    //数据监控的刷新方式
    setData:function(options, aimation){
        this.vanCharts.setData(options, aimation);
    },

    _autoRefresh:function(){

        var time = this.autoRefreshTime || 0;

        if(time < 1){
            return;
        }

        var self = this;

        setInterval(function () {
            var ID = FR.cjkEncode(self.chartID);
            var width = self.width || 0;
            var height = self.height || 0;
            var sheetIndex = self.sheetIndex;
            var ecName = self.ecName;
            if(FR.servletURL) {
                FR.ajax({
                    type: 'GET',
                    url: FR.servletURL + '?op=chartauto',
                    data: {
                        cmd: 'chart_auto_refresh',
                        sessionID: FR.SessionMgr.getSessionID(),
                        chartID: ID,
                        chartWidth: width,
                        chartHeight: height,
                        sheetIndex : sheetIndex,
                        ecName : ecName,
                        __time: new Date().getTime()
                    },
                    dataType: 'json',
                    success: function (chartRelateJS) {
                        var attrList = chartRelateJS.relateChartList;
                        for (var i = 0, len = attrList.length; i < len; i++) {
                            var chartID = FR.Chart.WebUtils._getChartIDAndIndex(attrList[i].id);
                            var chartSet = FR.ChartManager[chartID[0]];
                            if (chartSet && chartSet[chartID[1]]) {
                                var chartAttr = attrList[i].chartAttr;
                                var animation = attrList[i].animation;
                                chartSet[chartID[1]].setData(chartAttr, animation);
                            }
                        }
                    }
                });
            }

        }, time * 1000);
    },

    _chartRelated: function (attrList) {
        for (var i = 0; i < attrList.length; i++) {
            var chartID = FR.Chart.WebUtils._getChartIDAndIndex(attrList[i].id);
            var chartSet = FR.ChartManager[chartID[0]];
            if (chartSet && chartSet[chartID[1]]) {
                var chartAttr = attrList[i].chartAttr;
                chartSet[chartID[1]].refreshData(chartAttr);
            }
        }
    },

    dealChartAjax: function (para) {
        var para = para || "";
        var ID = FR.cjkEncode(this.chartID);
        var width = this.width || 0;
        var height = this.height || 0;
        var self = this;
        var sheetIndex = this.sheetIndex;
        var ecName = this.ecName;
        FR.ajax({
            type: 'GET',
            url: FR.servletURL + '?op=chartlink' + para,
            data: {
                cmd: 'refresh_relate_data',
                sessionID: FR.SessionMgr.getSessionID(),
                chartID: ID,
                chartWidth: width,
                chartHeight: height,
                sheetIndex : sheetIndex,
                ecName : ecName,
                __time: new Date().getTime()
            },
            dataType: 'json',
            success: function (chartRelateJS) {
                self._chartRelated(chartRelateJS.relateChartList);
            }
        });
    }

};