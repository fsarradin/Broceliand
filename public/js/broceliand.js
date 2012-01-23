
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
        + "<button class='btn large secondary' onclick='priceModalDel(" + sellerId + ")'>Del</button>"
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
