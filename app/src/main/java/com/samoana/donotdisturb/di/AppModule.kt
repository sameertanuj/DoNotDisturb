package com.samoana.donotdisturb.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

import com.samoana.donotdisturb.data.EventDatabase


@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    fun provideEventDataBase(app: Application) =
        Room.databaseBuilder(app, EventDatabase::class.java, "notes_database").build()
    @Provides
    @Singleton
    fun provideEventDAO(eventDatabase: EventDatabase) =
        eventDatabase.getEventDAO()
}