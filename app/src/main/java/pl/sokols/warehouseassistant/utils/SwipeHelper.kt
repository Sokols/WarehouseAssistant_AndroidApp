package pl.sokols.warehouseassistant.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Helper class created to prepare recyclerviews' items swipe.
 */
abstract class SwipeHelper(
    direction: Int
) : ItemTouchHelper.SimpleCallback(0, direction) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}