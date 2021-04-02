package ng.mint.ocrscanner.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import ng.mint.ocrscanner.views.common.MessageDialogManager
import ng.mint.ocrscanner.views.common.ProgressDialogManager

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {

    @Provides
    fun providesProgressDialogManager(@ActivityContext context: Context) =
        ProgressDialogManager(context)

    @Provides
    fun providesMessageDialogManager(@ActivityContext context: Context) =
        MessageDialogManager(context)
}