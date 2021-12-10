package com.example.splashscreen.models

class User {
    var fullName: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var phoneNo: String = ""
    var profile: String = "https://image.freepik.com/free-vector/board-template-with-cute-monkey-white-background_1308-44141.jpg"
    var userId: String = ""
    var status: String = ""

    constructor( ) {

    }
    constructor(
        fullName: String,
        username: String,
        password: String,
        email: String,
        phoneNo: String,
    ) {
        this.fullName = fullName
        this.username = username
        this.password = password
        this.email = email
        this.phoneNo = phoneNo
        this.profile
    }

}
