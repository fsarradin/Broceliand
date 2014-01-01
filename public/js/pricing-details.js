/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

define(['jquery', 'Underscore', 'Backbone', 'text!/resources/html/'],function($, _, Backbone, template) {
    var PricingDetailsView = Backbone.View.extend({
        events: {

        },

        initialize: function() {
            this.slot = this.option.slot;
            this.model.on("reset sync change", this.refreshView, this);
        },

        refreshView: function() {
            var json = this.model.toJSON();
            json.currentLine = this.model.currentLine;
            this.$el.html(this.renderTemplate(json));
            this.delegateEvents();
        },

        renderTemplate: function(json) {
            var $pricingElement = $(_.template(template, json));
            $pricingElement.find('[name*="line.pricingType"]')[0].value = this.model.currentLine.pricingType;
            return $pricingElement;
        }
    });

    return PricingDetailsView;
});