package com.mrcookies.simplerunning.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id : String,
    val name: String,
    val age: Int,
    val height: Int,
    val weight: Float,
    // 0 = Male, 1 = Female
    val sex: String
)
