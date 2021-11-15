package com.irfan.gridpuzzl.grid

import com.google.common.truth.Truth
import com.irfan.gridpuzzl.ThreeXThreeGrid
import org.junit.Before
import org.junit.Test


class ThreeXThreeGridTest {
    private lateinit var grid3x3: ThreeXThreeGrid
    /*Test Case 1:
        - Give a 3x3 grid
        - when grid is initialised
        - Then grid size is 9 */


    @Before
    fun setup() {
        grid3x3 = ThreeXThreeGrid()

    }

    @Test
    fun `The Grid Must Have Size of 9`() {
        val grid = grid3x3.getUnSolvedGrid()
        val expected = 9
        Truth.assertThat(grid.size).isEqualTo(expected)
    }

    /*Test Case 2:
       - Given a 3x3 grid when
       - when initialised
       - then
            - all numbers are unique*/

    @Test
    fun `All the number in the grid should be unique`() {
        val grid = grid3x3.getUnSolvedGrid()
        val expected = true
        val actual = checkAllEntriesUnique(grid)
        Truth.assertThat(actual).isEqualTo(expected)
    }


    /*Test Case 3:
          - given a grid3x3
          - when execute swap(a,b)
          - then numbers should successfully swap*/

    @Test
    fun `Grid should correctly swap elements`() {
        val unSolvedGrid = grid3x3.getUnSolvedGrid()
        val a = unSolvedGrid[0]
        val b = unSolvedGrid[unSolvedGrid.size - 1]
        val result = grid3x3.swap(a, b)
        val actualA = unSolvedGrid[result?.first?.first?:-1]
        val actualB = unSolvedGrid[result?.second?.first?:-1]
        Truth.assertThat(actualA).isEqualTo(a)
        Truth.assertThat(actualB).isEqualTo(b)
    }

    /*Test Case 4:
      - given a grid3x3
      - when execute swap(a,b) with a number not contain in the grid
      - then numbers should not swap*/

    @Test
    fun `Grid should prevent non contain elements`() {
        val unSolvedGrid = grid3x3.getUnSolvedGrid()
        val a = unSolvedGrid[0] - unSolvedGrid[0]-1
        val b = unSolvedGrid[unSolvedGrid.size - 1]
        grid3x3.swap(a, b)
        val actualA = unSolvedGrid[unSolvedGrid.size - 1]
        Truth.assertThat(actualA).isNotEqualTo(a)
        Truth.assertThat(actualA).isEqualTo(b)
    }


    /*Test case 5:
          - given a 3x3 grid grid
          - when a grid is  solved
          - grid should return true*/


    @Test
    fun `Puzzle is solved then game should complete`() {
        grid3x3.getUnSolvedGrid().sort()
        val actualMessage = grid3x3.isGameCompleted()
        Truth.assertThat(actualMessage).isTrue()
    }




    /*Test Case 6:
          - given a 3x3 grid
          - when unsolved grid
          - grid should not return false
     */

    @Test
    fun `When puzzle is not solved then game should not complete`() {
        val actualMessage = grid3x3.isGameCompleted()
        Truth.assertThat(actualMessage).isFalse()
    }

    /*Test Case 7:
          - given a 3x3 grid
          - when numbered tag and index are equal
          - grid then return false indicating number is correct position and can not move

     */

    @Test
    fun `can not swap when the number is on the correct position`(){
        val unSolvedGrid = grid3x3.getUnSolvedGrid()
        val a = 0
        unSolvedGrid[0] = a
        val result:Boolean  = grid3x3.isSwappable(a)
        Truth.assertThat(result).isFalse()
    }


    // test helper
    private fun checkAllEntriesUnique(arr: Array<Int>): Boolean {
        var lastValue = arr[0]
        for (i in 1 until arr.size) {
            val currentValue = arr[i]
            if (lastValue == arr[i]) return false
            lastValue = currentValue
        }
        return true
    }
}
