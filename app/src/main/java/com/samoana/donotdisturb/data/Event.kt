package com.samoana.donotdisturb.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "events")
data class Event(@PrimaryKey(autoGenerate = true) val id: Int,
                 val title: String,
                 val startDateTime: String,
                 val endDateTime: String,
                 val note : String) : Serializable