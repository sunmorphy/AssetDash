package com.andikas.assetdash.di

import android.app.Application
import androidx.room.Room
import com.andikas.assetdash.data.local.AssetDatabase
import com.andikas.assetdash.data.local.dao.AssetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAssetDatabase(
        app: Application
    ): AssetDatabase {
        return Room.databaseBuilder(
            app,
            AssetDatabase::class.java,
            "asset_dash.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideAssetDao(db: AssetDatabase): AssetDao {
        return db.assetDao()
    }
}