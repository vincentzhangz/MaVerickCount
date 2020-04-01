package com.vincentzhangz.maverickcount

import java.time.LocalDate
import java.util.*

data class Debt(var lenderId:Int, var borrowerId:Int, var ammount:Int, var deadline:LocalDate, var desciption:String)

object LendBrowse{
    val debt= listOf<Debt>(
        Debt(1,1,100000, LocalDate.parse("2019-03-30"),"asdasd"),
        Debt(1,2,100000,LocalDate.parse("2019-03-30"),"asdasdasd"),
        Debt(1,3,100000,LocalDate.parse("2019-03-30"),"asdasdasdasd")
    )
}