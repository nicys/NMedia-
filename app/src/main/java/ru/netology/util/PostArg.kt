package ru.netology.util

import android.os.Bundle
import ru.netology.dto.Post
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PostArg: ReadWriteProperty<Bundle, Post?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Post?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Post? {
        return thisRef.getParcelable(property.name)
    }

}