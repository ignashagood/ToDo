package nktns.todo.catalog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.R
import nktns.todo.catalog.options.CatalogOptionsFragment
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.databinding.FragmentCatalogCardBinding
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode

private const val SHOW_OPTIONS_TAG = "show_options_tag"

class CatalogCardFragment : Fragment() {

    companion object {
        private const val CATALOG = "catalog"
        fun newInstance(catalog: CatalogEntity): CatalogCardFragment {
            return CatalogCardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATALOG, catalog)
                }
            }
        }
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
            requireArguments().getParcelable<CatalogEntity>(CATALOG)?.let {
                CatalogOptionsFragment.newInstance(it).show(childFragmentManager, SHOW_OPTIONS_TAG)
            }
        }
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            requireArguments().getParcelable<CatalogEntity>(CATALOG)?.let {
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.task_list_fragment_container,
                        TaskListFragment.newInstance(TaskListMode.Catalog(it))
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
        binding?.catalogName?.text = requireArguments().getParcelable<CatalogEntity>(CATALOG)?.name
    }
}
