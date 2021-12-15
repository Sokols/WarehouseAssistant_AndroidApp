package pl.sokols.warehouseassistant.ui.main.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.AddEditItemDialogBinding

class ItemAddEditDialog(
    providedItem: Item?,
    private val listener: (Any) -> Unit
) : DialogFragment() {

    var item: Item = providedItem ?: Item()

    private lateinit var dialogBinding: AddEditItemDialogBinding

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = AddEditItemDialogBinding.inflate(inflater, container, false)
        dialogBinding.item = item
        setComponents()
        return dialogBinding.root
    }

    /**
     * Method which:
     * - validates the inputs,
     * - displays the errors if the inputs are incorrect,
     * - turns on the listener if the inputs are correct.
     */
    private fun setComponents() {
        dialogBinding.applyDialogButton.setOnClickListener {
            if (item.name.trim().isEmpty()) {
                dialogBinding.itemNameTextInputLayout.error = getString(R.string.incorrect_value)
                dialogBinding.itemNameTextInputLayout.isErrorEnabled = true
            } else {
                dialogBinding.itemNameTextInputLayout.isErrorEnabled = false
            }
            if (item.price.toInt() == 0) {
                dialogBinding.itemPriceTextInputLayout.error = getString(R.string.incorrect_value)
                dialogBinding.itemPriceTextInputLayout.isErrorEnabled = true
            } else {
                dialogBinding.itemPriceTextInputLayout.isErrorEnabled = false
            }
            if (item.name.trim().isNotEmpty() && item.price.toInt() != 0) {
                listener(item)
                dismiss()
            }
        }
    }
}
