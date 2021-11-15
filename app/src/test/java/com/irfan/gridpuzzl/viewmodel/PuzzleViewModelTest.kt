package com.irfan.gridpuzzl.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import com.irfan.gridpuzzl.PuzzleState
import com.irfan.gridpuzzl.PuzzleViewModel
import com.irfan.gridpuzzl.ThreeXThreeGrid
import com.irfan.gridpuzzl.getOrAwaitValue
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

/**===Todolist===
1- Initial state event emitted
2- Tags successfully swapped
3- Tags can not swapped
4- Game is completed


 */

@MediumTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PuzzleViewModelTest {
    private lateinit var threeXThreeGrid: ThreeXThreeGrid
    private lateinit var puzzleViewModel: PuzzleViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        threeXThreeGrid = ThreeXThreeGrid()
        puzzleViewModel = PuzzleViewModel(threeXThreeGrid)
    }

    /*
      Test case 1:
        given puzzle view model
        when constructed the view model
        the init state is emitted in the event
     */


    @Test
    fun `1- When ViewModel is constructed then event with initial state is triggered`() {
        val eventEmitter: LiveData<PuzzleState> = puzzleViewModel.eventEmitter
        val currentState = eventEmitter.getOrAwaitValue()
        val result = currentState is PuzzleState.Initialised
        Truth.assertThat(result).isTrue()
        Truth.assertThat((currentState as PuzzleState.Initialised).puzzleGrid).isNotEmpty()
    }

    @Test
    fun `2- When tags are moved then the tags are successfully swapped`() {
        val unsolvedGrid = threeXThreeGrid.getUnSolvedGrid()

        val indexA = 0
        val indexB =  unsolvedGrid.size - 1
        val a = unsolvedGrid[0]
        val b = unsolvedGrid[unsolvedGrid.size - 1]
        val expectedState = PuzzleState.Moved(Pair(Pair(unsolvedGrid.indexOf(b),a), Pair(unsolvedGrid.indexOf(a),b)))

        puzzleViewModel.swap(indexA, indexB)

        val actualState = puzzleViewModel.eventEmitter.getOrAwaitValue()
        Truth.assertThat(actualState).isEqualTo(expectedState)
    }

    @Test
    fun `3- When tags already in correct position then can not move`() {
        val unsolvedGrid = threeXThreeGrid.getUnSolvedGrid()
        val a = unsolvedGrid[0]
        val b = unsolvedGrid[unsolvedGrid.size - 1]

        val c = unsolvedGrid[a]
        unsolvedGrid[a] = a
        unsolvedGrid[0] = c

        puzzleViewModel.swap(a, b)

        val actualState = puzzleViewModel.eventEmitter.getOrAwaitValue() as PuzzleState.Moved
        Truth.assertThat(actualState.isMoved).isFalse()
    }
    @Test
    fun `4- When all tags are at correct positions then game completed`() {
        val unsolvedGrid = threeXThreeGrid.getUnSolvedGrid()
        val a = unsolvedGrid[0]
        val b = unsolvedGrid[unsolvedGrid.size - 1]
        puzzleViewModel.swapAndComplete(a, b)
        Truth.assertThat(puzzleViewModel.eventEmitter.getOrAwaitValue()).isEqualTo(PuzzleState.Completed)
    }



}




