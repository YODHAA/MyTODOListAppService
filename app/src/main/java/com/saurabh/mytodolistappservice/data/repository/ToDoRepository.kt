package com.saurabh.mytodolistappservice.data.repository

import androidx.lifecycle.LiveData
import com.saurabh.mytodolistappservice.data.ToDoDao
import com.saurabh.mytodolistappservice.data.models.ToDoData


class ToDoRepository(private val toDoDao: ToDoDao) {


    val getAllData : LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData = toDoData)
    }


}