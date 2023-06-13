package br.edu.scl.ifsp.inery.sharedlist.controller

import br.edu.scl.ifsp.inery.sharedlist.model.Task
import br.edu.scl.ifsp.inery.sharedlist.model.TaskDao
import br.edu.scl.ifsp.inery.sharedlist.model.TaskDaoDbFb
import br.edu.scl.ifsp.inery.sharedlist.view.MainActivity

class TaskController(private val mainActivity: MainActivity) {
    private val taskDaoImpl: TaskDao = TaskDaoDbFb()

    fun insertTask(task: Task) {
        Thread {
            taskDaoImpl.createTask(task)
        }.start()
    }
    fun getTask(id: Int) = taskDaoImpl.retrieveTask(id)
    fun getTasks() {
        Thread {
            val list = taskDaoImpl.retrieveTasks()
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(list)
            }
        }.start()
    }
    fun editTask(task: Task) {
        Thread {
            taskDaoImpl.updateTask(task)
        }.start()
    }
    fun removeTask(task: Task) {
        Thread {
            taskDaoImpl.deleteTask(task)
        }.start()
    }
    fun countTasks(title: String) = taskDaoImpl.countTasks(title)



}