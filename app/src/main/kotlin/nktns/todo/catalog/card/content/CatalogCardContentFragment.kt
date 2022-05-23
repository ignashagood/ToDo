package nktns.todo.catalog.card.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.R
import nktns.todo.databinding.CatalogCardContentFragmentBinding
import nktns.todo.main.MainFragment
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CatalogCardContentFragment : Fragment() {

    companion object {
        private const val CATALOG_ID = "catalog_id"
        fun newInstance(catalogId: Int): CatalogCardContentFragment {
            return CatalogCardContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(CatalogCardContentFragment.CATALOG_ID, catalogId)
                }
            }
        }
    }

    private val viewModel: CatalogCardContentVM by viewModel {
        parametersOf(requireArguments().getInt(CATALOG_ID))
    }
    private var binding: CatalogCardContentFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CatalogCardContentFragmentBinding.inflate(inflater, container, false).run {
        binding = this
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, MainFragment())
                .addToBackStack(null)
                .commit()
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
        viewModel.catalogName.observe(viewLifecycleOwner) { catalogName ->
            binding?.catalogName?.text = catalogName
        }
// TODO
    }
}
