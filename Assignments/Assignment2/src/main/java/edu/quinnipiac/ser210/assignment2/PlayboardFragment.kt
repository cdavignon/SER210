/*
Logic for game to run
@author cdavignon
@date 2/25/24
 */
package edu.quinnipiac.ser210.assignment2

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


class PlayboardFragment : Fragment()
{
    private val viewModel: edu.quinnipiac.ser210.assignment2.View by activityViewModels()
    private val userTiles = mutableSetOf<Int>()
    private val computerTiles = mutableSetOf<Int>()
    private val tilesSelected = mutableSetOf<Int>()
    private var isGameActive = true // Flag to track if the game is active
    private var userTurn = true

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playboard, container, false)

        val resetButton = view.findViewById<Button>(R.id.resetButton)
        resetButton?.isEnabled = false // Reset button is disabled on start

        viewModel.text.observe(viewLifecycleOwner) { name ->
            view.findViewById<TextView>(R.id.greeting)?.text = "Hello, $name!" // Greets the player at the top of the screen using user inputted name
        }

        //all button ids possible on the playboard
        val buttonsIds = listOf(
            R.id.imageButton0,
            R.id.imageButton1,
            R.id.imageButton2,
            R.id.imageButton3,
            R.id.imageButton4,
            R.id.imageButton5,
            R.id.imageButton6,
            R.id.imageButton7,
            R.id.imageButton8,
            R.id.imageButton9,
            R.id.imageButton10,
            R.id.imageButton11,
            R.id.imageButton12,
            R.id.imageButton13,
            R.id.imageButton14,
            R.id.imageButton15,
            R.id.imageButton16,
            R.id.imageButton17,
            R.id.imageButton18,
            R.id.imageButton19,
            R.id.imageButton20,
            R.id.imageButton21,
            R.id.imageButton22,
            R.id.imageButton23,
            R.id.imageButton24,
            R.id.imageButton25,
            R.id.imageButton26,
            R.id.imageButton27,
            R.id.imageButton28,
            R.id.imageButton29,
            R.id.imageButton30,
            R.id.imageButton31,
            R.id.imageButton32,
            R.id.imageButton33,
            R.id.imageButton34,
            R.id.imageButton35
        )
        buttonsIds.forEach { buttonId ->
            view.findViewById<ImageButton>(buttonId)?.setOnClickListener { buttonView ->
                if (isGameActive && buttonId !in tilesSelected)
                {
                    buttonView.setBackgroundResource(R.drawable.red_tile)
                    userTiles.add(buttonId)
                    tilesSelected.add(buttonId)
                    //user win check
                    if (checkForWin(userTiles))
                    {
                        isGameActive = false
                        setButtonsClickable(view, false)
                        resetButton.isEnabled = true
                    }
                    else
                    {
                        setButtonsClickable(
                            view,
                            false
                        ) //stops the player from interacting during the computer's turn
                        computerMove(buttonsIds, view)
                        {
                            setButtonsClickable(view, isGameActive)
                        }
                    }
                }
            }
        }

        //reset button
        view.findViewById<Button>(R.id.resetButton)?.setOnClickListener {
            buttonsIds.forEach { buttonIds ->
                view.findViewById<ImageButton>(buttonIds)
                    ?.setBackgroundResource(R.drawable.tile_shape)
            }
            userTiles.clear()
            computerTiles.clear()
            tilesSelected.clear()
            isGameActive = true
            view.findViewById<Button>(R.id.resetButton)?.isEnabled = false
            setButtonsClickable(view, true) // Re-enable buttons for a new game
        }

        return view
    }
    //makes all possible buttons interactable
    private fun setButtonsClickable(view: View, clickable: Boolean)
    {
        val buttonsIds = listOf(
            R.id.imageButton0,
            R.id.imageButton34,
            R.id.imageButton1,
            R.id.imageButton2,
            R.id.imageButton3,
            R.id.imageButton4,
            R.id.imageButton5,
            R.id.imageButton6,
            R.id.imageButton7,
            R.id.imageButton8,
            R.id.imageButton9,
            R.id.imageButton10,
            R.id.imageButton11,
            R.id.imageButton12,
            R.id.imageButton18,
            R.id.imageButton14,
            R.id.imageButton15,
            R.id.imageButton16,
            R.id.imageButton17,
            R.id.imageButton18,
            R.id.imageButton18,
            R.id.imageButton14,
            R.id.imageButton15,
            R.id.imageButton16,
            R.id.imageButton17,
            R.id.imageButton14,
            R.id.imageButton30,
            R.id.imageButton29,
            R.id.imageButton28,
            R.id.imageButton27,
            R.id.imageButton34,
            R.id.imageButton34,
            R.id.imageButton29,
            R.id.imageButton28,
            R.id.imageButton27,
            R.id.imageButton34
        )
        buttonsIds.forEach { buttonId ->
            view.findViewById<ImageButton>(buttonId)?.isEnabled = clickable
        }
    }
    //computer turn
    private fun computerMove(buttonsIds: List<Int>, view: View, onMoveCompleted: () -> Unit)
    {
        if (!isGameActive) return
        userTurn = false
        updateTurnStatus()
        Handler(Looper.getMainLooper()).postDelayed(
            {
            val availableSquares = buttonsIds.filterNot { it in tilesSelected }
            if (availableSquares.isNotEmpty())
            {
                val randomSquareId = availableSquares.random()
                activity?.runOnUiThread{
                    view.findViewById<ImageButton>(randomSquareId)?.let { button ->
                        button.setBackgroundResource(R.drawable.blue_tile)
                        computerTiles.add(randomSquareId)
                        tilesSelected.add(randomSquareId)
                    }

                    if (checkForWin(computerTiles))
                    {
                        activity?.runOnUiThread {
                            isGameActive = false
                            setButtonsClickable(view, false)
                        }
                    }
                    else
                    {
                        userTurn = true
                        updateTurnStatus()
                        onMoveCompleted()
                    }
                }
            }
        }, 1000) //adds 1 second delay
    }
    // Updates message for whos turn it is
    private fun updateTurnStatus(winner: String? = null)
    {
        val turnStatusTextView = view?.findViewById<TextView>(R.id.turns)
        turnStatusTextView?.text = when (winner)
        {
            "player" -> "Player Wins"
            "computer" -> "Computer Wins"
            null -> if (userTurn) "Player's Turn" else "Computer's Turn"
            else -> "Draw"
        }
    }
    private fun checkForWin(selectedButtonIds: Set<Int>): Boolean
    {
        val winPatterns = listOf(
            //checks for all possible horizontal win patterns
            listOf(R.id.imageButton0, R.id.imageButton1, R.id.imageButton2, R.id.imageButton3),
            listOf(R.id.imageButton1, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4),
            listOf(R.id.imageButton2, R.id.imageButton3, R.id.imageButton4, R.id.imageButton5),
            listOf(R.id.imageButton6, R.id.imageButton7, R.id.imageButton8, R.id.imageButton9),
            listOf(R.id.imageButton7, R.id.imageButton8, R.id.imageButton9, R.id.imageButton10),
            listOf(R.id.imageButton8, R.id.imageButton9, R.id.imageButton10, R.id.imageButton11),
            listOf(R.id.imageButton12, R.id.imageButton13, R.id.imageButton14, R.id.imageButton15),
            listOf(R.id.imageButton13, R.id.imageButton14, R.id.imageButton15, R.id.imageButton16),
            listOf(R.id.imageButton14, R.id.imageButton15, R.id.imageButton16, R.id.imageButton17),
            listOf(R.id.imageButton18, R.id.imageButton19, R.id.imageButton20, R.id.imageButton21),
            listOf(R.id.imageButton19, R.id.imageButton20, R.id.imageButton21, R.id.imageButton22),
            listOf(R.id.imageButton20, R.id.imageButton21, R.id.imageButton22, R.id.imageButton23),
            listOf(R.id.imageButton24, R.id.imageButton25, R.id.imageButton26, R.id.imageButton27),
            listOf(R.id.imageButton25, R.id.imageButton26, R.id.imageButton27, R.id.imageButton28),
            listOf(R.id.imageButton26, R.id.imageButton27, R.id.imageButton28, R.id.imageButton29),
            listOf(R.id.imageButton30, R.id.imageButton31, R.id.imageButton32, R.id.imageButton33),
            listOf(R.id.imageButton31, R.id.imageButton32, R.id.imageButton33, R.id.imageButton34),
            listOf(R.id.imageButton32, R.id.imageButton33, R.id.imageButton34, R.id.imageButton35),
            //checks for all possible vertical win patterns
            listOf(R.id.imageButton0, R.id.imageButton6, R.id.imageButton12, R.id.imageButton18),
            listOf(R.id.imageButton6, R.id.imageButton12, R.id.imageButton18, R.id.imageButton24),
            listOf(R.id.imageButton12, R.id.imageButton18, R.id.imageButton24, R.id.imageButton30),
            listOf(R.id.imageButton1, R.id.imageButton7, R.id.imageButton13, R.id.imageButton19),
            listOf(R.id.imageButton7, R.id.imageButton13, R.id.imageButton19, R.id.imageButton25),
            listOf(R.id.imageButton13, R.id.imageButton19, R.id.imageButton25, R.id.imageButton31),
            listOf(R.id.imageButton2, R.id.imageButton8, R.id.imageButton14, R.id.imageButton20),
            listOf(R.id.imageButton8, R.id.imageButton14, R.id.imageButton20, R.id.imageButton26),
            listOf(R.id.imageButton14, R.id.imageButton20, R.id.imageButton26, R.id.imageButton32),
            listOf(R.id.imageButton3, R.id.imageButton9, R.id.imageButton15, R.id.imageButton21),
            listOf(R.id.imageButton9, R.id.imageButton15, R.id.imageButton21, R.id.imageButton27),
            listOf(R.id.imageButton15, R.id.imageButton21, R.id.imageButton27, R.id.imageButton33),
            listOf(R.id.imageButton4, R.id.imageButton10, R.id.imageButton16, R.id.imageButton22),
            listOf(R.id.imageButton10, R.id.imageButton16, R.id.imageButton22, R.id.imageButton28),
            listOf(R.id.imageButton16, R.id.imageButton22, R.id.imageButton28, R.id.imageButton34),
            listOf(R.id.imageButton5, R.id.imageButton11, R.id.imageButton17, R.id.imageButton23),
            listOf(R.id.imageButton11, R.id.imageButton17, R.id.imageButton23, R.id.imageButton29),
            listOf(R.id.imageButton17, R.id.imageButton23, R.id.imageButton29, R.id.imageButton35),
            //checks for all possible diagonal win patterns
            listOf(R.id.imageButton0, R.id.imageButton7, R.id.imageButton14, R.id.imageButton21),
            listOf(R.id.imageButton1, R.id.imageButton8, R.id.imageButton15, R.id.imageButton22),
            listOf(R.id.imageButton2, R.id.imageButton9, R.id.imageButton16, R.id.imageButton23),
            listOf(R.id.imageButton6, R.id.imageButton13, R.id.imageButton20, R.id.imageButton27),
            listOf(R.id.imageButton7, R.id.imageButton14, R.id.imageButton21, R.id.imageButton28),
            listOf(R.id.imageButton8, R.id.imageButton15, R.id.imageButton22, R.id.imageButton29),
            listOf(R.id.imageButton12, R.id.imageButton19, R.id.imageButton26, R.id.imageButton33),
            listOf(R.id.imageButton13, R.id.imageButton20, R.id.imageButton27, R.id.imageButton34),
            listOf(R.id.imageButton14, R.id.imageButton21, R.id.imageButton28, R.id.imageButton35),
            listOf(R.id.imageButton3, R.id.imageButton8, R.id.imageButton13, R.id.imageButton18),
            listOf(R.id.imageButton4, R.id.imageButton9, R.id.imageButton14, R.id.imageButton19),
            listOf(R.id.imageButton5, R.id.imageButton10, R.id.imageButton15, R.id.imageButton20),
            listOf(R.id.imageButton9, R.id.imageButton14, R.id.imageButton19, R.id.imageButton24),
            listOf(R.id.imageButton10, R.id.imageButton15, R.id.imageButton20, R.id.imageButton25),
            listOf(R.id.imageButton11, R.id.imageButton16, R.id.imageButton21, R.id.imageButton26),
            listOf(R.id.imageButton15, R.id.imageButton20, R.id.imageButton25, R.id.imageButton30),
            listOf(R.id.imageButton16, R.id.imageButton21, R.id.imageButton26, R.id.imageButton31),
            listOf(R.id.imageButton17, R.id.imageButton22, R.id.imageButton27, R.id.imageButton32)
        )
        val winFound = winPatterns.any { pattern -> selectedButtonIds.containsAll(pattern) }
        if (winFound)
        {
            val winner =
                if (userTiles.containsAll(selectedButtonIds)) "player" else "computer"
            updateTurnStatus(winner)
            isGameActive = false
            view?.findViewById<Button>(R.id.resetButton)?.isEnabled = true
        }
        return winFound
    }
}