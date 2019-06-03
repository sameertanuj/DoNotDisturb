package com.samoana.donotdisturb.di

import com.samoana.donotdisturb.ui.EventAdapter
import dagger.Module
import dagger.Provides

@Module
class ViewEventsFragmentModule {
    @Provides
    fun provideEventAdapter() = EventAdapter()
}