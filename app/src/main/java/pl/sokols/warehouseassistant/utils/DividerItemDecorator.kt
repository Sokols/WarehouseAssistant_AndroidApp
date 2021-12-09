package pl.sokols.warehouseassistant.utils

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: Bhuvanesh BS (https://stackoverflow.com/a/46216274/15382251)
 *
 * Class created to manage separators in recyclerviews.
 */
class DividerItemDecorator(
    private val mDivider: Drawable
) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft: Int = parent.paddingLeft
        val dividerRight: Int = parent.width - parent.paddingRight

        val childCount: Int = parent.childCount
        for (i in 0..childCount - 2) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }
    }
}