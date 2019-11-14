package com.example.kmlpbl


import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {

    var product_name: String="afs"
    var price: Int = 0
    var explan: String="afdsf"

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(product_name: String, price: Int, explan: String) {
        this.product_name = product_name
        this.price = price
        this.explan = explan
    }

}