package br.edu.scl.ifsp.inery.sharedlist.view

import android.content.Intent
import android.graphics.Paint
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.inery.sharedlist.R
import br.edu.scl.ifsp.inery.sharedlist.adapter.TaskAdapter
import br.edu.scl.ifsp.inery.sharedlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.inery.sharedlist.model.Task
import br.edu.scl.ifsp.inery.sharedlist.adapter.OnTaskClickListenner
import br.edu.scl.ifsp.inery.sharedlist.controller.TaskController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), OnTaskClickListenner{
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(taskList, this)

    }
    private lateinit var carl: ActivityResultLauncher<Intent>

    private val taskController: TaskController by lazy {
        TaskController(this)
    }
    lateinit var updateViewsHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = "Shared List"

        taskController.getTasks()
        amb.tasksRv.layoutManager = LinearLayoutManager(this)
        amb.tasksRv.adapter = taskAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if ( result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra(EXTRA_TASK)
                }
                task?.let {_task ->
                    val position = taskList.indexOfFirst { it.id == task.id}
                    Toast.makeText(this, position.toString(), Toast.LENGTH_LONG).show()
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        Toast.makeText(this, "Task Edited", Toast.LENGTH_LONG).show()
                        taskAdapter.notifyItemChanged(position)
                    } else {
                        if(taskController.countTasks(_task.title) == 0){
                            taskController.insertTask(_task)
                            Toast.makeText(this, "Task inserted", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this, "Task already exists not inserted", Toast.LENGTH_LONG).show()
                        }
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            }
        }
        updateViewsHandler = Handler(Looper.myLooper()!!) {msg ->
            taskController.getTasks()
            true
        }
        updateViewsHandler.sendMessageDelayed(Message(),3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTaskMi -> {
                carl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.signOutMi -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish()
                true
            }
            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task)
        intent.putExtra(EXTRA_VIEW_TASK, true)
        carl.launch(intent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val task = taskList[position]
        if(task.isCompleted) {
            Toast.makeText(this, "Task concluded, can't edit", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task)
        carl.launch(intent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        if(task.isCompleted) {
            Toast.makeText(this, "Task concluded, can't remove", Toast.LENGTH_SHORT).show()
            return
        }
        taskList.removeAt(position)
        taskController.removeTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Task removed", Toast.LENGTH_SHORT).show()
    }

    override fun onDetailMenuItemClick(position: Int) {
        val task = taskList[position]
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task)
        intent.putExtra(EXTRA_VIEW_TASK, true)
        carl.launch(intent)
    }

    override fun onConcludeMenuItemClick(position: Int) {
        val task = taskList[position]
        task.isCompleted = true
        task.userConcluded = FirebaseAuth.getInstance().currentUser?.email.toString()
        taskController.editTask(task)

        val viewHolder = amb.tasksRv.findViewHolderForAdapterPosition(position)
        if (viewHolder is TaskAdapter.TaskViewHolder) {
            viewHolder.titleTv.paintFlags = viewHolder.titleTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        taskAdapter.notifyItemChanged(position)
        Toast.makeText(this, "Task concluded", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val email = FirebaseAuth.getInstance().currentUser?.email
        }
        else {
            Toast.makeText(this, "Não há usuário autenticado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}