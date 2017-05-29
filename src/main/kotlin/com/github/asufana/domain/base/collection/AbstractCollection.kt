package com.github.asufana.domain.base.collection


abstract class AbstractCollection<out T>(val list: List<T>) {

    fun count(): Int {
        return list.count()
    }

    fun get(index: Int): T {
        return list[index]
    }

}
