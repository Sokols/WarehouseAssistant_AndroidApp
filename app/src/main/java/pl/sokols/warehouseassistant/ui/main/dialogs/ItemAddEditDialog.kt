package pl.sokols.warehouseassistant.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.databinding.DialogAddEditItemBinding

class ItemAddEditDialog(
    providedItem: CountedItem?,
    private val onApplyClick: (CountedItem) -> Unit
) : DialogFragment() {

    private var item: CountedItem = providedItem ?: CountedItem()
    private lateinit var dialogBinding: DialogAddEditItemBinding

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = DialogAddEditItemBinding.inflate(inflater, container, false)
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
        val nameValid = item.name.trim().isNotEmpty()
        val priceValid = item.price.toInt() > 0
        dialogBinding.apply {
            itemNameTextInputLayout.apply {
                if (!nameValid) {
                    error = getString(R.string.incorrect_value)
                    isErrorEnabled = true
                } else {
                    isErrorEnabled = false
                }
            }

            itemPriceTextInputLayout.apply {
                if (!priceValid) {
                    error = getString(R.string.incorrect_value)
                    isErrorEnabled = true
                } else {
                    isErrorEnabled = false
                }
            }

            if (nameValid && priceValid) {
                onApplyClick(this@ItemAddEditDialog.item)
                dismiss()
            }
        }
    }

    //endregion
}
