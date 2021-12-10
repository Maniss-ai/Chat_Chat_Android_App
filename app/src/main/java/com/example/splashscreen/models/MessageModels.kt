package com.example.splashscreen.models

class MessageModels {
    var userId: String = ""
    var message: String = ""
    var senderName: String = ""
    var timeStamp: Long = 0

    constructor(){

    }

    constructor(userId: String, message: String) {
        this.userId = userId
        this.message = message
    }

    constructor(userId: String, message: String, senderName: String) {
        this.userId = userId
        this.message = message
        this.senderName = senderName
    }

    constructor(msgId:String, message:String, timeStamp:Long) {
        this.userId = msgId
        this.message = message
        this.timeStamp = timeStamp
    }

}