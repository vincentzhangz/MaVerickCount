package com.vincentzhangz.maverickcount.services

class NotificationData(val user:String,val body:String,val title:String,val sented:String){
    constructor():this("","","","")
}

class Sender(val data:NotificationData, val to:String){
    constructor():this(NotificationData("","","",""),"")
}