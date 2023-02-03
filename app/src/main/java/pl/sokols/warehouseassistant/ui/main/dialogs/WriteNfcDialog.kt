package pl.sokols.warehouseassistant.ui.main.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.sokols.warehouseassistant.databinding.DialogWriteNfcBinding

class WriteNfcDialog(
    private val onCancelListener: DialogInterface.OnCancelListener
) : DialogFragment() {

    private lateinit var dialogBinding: DialogWriteNfcBinding

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = DialogWriteNfcBinding.inflate(inflater, container, false)
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            setOnCancelListener(onCancelListener)
        }
        return dialogBinding.root
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    //endregion
}
