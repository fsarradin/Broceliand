# Broceliand

## Entity

* User
* BasketElement
* Book
* Seller
* SellerPrice

A book may have multiple prices, according to the seller who supplies it. For
example, for the same book, a seller A may sell it 10.00 € and a seller B may
sell it 11.00 €. Book and Seller entities are linked by the entity
SellerPrice.

         *     *
    Book <-----> Seller
            |
       SellerPrice

A ship rate policy is determined for each Seller.

* Fixed: fixed price for all orders,
* Quantity: price is relative to the quantity of ordered books.

A user will constitutes a basket of books. User and Book entities are linked
by BasketElement entity. From a set of BasketElement owned by the same user,
you can determine a part of the invoice.

         *     *
    User <-----> Book
            |
      BasketElement


         1     *
    User <-----> Connection
