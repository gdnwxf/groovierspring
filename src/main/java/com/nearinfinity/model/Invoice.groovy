package com.nearinfinity.model

class Invoice {

    String orderNumber
    Date orderDate
    List<LineItem> lineItems

    def getTotal() {
        lineItems.price.sum()
    }

}
