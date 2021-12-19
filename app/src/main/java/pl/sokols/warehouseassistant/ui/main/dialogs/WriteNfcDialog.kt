package pl.sokols.warehouseassistant.ui.main.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.databinding.WriteNfcDialogBinding

class WriteNfcDialog(
    private val onCancelListener: DialogInterface.OnCancelListener
    ) : DialogFragment() {

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
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnCancelListener(onCancelListener)
        return dialogBinding.root
    }
}
