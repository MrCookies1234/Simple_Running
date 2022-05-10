package com.mrcookies.simplerunning.data.data_source.user

interface UserUtility {

    fun isHeightInRange(height :Int): Boolean
    fun isWeightInRange(weight : Float): Boolean
    fun isAgeInRange(age: Int): Boolean

}