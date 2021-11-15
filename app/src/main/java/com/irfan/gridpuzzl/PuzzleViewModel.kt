package com.irfan.gridpuzzl

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PuzzleViewModel(private val threeXThreeGrid: ThreeXThreeGrid) : ViewModel() {


    private val _eventEmitter = MutableLiveData<PuzzleState>()
    val eventEmitter: LiveData<PuzzleState>
        get() = _eventEmitter

    init {
        _eventEmitter.value = PuzzleState.Initialised(threeXThreeGrid.getUnSolvedGrid())
    }

    fun swap(a: Int, b: Int) {
        var event: PuzzleState
        val isSwappable = threeXThreeGrid.isSwappable(a, b)
        event = if (isSwappable) {
            threeXThreeGrid.swap(a, b)
            val isGameCompleted = threeXThreeGrid.isGameCompleted()
            if (isGameCompleted) PuzzleState.Completed else PuzzleState.Moved(Pair(b, a), true)
        } else
            PuzzleState.Moved(Pair(a, b), false)

        _eventEmitter.value = event
    }

    // I avoid to use test double and use this method instead for the one test case
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun swapAndComplete(a: Int, b: Int) {
        var event: PuzzleState
        val isSwappable = threeXThreeGrid.isSwappable(a, b)
        event = if (isSwappable) {
            threeXThreeGrid.swap(a, b)
            threeXThreeGrid.getUnSolvedGrid().sort()
            val isGameCompleted = threeXThreeGrid.isGameCompleted()
            if (isGameCompleted) PuzzleState.Completed else PuzzleState.Moved(Pair(b, a), true)
        } else
            PuzzleState.Moved(Pair(a, b), false)
        _eventEmitter.value = event
    }
}


sealed class PuzzleState {
    data class Initialised(val puzzleGrid: Array<Int>) : PuzzleState()
    data class Moved(
        val puzzleMov: Pair<Int, Int>,
        val isMoved: Boolean = true,
        val message: String = "Could be moved"
    ) : PuzzleState()

    object Completed : PuzzleState()
}
 class PuzzleViewModelFactory(private val grid: ThreeXThreeGrid): ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
         return PuzzleViewModel(grid) as T
     }

 }