package nktns.todo.catalog.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import nktns.todo.R
import nktns.todo.catalog.card.CatalogCardFragment
import nktns.todo.catalog.editor.CatalogEditorFragment
import nktns.todo.catalog.editor.CatalogEditorMode
import nktns.todo.data.database.subset.CatalogWithCounts
import nktns.todo.databinding.FragmentCatalogListBinding
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SHOW_CATALOG_CREATOR = "show_catalog_creator"

class CatalogListFragment : Fragment(), CatalogListAdapter.OnItemClickListener {

    private val viewModel by viewModel<CatalogListVM>()
    private val adapter: CatalogListAdapter by lazy {
        CatalogListAdapter(getKoin().get(), this)
    }
    private var binding: FragmentCatalogListBinding? = null
    private var contentStateApplied: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentCatalogListBinding.inflate(inflater, container, false).run {
            binding = this
            recyclerViewCatalogs.adapter = adapter
            addButton.setOnClickListener {
                CatalogEditorFragment.newInstance(CatalogEditorMode.Create)
                    .show(childFragmentManager, SHOW_CATALOG_CREATOR)
            }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    is CatalogListState.InitialLoading -> {}
                    is CatalogListState.Content -> {
                        applyState(it)
                        if (it.catalogList.isEmpty()) {
                            binding?.apply {
                                recyclerViewCatalogs.isVisible = false
                                zaglushka.isVisible = true
                            }
                        } else {
                            binding?.apply {
                                recyclerViewCatalogs.isVisible = true
                                zaglushka.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onItemClick(catalog: CatalogWithCounts) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, CatalogCardFragment.newInstance(catalog.catalog))
            .addToBackStack(null)
            .commit()
    }

    private fun applyState(state: CatalogListState.Content) {
        if (contentStateApplied) {
            adapter.updateList(state.catalogList, state.diffResult)
        } else {
            adapter.initList(state.catalogList)
            contentStateApplied = true
        }
    }
}
