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

define(['Backbone', 'pricing-strategy'], function(Backbone, PricingStrategy) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            'seller/add-pricing': 'add_pricing'
        },

        add_pricing: function() {
            require(['pricing-details'], function(PricingDetailsView) {
                new PricingDetailsView({model:new PricingStrategy()}).render();
            });
        }
    });

    var initialize = function() {
        window.app_router = new AppRouter;
//        Backbone.history.start();
    };

    return {
        initialize: initialize
    };
});
