package com.samoana.donotdisturb.di

import com.samoana.donotdisturb.ui.AddEventFragment
import com.samoana.donotdisturb.ui.ViewEventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsBindingModule {
    @ContributesAndroidInjector
    abstract fun addEventFragment():AddEventFragment

    @ContributesAndroidInjector(modules = [ViewEventsFragmentModule::class])
    abstract fun viewEventsFragment():ViewEventsFragment
}