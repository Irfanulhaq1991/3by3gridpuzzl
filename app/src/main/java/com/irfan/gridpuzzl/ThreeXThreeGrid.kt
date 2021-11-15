package com.irfan.gridpuzzl

import androidx.core.content.res.TypedArrayUtils
import kotlin.random.Random

class ThreeXThreeGrid {
    private val gridSize = 9   // 3*3 2dimension result in 9 one dimension

    // one dimension array will work perfectly
    private val solvedGrid = Array(gridSize) { it }
    private val unSolvedGrid = solvedGrid.copyOf().reversedArray()


    fun getUnSolvedGrid(): Array<Int> {
        return unSolvedGrid
    }

    fun swap(a: Int, b: Int) {
        if (solvedGrid[solvedGrid.size - 1] < a || a < solvedGrid[0] || solvedGrid[solvedGrid.size - 1] < b || b < solvedGrid[0]) return
        val indexA = unSolvedGrid.indexOf(a)
        val indexB = unSolvedGrid.indexOf(b)
        unSolvedGrid[indexA] = b
        unSolvedGrid[indexB] = a
    }

    fun isGameCompleted(): Boolean {
        for (i in unSolvedGrid.indices) {
            if (i != unSolvedGrid[i]) return false
        }
        return true
    }

    fun isSwappable(a: Int, b: Int): Boolean {
        val unSolvedIndexOfA = unSolvedGrid.indexOf(a)
        val unSolvedIndexOfB = unSolvedGrid.indexOf(b)
        val solvedIndexOfA = solvedGrid.indexOf(a)
        val solvedIndexOfB = solvedGrid.indexOf(b)
        val canSwap = unSolvedIndexOfA == solvedIndexOfA && unSolvedIndexOfB == solvedIndexOfB

        return !canSwap
    }


}
