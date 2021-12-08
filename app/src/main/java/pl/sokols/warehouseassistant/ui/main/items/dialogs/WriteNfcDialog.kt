package pl.sokols.warehouseassistant.ui.main.items.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.databinding.AddEditItemDialogBinding
import pl.sokols.warehouseassistant.databinding.WriteNfcDialogBinding
import pl.sokols.warehouseassistant.utils.OnItemClickListener

class WriteNfcDialog() : DialogFragment() {

    private lateinit var dialogBinding: WriteNfcDialogBinding

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = WriteNfcDialogBinding.inflate(inflater, container, false)
        return dialogBinding.root
    }
}
