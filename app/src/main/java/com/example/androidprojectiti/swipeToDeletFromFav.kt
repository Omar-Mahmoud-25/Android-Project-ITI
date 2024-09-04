package com.example.androidprojectiti

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.Adapters.favoriteAdapter

fun swipeToDeleteFromFav(adapter: favoriteAdapter, context: Context, showConfirmationDialog: (() -> Unit) -> Unit): ItemTouchHelper.SimpleCallback {
    return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            adapter.notifyItemChanged(position)
            showConfirmationDialog {
                adapter.removeAt(position, context)
            }
        }
    }
}
