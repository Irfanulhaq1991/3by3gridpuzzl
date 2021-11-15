package com.irfan.gridpuzzl

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity(), Observer<PuzzleState>, ItemLayoutManger {
    private var isGameCompleted: Boolean = false
    lateinit var recyclerView: RecyclerView
    private val viewModel: PuzzleViewModel by viewModels {
        val grid = ThreeXThreeGrid()
        PuzzleViewModelFactory(grid)
    }

    private val dataAdapter: GridAdaptor by lazy {
        GridAdaptor(this)
    }

    private val dragAndDropItem: DragAndDropItem by lazy {
        DragAndDropItem(viewModel)
    }
    private val touchHelper: ItemTouchHelper by lazy {
        ItemTouchHelper(dragAndDropItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gridRecyclerView.setHasFixedSize(true)
        gridRecyclerView.adapter = dataAdapter
        recyclerView = gridRecyclerView
        touchHelper.attachToRecyclerView(gridRecyclerView)

        // observe change from view model
        viewModel.eventEmitter.observe(this, this)

    }


    override fun onChanged(t: PuzzleState?) {
        when (t) {
            is PuzzleState.Initialised -> {
                val data = (t as PuzzleState.Initialised).puzzleGrid
                dataAdapter.setData(data)
            }
            is PuzzleState.Completed -> {

                val movementData = t.puzzleMov
                val tileFirst = movementData!!.first
                val tileSecond = movementData.second
                moveTile(tileFirst, tileSecond)
                touchHelper.attachToRecyclerView(null)
               isGameCompleted = true
                showCompletionDialog(t.message)

            }
            is PuzzleState.Moved -> {
                val isMoved = t.isMoved && t.puzzleMov != null

                if (!isMoved) return


                val movementData = t.puzzleMov
                val tileFirst = movementData!!.first
                val tileSecond = movementData.second
                moveTile(tileFirst, tileSecond)
            }
            else -> {//illegal state}
            }

        }
    }



    override fun getLayoutId(position: Int): Int {
        return R.layout.grid_cell
    }


    private fun moveTile(newPositionAData: Pair<Int, Int>, newPositionBData: Pair<Int, Int>) {
        with(dataAdapter) {
//            val temp: Int = get(oldPos)?:return
//            val newItem = get(newPos)?:return
            set(newPositionAData.first, newPositionAData.second)
            set(newPositionBData.first, newPositionBData.second)
            notifyItemChanged(newPositionAData.first)
            notifyItemChanged(newPositionBData.first)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        val tileNumber = dataAdapter.get(position) ?: return
        val tileId = resources.getIdentifier("img_$tileNumber", "drawable", packageName)
        val tile = ContextCompat.getDrawable(this, tileId)
        val width = recyclerView.width.toFloat() / 3
        val height = recyclerView.height.toFloat() / 3

        val tileImageView = (holder as GridViewHolder).view.findViewById<ImageView>(R.id.imgvTile)
       tileImageView.layoutParams.width = width.toInt()
       tileImageView.layoutParams.height = height.toInt()

        tileImageView.setImageDrawable(tile)
        //todo: move to stop touch listening
        holder.view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !isGameCompleted) {
                touchHelper.startDrag(holder)
                true
            } else {
                false
            }
        }

    }

    private fun showCompletionDialog(message: String) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle("Puzzle")
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }
}