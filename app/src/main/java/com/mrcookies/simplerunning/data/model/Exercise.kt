package com.mrcookies.simplerunning.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (
    var img : Bitmap? = null,
    val timestamp : Long,
    val time : Long,
    val calories : Int,
    // 0 = run, 1 = bike
    val type : Int,
    val distance : Float,
    val avgSpeed : Float
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}