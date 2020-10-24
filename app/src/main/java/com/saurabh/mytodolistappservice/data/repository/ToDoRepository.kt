package com.saurabh.mytodolistappservice.data.repository

import androidx.lifecycle.LiveData
import com.saurabh.mytodolistappservice.data.ToDoDao
import com.saurabh.mytodolistappservice.data.models.ToDoData


class ToDoRepository(private val toDoDao: ToDoDao) {


    val getAllData : LiveData<List<ToDoData>> = toDoDao.getAllData()

    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortbyHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortbyLowPriority()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData = toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData){
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll(){
        toDoDao.deleteAll()
    }

    fun searchDatabase(searchQuery : String) : LiveData<List<ToDoData>> {
        return  toDoDao.searchDatabase(searchQuery)
    }


}