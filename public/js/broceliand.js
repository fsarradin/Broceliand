
jQuery.fn.exists = function() { return this.length > 0; }

var IdGenerator = function() {
    var id = 0;
    this.newId = function() {
        return id++;
    };
};

var sessionIdGenerator = new IdGenerator();

// add-modify-book.stg

function priceModalDel(sellerId)  {
    $('#price-table #seller-' + sellerId).remove();
}

function appendPrice(sellerId, price, sellers) {
    var seller = sellers[sellerId];
    $('#price-table').append(
        "<tr id='seller-" + sellerId + "'>"
        + "<td class='price'><input type='text' name='seller-price-" + sellerId + "' value='" + price + "' class='span2' />&nbsp;&euro;</td>"
        + "<td>" + seller + "</td>"
        + "<td>"
        + "<button class='btn btn-danger' onclick='priceModalDel(" + sellerId + ")'><i class='icon-trash icon-white'></i> Del</button>"
        + "</td>"
        + "</tr>"
    );
}

function priceModalSet(sellers) {
    var price = $('#price').val();
    var sellerId = $('#seller').val();
    var sellerSelector = $('#price-table #seller-' + sellerId);
    if (!sellerSelector.exists()) {
        appendPrice(sellerId, "", sellers)
    }
}

// add-modify-seller.stg

function shippingChargeModalSet(sellers) {
    var policy = $('#policy').val();
//    var sellerId = $('#seller').val();
//    var sellerSelector = $('#price-table #seller-' + sellerId);
//    if (!sellerSelector.exists()) {
        appendPolicy(policy)
//    }
}

function match(value, obj) {
    return obj[value](value);
}

function appendPolicy(policy) {
    match(policy, {
        'None': function(policy) { return appendPolicyRow(policy, function(id) { return ""; }); },
        'Fixed': function(policy) { return appendPolicyRow(policy, generatePriceInput); },
        'Proportional': function(policy) { return appendPolicyRow(policy, generateRateInput); }
    });
}

function generatePriceInput(id) {
    return "<label>Price</label><input id='shipping-charge-price-" + id + "' type='text'>";
}

function generateRateInput(id) {
    return "<label>Rate</label><input id='shipping-charge-rate-" + id + "' type='text'>";
}

function shippingChargeDel(id) {
    $('#policy-table #' + id).remove();
}

function appendPolicyRow(policy, parameterGenerator) {
    var id = sessionIdGenerator.newId();
    var scId = "shipping-charge-" + id;
    $("#policy-table").append("<tr id='" + scId + "'>"
        + "<td><input id='shipping-charge-policy-" + id + "' type='hidden' value='" + policy + "'>" + policy + "</td>"
        + "<td>" + parameterGenerator(id) + "</td>"
        + "<td><input id='shipping-charge-quantity-" + id + "' type='text' class='span1'/></td>"
        + "<td><a href='#' class='btn btn-danger' onclick=\"shippingChargeDel('" + scId + "')\"><i class='icon-trash icon-white'></i> Delete</a></td>"
        + "</tr>");
}
