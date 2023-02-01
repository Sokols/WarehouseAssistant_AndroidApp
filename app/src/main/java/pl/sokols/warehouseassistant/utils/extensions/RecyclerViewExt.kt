package pl.sokols.warehouseassistant.utils.extensions

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.utils.DividerItemDecorator

fun RecyclerView.setupDivider() {
    addItemDecoration(
        DividerItemDecorator(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.divider,
                null
            )!!
        )
    )
}