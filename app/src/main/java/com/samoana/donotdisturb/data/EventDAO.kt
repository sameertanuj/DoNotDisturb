package com.samoana.donotdisturb.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EventDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event): Long

    @Query("SELECT * from events ORDER BY id ASC")
    fun getAllEvents(): LiveData<List<Event>>

    @Delete
    fun delete(event: Event)

}