package nktns.todo.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nktns.todo.data.TaskRepository
import nktns.todo.data.database.entity.TaskEntity

class ViewModelList(private val repository: TaskRepository, application: Application) :
    AndroidViewModel(application),
    ListFragment.TaskActionHandler {

    private var currentTasks: List<TaskEntity> = emptyList()
    val allTasks: LiveData<Pair<List<TaskEntity>, DiffUtil.DiffResult>>

    init {

        allTasks = Transformations.map(repository.sortedTasks) { newTasks ->
            val diffResult: DiffUtil.DiffResult =
                DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int = currentTasks.size

                    override fun getNewListSize(): Int = newTasks.size

                    override fun areItemsTheSame(
                        oldPosition: Int,
                        newPosition: Int
                    ): Boolean {
                        return currentTasks[oldPosition].itemId == newTasks[newPosition].itemId
                    }

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Boolean {
                        return currentTasks[oldItemPosition] == newTasks[newItemPosition]
                    }
                })
            currentTasks = newTasks
            newTasks to diffResult
        }
    }

    override fun onTaskDeleteClick(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(task)
        }
    }

    override fun onTaskCompleted(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(task)
        }
    }
}
