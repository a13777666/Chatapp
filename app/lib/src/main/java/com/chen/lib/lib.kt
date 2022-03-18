package com.chen.lib

fun main() {
    val r1 = Regex("^[a-zA-Z0-9]+$" )
    println(r1.matches("789"))
}