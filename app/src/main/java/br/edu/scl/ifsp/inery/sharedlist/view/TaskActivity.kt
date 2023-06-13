package br.edu.scl.ifsp.inery.sharedlist.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.edu.scl.ifsp.inery.sharedlist.R
import br.edu.scl.ifsp.inery.sharedlist.controller.TaskController
import br.edu.scl.ifsp.inery.sharedlist.databinding.ActivityTaskBinding
import br.edu.scl.ifsp.inery.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class TaskActivity : BaseActivity(){
    private val acb : ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)
        supportActionBar?.subtitle = "Task information"

        val receivedTask = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }
        receivedTask?.let{_receivedTask ->
            with(acb){
                with(_receivedTask){
                    titleEt.setText(title)
                    descriptionEt.setText(description)
                    dateCreateEt.setText(dateCreate)
                    datePreviousFinishEt.setText(datePreviousFinish)
                    userEt.setText(user)
                    userCompleteEt.setText(userConcluded)
                }
            }
            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            with(acb){
                titleLabelTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                titleEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                titleEt.isEnabled = !viewTask
                descriptionEt.isEnabled = !viewTask
                dateCreateLabelTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                dateCreateEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                dateCreateEt.isEnabled = !viewTask
                datePreviousFinishEt.isEnabled = !viewTask
                userLabelTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                userEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                userEt.isEnabled = !viewTask
                saveBt.visibility = if(viewTask) View.GONE else View.VISIBLE
                if(receivedTask.isCompleted){
                    userCompleteLabelTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                    userCompleteEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                    userCompleteEt.isEnabled = !viewTask
                }
            }
        }
        acb.saveBt.setOnClickListener{
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val date = Date()
            val dateCreate = sdf.format(date)
            val task: Task = Task(
                id = receivedTask?.id,
                title = acb.titleEt.text.toString(),
                description = acb.descriptionEt.text.toString(),
                dateCreate = dateCreate,
                datePreviousFinish = acb.datePreviousFinishEt.text.toString(),
                user = FirebaseAuth.getInstance().currentUser?.email.toString()
            )

            val resultIntent = intent
            resultIntent.putExtra(EXTRA_TASK, task)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }
}