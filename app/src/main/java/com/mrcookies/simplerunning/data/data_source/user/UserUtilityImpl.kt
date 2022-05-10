package com.mrcookies.simplerunning.data.data_source.user

import kotlin.math.roundToInt

class UserUtilityImpl : UserUtility{

    override fun isHeightInRange(height : Int) : Boolean{
        return height in 140..220
    }

    override fun isWeightInRange(weight : Float) : Boolean{
        return weight.roundToInt() in 40..150
    }

    override fun isAgeInRange(age : Int) : Boolean{
        return age in 14..80
    }
}