package br.edu.scl.ifsp.inery.sharedlist.model

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class TaskDaoDbFb: TaskDao {
    private val TASK_LIST_ROOT_NODE = "taskList"
    private val taskRtDbFbReference = FirebaseDatabase.getInstance().getReference(TASK_LIST_ROOT_NODE)

    private val taskList: MutableList<Task> = mutableListOf()
    init{
        taskRtDbFbReference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task : Task ? = snapshot.getValue<Task>()
                task?.let { _task ->
                    if (!taskList.any { _task.title == it.title }) {
                        taskList.add(_task)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
               val task: Task? = snapshot.getValue<Task>()
                task?.let { _task ->
                    taskList[taskList.indexOfFirst { _task.title == it.title }] = _task
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let { _task ->
                    taskList.remove(_task)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        taskRtDbFbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskHashMap = snapshot.getValue<HashMap<String, Task>>()
                taskList.clear()
                taskHashMap?.values?.forEach {
                    taskList.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }

        })
    }

    override fun createTask(task: Task) {
        createOrUpdateTask(task)
    }

    override fun retrieveTask(id: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun retrieveTasks(): MutableList<Task> {
        return taskList;
    }

    override fun updateTask(task: Task): Int {
        createOrUpdateTask(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        taskRtDbFbReference.child(task.title).removeValue()
        return 1
    }

    override fun countTasks(title: String): Int {
        return taskList.count { it.title == title }
    }
    private fun createOrUpdateTask(task: Task) {
        taskRtDbFbReference.child(task.title).setValue(task)
    }
}