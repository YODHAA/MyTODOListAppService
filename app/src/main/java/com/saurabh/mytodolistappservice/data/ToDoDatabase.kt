package com.saurabh.mytodolistappservice.data

import android.content.Context
import androidx.room.*


@Database(entities = [ToDoData::class],version = 1, exportSchema = true)
@TypeConverters(Converter::class)
abstract class ToDoDatabase  : RoomDatabase(){

    abstract fun toDoDao():ToDoDao


    companion object {

       @Volatile
        private var INSTANCE : ToDoDatabase? =null

        fun getDatabase(context: Context) : ToDoDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!= null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}