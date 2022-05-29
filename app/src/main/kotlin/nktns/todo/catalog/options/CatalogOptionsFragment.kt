package nktns.todo.catalog.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nktns.todo.catalog.editor.CatalogEditorFragment
import nktns.todo.catalog.editor.CatalogEditorMode
import nktns.todo.databinding.FragmentCatalogOptionsBinding

const val SHOW_CATALOG_CHANGER_TAG = "show_catalog_changer_tag"

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
                CatalogEditorFragment.newInstance(CatalogEditorMode.View(requireArguments().getInt(CATALOG_ID)))
                    .show(childFragmentManager, SHOW_CATALOG_CHANGER_TAG)
            }
            root
        }
}
