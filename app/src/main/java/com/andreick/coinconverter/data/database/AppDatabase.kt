package com.andreick.coinconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andreick.coinconverter.data.database.dao.ExchangeDao
import com.andreick.coinconverter.data.model.ExchangeResponseValue

@Database(entities = [ExchangeResponseValue::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDao

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "coin_converter"
            ).fallbackToDestructiveMigration().build()
        }
    }
}