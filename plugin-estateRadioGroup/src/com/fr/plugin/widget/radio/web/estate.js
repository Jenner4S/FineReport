;
(function ($) {
    /**
     * 地产行业通用单选按钮组
     *
     *     @example
     *     var editor = new FR.EstateRadioGroup({
     *          renderEl : 'body',
     *          width : 200,
     *          height : 100,
     *          items : [
     *              {text : 'apple', value : '1'},
     *              {text : 'orange', value : '2'},
     *              {text : 'banana', value : '3'}
     *          ]
     *     });
     *
     * @class FR.EstateRadioGroup
     * @extends FR.ToggleButtonGroup
     *
     * @cfg {JSON} o 配置属性
     * @cfg {Boolean} [o.showDefault=true] 是否显示默认值，如果为false，则web端默认不选中默认值
     * @cfg {FRFont} [o.defaultFont=""] 选项未选中时的字体
     * @cfg {FRFont} [o.selectedFont=''] 选项被选中时的字体
     */
    FR.EstateRadioGroup = FR.extend(FR.ToggleButtonGroup, /**@class FR.EstateRadioGroup*/{

        lbox_class: 'fr-group-box',
        sbox_class: 'fr-group-span',

        _init: function () {
            FR.EstateRadioGroup.superclass._init.apply(this, arguments);
        },

        initData: function () {
            if (this.options.data) {
                return;
            }

            if (this.options.controlAttr) {
                this.setSource(this.options.controlAttr);
                this._confirmEvents = function () {
                    this.fireEvent(FR.Events.AFTERINIT);
                };
                return;
            }

            if (this.options.widgetUrl) {
                this.options.data = FR.DataFactory.createSynchronJSONData(
                    this.options.widgetUrl, false);
                this.options.data.resetStatus(this.createDependencePara4Data());
            } else if (this.options.items) {
                this.options.data = FR.DataFactory
                    .createSynchronArrayData(this.options.items);
            }
            var self = this;
            this.options.data.afterRead(function (items) {
                if (self.isBoxBuild !== true) {
                    self._buildBox(items);
                }
            });
            this.options.data.loadData();
        },

        _setItems: function (items) {
            var records = items || [], len = records.length;
            var rname;
            if (this.options.widgetName) {
                rname = this.options.widgetName;
            } else {
                rname = "noNameChild";
            }
            var self = this;
            $.each(records, function (idx, it) {
                if (self.options.adaptive) {
                    var outter = $("<span></span>")
                        .addClass(self.sbox_class)
                        .appendTo(self.$container);
                } else {
                    var outter = $("<span/>")
                        .addClass(self.sbox_class);
                    var gridElement = {
                        column: idx % self.gridConfig.columns,
                        row: Math.floor(idx / self.gridConfig.columns),
                        el: outter
                    };
                    self.gridConfig.items.push(gridElement);
                }

                self.buttonArray[idx] = self.initItemRadio(idx,it,outter,rname);
                self.buttonArray[idx].on(
                    FR.Events.BEFORESTATECHANGE, function () {
                        self
                            .fireEvent(FR.Events.BEFORESTATECHANGE);
                    });
                self.buttonArray[idx].on(FR.Events.STATECHANGE,
                    function () {
                        self.assureOneButtonChecked(this);
                        if (this.selected() === true || self.getValue() == "") {
                            self.fireEvent(FR.Events.STATECHANGE, idx);
                            self.fireEvent(FR.Events.AFTEREDIT);
                        } else {
                            self.oriValue = self.getValue();
                        }
                    });
            });
            
            if (!self.options.adaptive) {
                this.grid = new FR.GridLayout(this.gridConfig);
                this.grid.element.doLayout();
                this._checkTable();
            }
        },

        initItemRadio: function(idx,it,outter,rname){
           return new FR.EstateRadioButton({
                renderEl: $("<div/>").appendTo(outter),
                disabled: this.options.disabled,
                text: it.getShowValue(),
                fieldValue: it.getValue(),
                sessionID: this.options.sessionID,
                name: rname,
                defaultFont: this.options.defaultFont,
                selectedFont: this.options.selectedFont
            });
        },
        assureOneButtonChecked: function (radio) {
            var foundSelected = false;
            if (radio.isSelected()) {
                for (var i = 0, len = this.buttonArray.length; i < len; i++) {
                    if (this.buttonArray[i] == radio) {
                        foundSelected = true;
                        continue;
                    }
                    this.buttonArray[i].setSelectedWithoutEvent(false);
                }
            }
            if (!foundSelected && this.options.assureSelect === true) {
                radio.setSelectedWithoutEvent(true);
            }
        },

        getValue: function () {
            return this.getTV(false);
        },

        getText: function () {
            return this.getTV(true);
        },

        /**
         * 获取组件的显示值或者实际值
         * @private
         * @param forShowValue 是否返回显示值
         * @returns {String} 返回控件的值
         */
        getTV: function (forShowValue) {
            var self = this;
            var value = "";
            $.each(self.buttonArray, function (idx, it) {
                if (it.selected()) {
                    var record = self.options.data.getRecord(idx);
                    if (record) {
                        value = forShowValue ? record.getShowValue() : record.getValue();
                    }
                }
            });
            return value;
        },

        _dealValueWithEvents: function (value) {
            var self = this;
            var argument = arguments[1];
            var setValueFunc = function () {
                if (self.isBoxBuild !== true) {
                    return;
                }
                clearInterval(sh);
                var oldValue = self.options.value;

                if (typeof value == 'boolean') {
                    value = value ? 'true' : 'false';
                }

                //默认值的时候, value是"", 要求选中
                //if (!value && value !== 0) {
                //    return;
                //}

                for (var i = 0; i < self.buttonArray.length; i++) {
                    self.buttonArray[i].setSelectedWithoutEvent(false);
                }

                for (var i = 0, len = self.buttonArray.length; i < len; i++) {
                    if (self.buttonArray[i].options.fieldValue == value) {
                        if (argument !== false) {
                            self.buttonArray[i].setSelected(true);
                        } else {
                        	var o = self.options;
                        	var button = self.buttonArray[i];
                        	button.setSelectedWithoutEvent(true);
                        	self.dealWithFont(o.selectedFont, $(button.$btn));
                        }
                        break;
                    }
                }
            };
            if (this.isBoxBuild === true) {
                setValueFunc();
                return;
            }
            var sh = setInterval(setValueFunc, 100);
        },
        
        dealWithFont: function(font, $item){
			if (font.fontColor) {
				$item.css('color', font.fontColor);
			}
			if (font.fontSize) {
				$item.css('font-size', font.fontSize);
			}
			if (font.fontFamily) {
				$item.css('font-family', font.fontFamily);
			}
			if (font.fontWeight) {
				$item.css('font-weight', font.fontWeight);
			}
			if (font.fontstyle) {
				$item.css('font-style', font.fontStyle)
			}
        },

        isValidate: function (cValue) {
            var validate = FR.EstateRadioGroup.superclass.isValidate.apply(this, arguments);
            if (validate === false) {
                if (this.oriValue) {
                    this._dealValueWithEvents(this.oriValue);
                }
            }
            return validate;
        }
    });
    $.shortcut('estateradiogroup', FR.EstateRadioGroup);

    /**
     * 地产行业通用单选按钮
     *
     *     @example
     *     var editor = new FR.EstateRadioButton({
 *          renderEl : 'body',
 *          text : "文本",
 *          selected : true,
 *          disabled : false
 *     });
     *
     * @class FR.EstateRadioButton
     * @extends FR.ToggleButton
     */
    FR.EstateRadioButton = FR.extend(FR.ToggleButton, /**@class FR.EstateRadioButton*/{
        /**
         * @property {String} selected_class 单选按钮处于选中状态时的样式表类
         */
        selected_class : 'fr-estate-radio-radioon',

        /**
         * @property {String} unselected_class 单选按钮处于未选中状态时的样式表类
         */
        unselected_class : 'fr-estate-radio-radiooff',

        dealWithFont : function(font, $item){
            if (font.fontColor) {
                $item.css('color', font.fontColor);
            }
            if (font.fontSize) {
                $item.css('font-size', font.fontSize);
            }
            if (font.fontFamily) {
                $item.css('font-family', font.fontFamily);
            }
            if (font.fontWeight) {
                $item.css('font-weight', font.fontWeight);
            } else {
                $item.css('font-weight', 'normal');
            }
            if (font.fontstyle) {
                $item.css('font-style', font.fontStyle)
            } else {
                $item.css('font-style', 'normal')
            }
        },

        _init : function() {
            FR.EstateRadioButton.superclass._init.apply(this, arguments);
        },

        initFormBtn : function() {
            var self = this;
            var defaultFont = self.options.defaultFont;
            var selectedFont = self.options.selectedFont;
            $btn = $(self.$btn);
            self.dealWithFont(defaultFont,$btn);
            $btn.click(function(e) {
                $('.fr-estate-radio-radiooff').each(function(i, item) {
                    self.dealWithFont(defaultFont, $(item));
                });
                $('.fr-estate-radio-radioon').each(function(i, item) {
                    self.dealWithFont(selectedFont, $(item));
                });

            });

            if (this.options.sessionID
                && _g(this.options.sessionID).rtype == 'form') {
                var $par = $(this.$btn.parent());
                $par.css("background-color", "white");
                this.$radio = $("<input type='radio'/>").css("display", "none")
                    .attr("name", this.options.name || this.options.widgetName)
                    .appendTo($par);
                this.$radio.attr("value", this.options.fieldValue
                || this.options.value || '');
            }

            return this.$radio;
        }
    });
    $.shortcut("estateradio", FR.EstateRadioButton);
})(jQuery);