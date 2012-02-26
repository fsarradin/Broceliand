
jQuery.fn.exists = function() { return this.length > 0; }

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
        'None': function(policy) { return appendPolicyRow(policy, ""); },
        'Fixed': function(policy) { return appendPolicyRow(policy, "<label>Price</label><input type='text'>"); },
        'Proportional': function(policy) { return appendPolicyRow(policy, "<label>Rate</label><input type='text'>"); }
    });
}

function appendPolicyRow(policy, parameters) {
    $("#policy-table").append("<tr>"
        + "<td>" + policy + "</td>"
        + "<td>" + parameters + "</td>"
        + "<td><input type='text' class='span1'/></td>"
        + "<td><a href='#' class='btn btn-danger'><i class='icon-trash icon-white'></i> Delete</a></td>"
        + "</tr>");
}
