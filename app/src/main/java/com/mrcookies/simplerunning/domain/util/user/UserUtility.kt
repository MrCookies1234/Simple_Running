package com.mrcookies.simplerunning.domain.util.user

interface UserUtility {

    fun isHeightInRange(height :Int): Boolean
    fun isWeightInRange(weight : Float): Boolean
    fun isAgeInRange(age: Int): Boolean

}