package nktns.todo.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import nktns.todo.base.AppEvent
import nktns.todo.base.EventBus
import nktns.todo.databinding.ArchiveConfirmDialogBinding
import org.koin.java.KoinJavaComponent

class ArchiveConfirmDeleteFragment : DialogFragment() {

    private var binding: ArchiveConfirmDialogBinding? = null

    private val eventBus: EventBus = KoinJavaComponent.getKoin().get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ArchiveConfirmDialogBinding.inflate(inflater, container, false).run {
            binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            buttonConfirm.setOnClickListener {
                eventBus.emitEvent(AppEvent.ClearArchive)
                this@ArchiveConfirmDeleteFragment.dismiss()
            }
            buttonCancel.setOnClickListener {
                this@ArchiveConfirmDeleteFragment.dismiss()
            }
        }
    }
}
