package pl.sokols.warehouseassistant.utils.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.card.MaterialCardView
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.CustomItemCardBinding
import pl.sokols.warehouseassistant.databinding.LayoutEditTextBinding

class CustomItemCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding: CustomItemCardBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.custom_item_card,
        this,
        true
    )

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomItemCard).apply {
            binding.apply {
                itemIcon.setImageDrawable(getDrawable(R.styleable.CustomItemCard_itemIcon))
                itemTitle.text = getString(R.styleable.CustomItemCard_itemTitle)
                itemDescription.text = getString(R.styleable.CustomItemCard_itemDescription)

                recycle()
            }
        }
    }
}