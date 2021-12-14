package pl.sokols.warehouseassistant.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import pl.sokols.warehouseassistant.R

class CustomItemCard(
    context: Context,
    attrs: AttributeSet
) : MaterialCardView(context, attrs) {
    init {
        inflate(context, R.layout.custom_item_card, this)

        val customAttributesStyle = context.obtainStyledAttributes(attrs, R.styleable.CustomItemCard, 0, 0)

        val itemIcon = findViewById<ImageView>(R.id.itemIcon)
        val itemTitle = findViewById<TextView>(R.id.itemTitle)
        val itemDescription = findViewById<TextView>(R.id.itemDescription)

        try {
            itemIcon.setImageDrawable(customAttributesStyle.getDrawable(R.styleable.CustomItemCard_itemIcon))
            itemTitle.text = customAttributesStyle.getString(R.styleable.CustomItemCard_itemTitle)
            itemDescription.text = customAttributesStyle.getString(R.styleable.CustomItemCard_itemDescription)
        } finally {
            customAttributesStyle.recycle()
        }
    }
}