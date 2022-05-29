package nktns.todo.catalog.card.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nktns.todo.catalog.card.bottom.CatalogCardBottomFragment
import nktns.todo.catalog.card.bottom.CatalogCardBottomMode
import nktns.todo.databinding.FragmentCatalogOptionsBinding

class CatalogOptionsFragment : BottomSheetDialogFragment() {

    companion object {
        private const val CATALOG_ID = "catalog_id"
        fun newInstance(catalogId: Int): CatalogOptionsFragment {
            return CatalogOptionsFragment().apply {
                arguments = Bundle().apply {
                    putInt(CatalogOptionsFragment.CATALOG_ID, catalogId)
                }
            }
        }
    }

    private var binding: FragmentCatalogOptionsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentCatalogOptionsBinding.inflate(inflater, container, false).run {
            binding = this
            changeCatalogBtn.setOnClickListener {
                CatalogCardBottomFragment.newInstance(CatalogCardBottomMode.View(requireArguments().getInt(CATALOG_ID)))
                    .show(childFragmentManager, "ShowBottomSheet")
            }
            root
        }
}
