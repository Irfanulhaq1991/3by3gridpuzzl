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




    fun getCurrentState(){
        _eventEmitter.value = PuzzleState.Initialised(threeXThreeGrid.getUnSolvedGrid())
    }

    fun swap(indexA: Int, indexB: Int) {
        var event: PuzzleState

        val a = threeXThreeGrid.getUnSolvedGrid()[indexA]
        val b = threeXThreeGrid.getUnSolvedGrid()[indexB]

        val isASwappable = threeXThreeGrid.isSwappable(a)
        val isBSwappable = threeXThreeGrid.isSwappable(b)

        event = if (isASwappable && isBSwappable) {
            val result = threeXThreeGrid.swap(a, b)
            val isGameCompleted = threeXThreeGrid.isGameCompleted()
            if (isGameCompleted) PuzzleState.Completed(result) else PuzzleState.Moved(result, true)
        } else
            PuzzleState.Moved(null, false)

        _eventEmitter.value = event
    }


    // I avoided to use test double and use this method instead for the one test case
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun swapAndComplete(a: Int, b: Int) {
        var event: PuzzleState
        val isASwappable = threeXThreeGrid.isSwappable(a)
        val isBSwappable = threeXThreeGrid.isSwappable(b)
        event = if (isASwappable && isBSwappable) {
            val result = threeXThreeGrid.swap(a, b)
            threeXThreeGrid.getUnSolvedGrid().sort() // solved
            val isGameCompleted = threeXThreeGrid.isGameCompleted()
            if (isGameCompleted) PuzzleState.Completed(result) else PuzzleState.Moved(result, true)
        } else
            PuzzleState.Moved(null,false,"Could not move")
        _eventEmitter.value = event
    }
}


sealed class PuzzleState {
    data class Initialised(val puzzleGrid: Array<Int>) : PuzzleState()
    data class Moved(
        val puzzleMov:Pair<Pair<Int, Int>, Pair<Int, Int>>?,
        val isMoved: Boolean = true,
        val message: String = ""
    ) : PuzzleState()

    data class Completed(val puzzleMov:Pair<Pair<Int, Int>, Pair<Int, Int>>?,val message:String = "Gam is Completed") : PuzzleState()
}

class PuzzleViewModelFactory(private val grid: ThreeXThreeGrid) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PuzzleViewModel(grid) as T
    }

}