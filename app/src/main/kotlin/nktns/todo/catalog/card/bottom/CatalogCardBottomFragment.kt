package nktns.todo.catalog.card.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nktns.todo.databinding.CatalogCardBottomFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CatalogCardBottomFragment : BottomSheetDialogFragment() {

    companion object {
        private const val CATALOG_CARD_MODE_KEY = "catalog_card_mode_key"
        fun newInstance(catalogCardMode: CatalogCardBottomMode): CatalogCardBottomFragment {
            return CatalogCardBottomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATALOG_CARD_MODE_KEY, catalogCardMode)
                }
            }
        }
    }

    private val viewModel: CatalogCardBottomVM by viewModel {
        parametersOf(requireArguments().getParcelable(CatalogCardBottomFragment.CATALOG_CARD_MODE_KEY))
    }

    private var binding: CatalogCardBottomFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.action.observe(this) {
            if (it != null) {
                when (it) {
                    CatalogCardBottomAction.DISMISS -> dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CatalogCardBottomFragmentBinding.inflate(inflater, container, false).run {
        binding = this
        saveButton.setOnClickListener { viewModel.onSaveButtonClicked() }
        inputNameCatalog.addTextChangedListener { viewModel.onCatalogNameChanged(it?.toString().orEmpty()) }
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CatalogCardBottomState.Content -> binding?.run {
                    if (inputNameCatalog.text.toString() != state.name) {
                        inputNameCatalog.setText(state.name)
                    }
                }
            }
        }
    }
}
