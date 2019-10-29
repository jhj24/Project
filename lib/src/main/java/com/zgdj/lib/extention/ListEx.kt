package com.zgdj.lib.extention

fun <T> List<T>?.toArrayList(): ArrayList<T> {
    return ArrayList(this.orEmpty())
}