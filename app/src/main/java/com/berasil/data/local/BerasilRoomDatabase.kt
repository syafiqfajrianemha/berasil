package com.berasil.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berasil.data.local.entity.ResultDetection

@Database(entities = [ResultDetection::class], version = 1, exportSchema = false)
abstract class BerasilRoomDatabase : RoomDatabase() {

    abstract fun berasilDao(): BerasilDao

    companion object {
        @Volatile
        private var INSTANCE: BerasilRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): BerasilRoomDatabase {
            if (INSTANCE == null) {
                synchronized(BerasilRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BerasilRoomDatabase::class.java,
                        "berasil_database"
                    ).build()
                }
            }
            return INSTANCE as BerasilRoomDatabase
        }
    }
}