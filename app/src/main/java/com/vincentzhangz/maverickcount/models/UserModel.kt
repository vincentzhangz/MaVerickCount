package com.vincentzhangz.maverickcount.models

class User(val uid: String, val name: String, val friends:ArrayList<Friend>) {
    constructor() : this("", "", ArrayList())
}

class Friend(val uid:String, val name:String){
    constructor():this("","")
}