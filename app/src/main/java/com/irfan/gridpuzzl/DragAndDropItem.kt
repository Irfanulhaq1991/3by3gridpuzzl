package com.irfan.gridpuzzl

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicReference


class DragAndDropItem(private val viewModel: PuzzleViewModel) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or
            ItemTouchHelper.DOWN or
            ItemTouchHelper.LEFT or
            ItemTouchHelper.RIGHT, 0
) {
    private val oldPosition = AtomicReference<Int>()
    private val newPosition = AtomicReference<Int>()


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        oldPosition.set(viewHolder.adapterPosition)
        newPosition.set(target.adapterPosition)
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // swipe is disabled
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewModel.swap(oldPosition.get(), newPosition.get())
    }


}