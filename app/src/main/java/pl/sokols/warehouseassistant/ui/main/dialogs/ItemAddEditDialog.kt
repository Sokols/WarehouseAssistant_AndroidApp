package pl.sokols.warehouseassistant.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.AddEditItemDialogBinding

class ItemAddEditDialog(
    providedItem: CountedItem?,
    private val listener: (Any) -> Unit
) : DialogFragment() {

    private var item: CountedItem = providedItem ?: CountedItem()
    private lateinit var dialogBinding: AddEditItemDialogBinding

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = AddEditItemDialogBinding.inflate(inflater, container, false)
        dialogBinding.item = item
        setupButtons()
        return dialogBinding.root
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    //endregion

    //region Helpers

    private fun setupButtons() {
        dialogBinding.applyDialogButton.setOnClickListener { onApplyClicked() }
    }

    private fun onApplyClicked() {
        dialogBinding.apply {
            itemNameTextInputLayout.apply {
                if (this@ItemAddEditDialog.item.name.trim().isEmpty()) {
                    error = getString(R.string.incorrect_value)
                    isErrorEnabled = true
                } else {
                    isErrorEnabled = false
                }
            }

            itemPriceTextInputLayout.apply {
                if (this@ItemAddEditDialog.item.price.toInt() == 0) {
                    error = getString(R.string.incorrect_value)
                    isErrorEnabled = true
                } else {
                    isErrorEnabled = false
                }
            }

            if (this@ItemAddEditDialog.item.name.trim().isNotEmpty() && this@ItemAddEditDialog.item.price.toInt() != 0) {
                listener(this@ItemAddEditDialog.item)
                dismiss()
            }
        }
    }

    //endregion
}
