package nktns.todo.catalog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import nktns.todo.R
import nktns.todo.catalog.options.CatalogOptionsFragment
import nktns.todo.databinding.FragmentCatalogCardBinding
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val SHOW_OPTIONS_TAG = "show_options_tag"

class CatalogCardFragment : Fragment() {

    companion object {
        private const val CATALOG_ID = "catalog_id"
        fun newInstance(catalogId: Int): CatalogCardFragment {
            return CatalogCardFragment().apply {
                arguments = Bundle().apply {
                    putInt(CATALOG_ID, catalogId)
                }
            }
        }
    }

    private val viewModel: CatalogCardVM by viewModel {
        parametersOf(requireArguments().getInt(CATALOG_ID))
    }
    private var binding: FragmentCatalogCardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCatalogCardBinding.inflate(inflater, container, false).run {
        binding = this
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        optionsBtn.setOnClickListener {
            CatalogOptionsFragment.newInstance(requireArguments().getInt(CATALOG_ID))
                .show(childFragmentManager, SHOW_OPTIONS_TAG)
        }
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.task_list_fragment_container,
                    TaskListFragment.newInstance(TaskListMode.Catalog(requireArguments().getInt(CATALOG_ID)))
                )
                .addToBackStack(null)
                .commit()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.catalogName.collect { catalogName ->
                binding?.catalogName?.text = catalogName
            }
        }
    }
}
