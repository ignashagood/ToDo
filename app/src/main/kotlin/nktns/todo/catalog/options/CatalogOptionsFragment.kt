package nktns.todo.catalog.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nktns.todo.R
import nktns.todo.catalog.editor.CatalogEditorFragment
import nktns.todo.catalog.editor.CatalogEditorMode
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.databinding.FragmentCatalogOptionsBinding

const val SHOW_CATALOG_CHANGER_TAG = "show_catalog_changer_tag"

class CatalogOptionsFragment : BottomSheetDialogFragment() {

    companion object {
        private const val CATALOG = "catalog"
        fun newInstance(catalog: CatalogEntity): CatalogOptionsFragment {
            return CatalogOptionsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATALOG, catalog)
                }
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private var binding: FragmentCatalogOptionsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentCatalogOptionsBinding.inflate(inflater, container, false).run {
            binding = this
            changeCatalogBtn.setOnClickListener {
                requireArguments().getParcelable<CatalogEntity>(CATALOG)?.let {
                    CatalogEditorFragment.newInstance(CatalogEditorMode.View(it))
                        .show(parentFragmentManager, SHOW_CATALOG_CHANGER_TAG)
                }
                dismiss()
            }
            root
        }
}
