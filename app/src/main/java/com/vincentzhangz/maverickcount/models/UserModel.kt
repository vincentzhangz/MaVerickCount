package com.vincentzhangz.maverickcount.models

class User(val uid: String, val name: String) {
    constructor() : this("", "")
}