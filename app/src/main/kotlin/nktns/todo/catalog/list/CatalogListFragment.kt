package nktns.todo.catalog.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.catalog.card.bottom.CatalogCardBottomFragment
import nktns.todo.catalog.card.bottom.CatalogCardBottomMode
import nktns.todo.databinding.FragmentCatalogListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatalogListFragment : Fragment() {

    private val viewModel by viewModel<CatalogListVM>()
    private val adapter: CatalogListAdapter by lazy { CatalogListAdapter() }
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
                CatalogCardBottomFragment.newInstance(CatalogCardBottomMode.Create)
                    .show(childFragmentManager, "ShowBottomSheet")
            }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is CatalogListState.InitialLoading -> {}
                is CatalogListState.Content -> applyState(it)
            }
        }
    }

    private fun applyState(state: CatalogListState.Content) {
        if (contentStateApplied) {
            adapter.updateList(state.catalogList, state.diffResult)
        } else {
            // Если состояние ещё ни разу не было применено, значит список мы просто резко отображаем, без анимаций
            adapter.initList(state.catalogList)
            contentStateApplied = true
        }
    }
}
