package com.smach.zapmancer.core.database.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = object : KoinComponent {}.get<Context>()
    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = "scriptside.db",
    )
}

// // shared/src/androidMain/kotlin/Database.android.kt
//
// fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
//    val appContext = context.applicationContext
//    val dbFile = appContext.getDatabasePath("my_room.db")
//    return Room.databaseBuilder<AppDatabase>(
//        context = appContext,
//        name = dbFile.absolutePath
//    )
// }
