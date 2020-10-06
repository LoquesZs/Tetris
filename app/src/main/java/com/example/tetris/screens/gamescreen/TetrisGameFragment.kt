package com.example.tetris.screens.gamescreen

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tetris.R
import com.example.tetris.RESULT_SCORE
import com.example.tetris.utils.ScoreHolder
import kotlinx.android.synthetic.main.fragment_tetris_game.*

class TetrisGameFragment : Fragment() {

    private lateinit var tetrisFieldInvalidation: Runnable
    private lateinit var handler: Handler
    private lateinit var tetrisGameViewModel: TetrisGameViewModel
    private lateinit var scoreHolder: ScoreHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        handler = Handler()
        tetrisFieldInvalidation = Runnable {
            if (tetrisField != null) tetrisField.invalidate()
            if (score_display != null) score_display.text = scoreHolder.currentScore.toString()
            if (best_score_display != null) best_score_display.text = scoreHolder.bestScore.toString()
            if (tetrisGameViewModel.isGameOver) {
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.tetrisGameFragment) {
                    val bundle = bundleOf(RESULT_SCORE to scoreHolder.currentScore)
                    navController.navigate(R.id.action_tetrisGameFragment_to_gameOver, bundle)
                }
            }
            handler.postDelayed(tetrisFieldInvalidation, 17)
        }
        scoreHolder = activity?.let { ScoreHolder(it) }!!
        tetrisGameViewModel = TetrisGameViewModel(scoreHolder)

        return inflater.inflate(R.layout.fragment_tetris_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        score_display.text = scoreHolder.currentScore.toString()

        best_score_display.text = scoreHolder.bestScore.toString()

        left_button.setOnClickListener {
            tetrisGameViewModel.moveLeft()
        }

        rotate_button.setOnClickListener {
            tetrisGameViewModel.rotateFigure()
        }

        right_button.setOnClickListener {
            tetrisGameViewModel.moveRight()
        }

        new_game_button.setOnClickListener {
            tetrisGameViewModel.endGame()
            scoreHolder.saveBestScore()
            scoreHolder.currentScore = 0
            score_display.text = scoreHolder.currentScore.toString()
            tetrisGameViewModel.startGame()
        }

        speed_up_button.setOnTouchListener { v, event ->
            v.performClick()
            if (event != null) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        tetrisGameViewModel.speedUp()
                    }
                    MotionEvent.ACTION_UP -> {
                        tetrisGameViewModel.slowDown()
                    }
                }
            }
            true
        }

        pause_button.setOnClickListener {
            it.findNavController().navigate(R.id.action_tetrisGameFragment_to_pauseMenu)
        }

        tetrisField.field = tetrisGameViewModel.gameField
    }

    override fun onStart() {
        handler.post(tetrisFieldInvalidation)
        tetrisGameViewModel.startGame()
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        tetrisGameViewModel.pause()
        handler.removeCallbacks(tetrisFieldInvalidation)
        scoreHolder.saveBestScore()
    }

    override fun onResume() {
        super.onResume()
        tetrisGameViewModel.resume()
        handler.post(tetrisFieldInvalidation)
    }

    override fun onStop() {
        super.onStop()
        tetrisGameViewModel.endGame()
        handler.removeCallbacks(tetrisFieldInvalidation)
    }
}