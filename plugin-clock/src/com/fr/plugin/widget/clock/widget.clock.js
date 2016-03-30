(function () {
    /**
     * @class FR.Clock
     * @extends FR.Widget
     * ���ʱ��ʹ��canvas����
     */
    FR.Clock = FR.extend(FR.Widget, {
        _defaultConfig: function () {
            return $.extend(FR.Widget.superclass._defaultConfig.apply(), {
                baseCls: 'fr-clock'
            });
        },

        _init: function () {
            FR.Clock.superclass._init.apply(this, arguments);
            var o = this.options;
            var wh = Math.min(o.width, o.height);
            this.$canvas = $("<canvas>").attr({
                width: wh,
                height: wh
            }).css({
                position : 'absolute',
                top : wh == o.width ? (o.height - wh) / 2 : 0,
                left : wh == o.height ? (o.width - wh) / 2 : 0
            }).appendTo(this.element);
            this.draw(this.$canvas[0]);
        },

        draw: function (canvas) {
            var ctx = canvas.getContext("2d");
            this.ctx = ctx;
            var radius = canvas.height / 2;
            ctx.translate(radius, radius);
            radius = radius * 0.90
            setInterval(drawClock, 1000);

            function drawClock() {
                drawFace(ctx, radius);
                drawNumbers(ctx, radius);
                drawTime(ctx, radius);
            }

            function drawFace(ctx, radius) {
                var grad;
                ctx.beginPath();
                ctx.arc(0, 0, radius, 0, 2 * Math.PI);
                ctx.fillStyle = 'white';
                ctx.fill();
                grad = ctx.createRadialGradient(0, 0, radius * 0.95, 0, 0, radius * 1.05);
                grad.addColorStop(0, '#333');
                grad.addColorStop(0.5, 'white');
                grad.addColorStop(1, '#333');
                ctx.strokeStyle = grad;
                ctx.lineWidth = radius * 0.1;
                ctx.stroke();
                ctx.beginPath();
                ctx.arc(0, 0, radius * 0.1, 0, 2 * Math.PI);
                ctx.fillStyle = '#333';
                ctx.fill();
            }

            function drawNumbers(ctx, radius) {
                var ang;
                var num;
                ctx.font = radius * 0.15 + "px arial";
                ctx.textBaseline = "middle";
                ctx.textAlign = "center";
                for (num = 1; num < 13; num++) {
                    ang = num * Math.PI / 6;
                    ctx.rotate(ang);
                    ctx.translate(0, -radius * 0.85);
                    ctx.rotate(-ang);
                    ctx.fillText(num.toString(), 0, 0);
                    ctx.rotate(ang);
                    ctx.translate(0, radius * 0.85);
                    ctx.rotate(-ang);
                }
            }

            function drawTime(ctx, radius) {
                var now = new Date();
                var hour = now.getHours();
                var minute = now.getMinutes();
                var second = now.getSeconds();
                //hour
                hour = hour % 12;
                hour = (hour * Math.PI / 6) +
                (minute * Math.PI / (6 * 60)) +
                (second * Math.PI / (360 * 60));
                drawHand(ctx, hour, radius * 0.5, radius * 0.07);
                //minute
                minute = (minute * Math.PI / 30) + (second * Math.PI / (30 * 60));
                drawHand(ctx, minute, radius * 0.8, radius * 0.07);
                // second
                second = (second * Math.PI / 30);
                drawHand(ctx, second, radius * 0.9, radius * 0.02);
            }

            function drawHand(ctx, pos, length, width) {
                ctx.beginPath();
                ctx.lineWidth = width;
                ctx.lineCap = "round";
                ctx.moveTo(0, 0);
                ctx.rotate(pos);
                ctx.lineTo(0, -length);
                ctx.stroke();
                ctx.rotate(-pos);
            }
        },

        doResize: function (give) {
            //var o = this.options, canvas = this.$canvas[0];
            FR.Clock.superclass.doResize.apply(this, arguments);
            //var wh = Math.min(o.width, o.height);
            //canvas.width = wh;
            //canvas.height = wh;
            //canvas.style.top = wh == o.width ? (o.height - wh) / 2 : 0 + "px";
            //canvas.style.left = wh == o.height ? (o.width - wh) / 2 : 0 + "px";
            //this.ctx.clearRect(0, 0, 2000, 2000);
            //this.draw(this.$canvas[0]);
        }

    });
    $.shortcut("clock", FR.Clock);
})(jQuery);