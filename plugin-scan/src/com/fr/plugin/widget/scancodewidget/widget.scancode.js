/**
 * Created by joyxu on 2016/3/8.
 */
//如果要使用canvas实现的时钟

(function ($) {
    FR.Scan = FR.extend(FR.Widget, {
        _defaultConfig: function () {
            return $.extend(FR.Widget.superclass._defaultConfig.apply(), {
                baseCls: 'fr-scan'
            });
        },

        _init: function () {
            FR.Scan.superclass._init.apply(this, arguments);

            var content = $("<input type='text'>").addClass("fr-widget-scan-scaninput")
                .css("backgroundImage", "url(" + FR.servletURL + "?op=resource&resource=/com/fr/plugin/widget/scancodewidget/images/qrcode.png)")
                .click(
                function () {
                alert(FR.i18nText("FR_Designer-Scan_PC_No_Scan"));
               $(this).addClass("fr-widget-scan-click");
            }).blur(function(){
                $(this).removeClass("fr-widget-scan-click");
            });

            this.element.append(content);
        }
    });
    $.shortcut("scan", FR.Scan);
})(jQuery);