package pl.sokols.warehouseassistant.utils.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.ItemSummaryBinding

class CustomItemCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding: ItemSummaryBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.item_summary,
        this,
        true
    )

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomItemCard).apply {
            binding.apply {
                ivIcon.apply {
                    visibility = View.VISIBLE
                    setImageDrawable(getDrawable(R.styleable.CustomItemCard_itemIcon))
                }
                tvTitle.text = getString(R.styleable.CustomItemCard_itemTitle)
                tvSubtitle.text = getString(R.styleable.CustomItemCard_itemDescription)

                recycle()
            }
        }
    }
}