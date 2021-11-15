package com.irfan.gridpuzzl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Observer<PuzzleState>, ItemLayoutManger {

    private val viewModel: PuzzleViewModel by viewModels {
        val grid = ThreeXThreeGrid()
        PuzzleViewModelFactory(grid)
    }

    private val dataAdapter: GridAdaptor by lazy {
        GridAdaptor(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gridRecyclerView.adapter = dataAdapter
        val dragAndDropItem = DragAndDropItem(dataAdapter,viewModel)
        dragAndDropItem.attachRecyclerView(gridRecyclerView)

        // observe change from view model
        viewModel.eventEmitter.observe(this, this)

    }


    override fun onChanged(t: PuzzleState?) {
        when (t) {
           is PuzzleState.Initialised ->{
               val data = (t as PuzzleState.Initialised).puzzleGrid
               dataAdapter.setData(data)
            }
            is PuzzleState.Completed -> {
                // show alert
            }
            is PuzzleState.Moved -> {
                val isMoved = (t as PuzzleState.Moved).puzzleMov
                val data = (t as PuzzleState.Moved).puzzleMov
            }
            else -> {//illegal state}
            }

        }
    }

    override fun getLayoutId(position: Int): Int {
        return R.layout.grid_cell
    }


    private fun moveItem(oldPos: Int, newPos: Int) {
        with(dataAdapter) {
            val temp: Int = get(oldPos)?:return
            val newItem = get(newPos)?:return
            set(oldPos, newItem)
            set(newPos, temp)
            notifyItemChanged(oldPos)
            notifyItemChanged(newPos)
        }
    }
    override fun bindView(holder: RecyclerView.ViewHolder, position: Int) {
        val tileNumber = dataAdapter.get(position)?:return
        val tileId = resources.getIdentifier("img_$tileNumber", "drawable", packageName)
        val tile = ContextCompat.getDrawable(this, tileId)
        val tileImageView = (holder as GridViewHolder).view.findViewById<ImageView>(R.id.imgvTile)
        tileImageView.setImageDrawable(tile)
    }
}