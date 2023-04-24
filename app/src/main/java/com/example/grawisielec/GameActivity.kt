package com.example.grawisielec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children

class GameActivity : AppCompatActivity() {

    private val gameManager = GameManager() // obiekt klasy GameManager()

    private lateinit var announcement : TextView
    private lateinit var image : ImageView
    private lateinit var wordToFind : TextView
    private lateinit var lettersUsed : TextView
    private lateinit var button: Button
    private lateinit var keyboard : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // powrót do ekranu głównego na support barze; Uwaga! trzeba zmienić w manifeście parent activity

        //inicjalizujemy widgety
        announcement = findViewById(R.id.textView)
        image = findViewById(R.id.imageView)
        wordToFind = findViewById(R.id.textView3)
        lettersUsed = findViewById(R.id.textView2)
        button = findViewById(R.id.button)
        keyboard = findViewById(R.id.lettersLayout)

        announcement.text = "Zgaduj kolejne litery:"

        button.setOnClickListener{
            startNewGame()
        }
        val gameState = gameManager.startNewGame()
        updateUI(gameState)

        keyboard.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState1 = gameManager.step((letterView).text[0])
                    updateUI(gameState1)
                }
            }
        }
    }

    private fun updateUI(gameState: GameState) {
        when (gameState) {
            is GameState.Lost -> showGameIsLost(gameState.wordToGuess)
            is GameState.Running -> {
                wordToFind.text = gameState.underscoreWord
                lettersUsed.text = "Użyte litery: ${gameState.lettersUsed}"
                image.setImageDrawable(ContextCompat.getDrawable(this, gameState.drawable))
            }
            is GameState.Won -> showGameWon()
        }
    }
    private fun showGameIsLost(wordToGuess: String) {
        announcement.text = "Przegrałeś!" + "\n" +  "Słowo do odgadnięcia: " + wordToGuess
        image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable._21))
    }

    private fun showGameWon() {
        announcement.text = "Wygrałeś!"
        image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable._5485))
        wordToFind.text = gameManager.wordToGuess
    }

    private fun startNewGame() {
        val gameState = gameManager.startNewGame()
        announcement.text = "Zgaduj kolejne litery:"
        keyboard.visibility = View.VISIBLE
        keyboard.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
        updateUI(gameState)
    }
}