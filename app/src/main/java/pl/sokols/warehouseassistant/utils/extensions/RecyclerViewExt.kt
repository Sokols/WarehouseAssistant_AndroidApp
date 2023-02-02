package pl.sokols.warehouseassistant.utils.extensions

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.utils.DividerItemDecorator
import pl.sokols.warehouseassistant.utils.SwipeHelper

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

fun RecyclerView.addSwipe(onSwiped: (index: Int) -> Unit) {
    ItemTouchHelper(object : SwipeHelper(this.context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSwiped(viewHolder.layoutPosition)
        }
    }).attachToRecyclerView(this)
}