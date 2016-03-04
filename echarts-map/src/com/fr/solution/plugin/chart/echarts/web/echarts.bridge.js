/**
 * Created by richie on 16/1/29.
 */
EChartsFactory = function(options, $dom) {
    this.options = options;
    this.$dom = $dom;
    this.chartID = options.chartID;
    this.autoRefreshTime = options.autoRefreshTime || 0;

    this.width = options.width || $dom.width();
    this.height = options.height || $dom.height();
    this.sheetIndex = options.sheetIndex || 0;
    this.ecName = options.ecName || '';

    FR.Chart.WebUtils._installChart(this, this.chartID);
};

EChartsFactory.prototype = {

    constructor : EChartsFactory,

    inits : function() {
        this.newCharts = echarts.init(this.$dom[0]);
        this.newCharts.setOption(this.options.chartAttr);
    },

    resize : function() {
        this.newCharts.resize();
    },
    refresh:function() {

    },

    refreshData:function(options){

    },

    setData:function(options, aimation){

    }
};