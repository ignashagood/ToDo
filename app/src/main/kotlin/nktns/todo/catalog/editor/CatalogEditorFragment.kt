package nktns.todo.catalog.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import nktns.todo.R
import nktns.todo.databinding.FragmentCatalogEditorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CatalogEditorFragment : BottomSheetDialogFragment() {

    companion object {
        private const val CATALOG_CARD_MODE_KEY = "catalog_card_mode_key"
        fun newInstance(catalogCardMode: CatalogEditorMode): CatalogEditorFragment {
            return CatalogEditorFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATALOG_CARD_MODE_KEY, catalogCardMode)
                }
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private val viewModel: CatalogEditorVM by viewModel {
        parametersOf(requireArguments().getParcelable(CATALOG_CARD_MODE_KEY))
    }

    private var binding: FragmentCatalogEditorBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collect {
                when (it) {
                    CatalogEditorAction.DISMISS -> dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCatalogEditorBinding.inflate(inflater, container, false).run {
        binding = this
        saveButton.setOnClickListener { viewModel.onCompleteButtonClicked() }
        deleteButton.setOnClickListener { viewModel.onDeleteButtonClicked() }
        inputNameCatalog.addTextChangedListener { viewModel.onCatalogNameChanged(it?.toString().orEmpty()) }
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is CatalogEditorState.Content -> binding?.run {
                        if (inputNameCatalog.text.toString() != state.catalogName) {
                            inputNameCatalog.setText(state.catalogName)
                        }
                        catalogCardBottomTitle.text = state.completedActionName
                        deleteButton.isVisible = state.canDelete
                    }
                    is CatalogEditorState.InitialLoading -> Unit
                }
            }
        }
    }
}
