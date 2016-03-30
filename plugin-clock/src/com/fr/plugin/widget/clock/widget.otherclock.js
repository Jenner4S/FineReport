(function () {
    $.extend(FR, {
        rotateTo : function(d) {
            return '-webkit-transform:rotate(' + d + 'deg);-moz-transform:rotate(' + d + 'deg);-o-transform:rotate(' + d + 'deg);-ms-transform:rotate(' + d + 'deg);'
        }
    });
    /**
     * @class FR.OtherClock
     * @extends FR.Widget
     * 这个时钟是用CSS3实现的
     */
    FR.OtherClock = FR.extend(FR.Widget, {
        _defaultConfig: function () {
            return $.extend(FR.Widget.superclass._defaultConfig.apply(), {
                baseCls: 'fr-clock'
            });
        },

        _init: function () {
            FR.OtherClock.superclass._init.apply(this, arguments);
            this._buildAndRun();
        },

        _createClockHtml: function (o) {
            var p = 'position:absolute;left:50%;top:50%;', hw = o.w * 0.04, hh = o.w * 0.52, mw = o.w * 0.04, mh = o.w * 0.64, sw = o.w * 0.02, sh = o.w * 0.98, a = "";

            function rotateTo(d) {
                return '-webkit-transform:rotate(' + d + 'deg);-moz-transform:rotate(' + d + 'deg);-o-transform:rotate(' + d + 'deg);-ms-transform:rotate(' + d + 'deg);'
            }

            function setCover(s, z, c) {
                return '<div style="width:' + (o.w * s) + 'px;height:' + (o.w * s) + 'px;' + p + 'z-index:' + z + ';margin-left:' + (-o.w * s * 0.5) + 'px;margin-top:' + (-o.w * s * 0.5) + 'px;border-radius:' + (o.w * s * 0.5) + 'px;background-color:' + c + ';"></div>'
            }

            function setHands(hand, width, height, color, index) {
                return '<p class="' + hand + '" style="width:' + width + 'px;height:' + height + 'px;' + p + 'z-index:' + index + ';margin-left:' + (-width * 0.5) + 'px;margin-top:' + (-height * 0.5) + 'px;"><span style="display:block;width:100%;height:' + (height * 0.7) + 'px;background-color:' + color + ';"></span></p>'
            }

            for (var n = 0; n < 30; n++) {
                var nd = n * 6;
                a += '<p style="width:' + (o.w * 0.02) + 'px;height:100%;' + p + 'z-index:0;margin-left:' + (-o.w * 0.01) + 'px;margin-top:' + (-o.w * 0.5) + 'px;background:#444;' + rotateTo(nd) + '"></p>'
            }
            for (var j = 0; j < 6; j++) {
                var jd = j * 30;
                a += '<p style="width:' + (o.w * 0.04) + 'px;height:100%;' + p + 'z-index:2;margin-left:-' + (o.w * 0.02) + 'px;margin-top:' + (-o.w * 0.5) + 'px;background:#333;' + rotateTo(jd) + '"></p>'
            }
            for (var i = 1; i < 13; i++) {
                var id = i * 30;
                a += '<p style="width:' + (o.w * 0.24) + 'px;height:' + (o.w * 0.92) + 'px;' + p + 'z-index:4;margin-left:' + (-o.w * 0.12) + 'px;margin-top:' + (-o.w * 0.46) + 'px;' + rotateTo(id) + '"><span style="display:block;width:100%;height:' + (o.w * 0.14) + 'px;line-height:' + (o.w * 0.14) + 'px;' + rotateTo(-id) + '">' + i + '</span></p>'
            }
            var pc = '<div id="' + o.id + '" style="width:' + o.w + 'px;height:' + o.w + 'px;position:absolute;' + 'left:' + o.left + 'px;'+'top:'+ o.top + 'px;' +'border-radius:' + (o.w * 0.54) + 'px;border:' + (o.w * 0.04) + 'px solid ' + o.c + ';background-color:' + o.bc + ';text-align:center;font:' + (o.w * 0.12) + 'px \'Helvetica\';">' + setCover(0.96, 1, o.bc) + setCover(0.92, 3, o.bc) + setCover(0.08, 8, '#555') + setCover(0.02, 9, '#eee') + setHands('clock-h', hw, hh, o.hc, 5) + setHands('clock-m', mw, mh, o.mc, 6) + setHands('clock-s', sw, sh, o.sc, 7);
            pc += a;
            pc += '</div>';
            return pc;
        },

        run : function() {
            var self = this;
            var t = new Date(), h = t.getHours(), m = t.getMinutes(), s = t.getSeconds();
            if (h > 12) {
                h -= 12
            }
            $('.clock-s', this.element).each(function(){
                this.style.cssText += FR.rotateTo(6 * s);
            });
            $('.clock-m', this.element).each(function(){
                this.style.cssText += FR.rotateTo(6 * m + 0.1 * s);
            });
            $('.clock-h', this.element).each(function(){
                this.style.cssText += FR.rotateTo(30 * h + 0.5 * m + s / 120);
            });
            if (this.stopped !== true) {
                setTimeout(function () {
                    self.run();
                }, 30)
            }
        },

        /**
         * 对外开放的API，在停止时钟转动后，重新启动
         */
        start : function() {
            this.stopped = false;
            this.run();
        },

        /**
         * 停止时钟转动
         */
        stop : function() {
            this.stopped = true;
        },

        _buildAndRun : function() {
            var o = this.options;
            var w = Math.min(o.width, o.height);
            this.element[0].innerHTML = '';
            this.element[0].innerHTML = this._createClockHtml({
                id : o.widgetName,
                w: w - w * 0.04 * 2,
                top : w == o.width ? (o.height - w) / 2 : 0,
                left : w == o.height ? (o.width - w) / 2 : 0,
                c: "#6c0",		// 钟框颜色
                bc: "#fff",		// 钟面颜色
                hc: "#333",		// 时针颜色
                mc: "#DDB11A",	// 分针颜色
                sc: "#DC0D07"	// 秒针颜色
            });
            var self = this;
            setTimeout(self.run.createDelegate(self), 200);
        },

        doResize: function (give) {
            FR.OtherClock.superclass.doResize.apply(this, arguments);
            this._buildAndRun();
        }

    });
    $.shortcut("clock", FR.OtherClock);
})(jQuery);