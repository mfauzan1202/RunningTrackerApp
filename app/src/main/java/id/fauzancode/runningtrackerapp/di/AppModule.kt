package id.fauzancode.runningtrackerapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.fauzancode.runningtrackerapp.db.RunDatabase
import id.fauzancode.runningtrackerapp.utils.Constants.RUNNING_DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRunDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideRunDao(db: RunDatabase) = db.getRunDao()
}