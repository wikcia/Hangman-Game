package com.example.grawisielec

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameManager {

    private var lettersUsed: String = ""
    private lateinit var underscoreWord: String
    lateinit var wordToGuess: String
    private val maxTries = 9
    private var currentTries = 0
    private var gallows: Int = R.drawable.one
    private val countriesArray: Array<String> = arrayOf("Polska", "Albania", "Maroko")
    private val applicationContext = AppApplication.getInstance().applicationContext

    /**
     * !!!UWAGA!!!
     * GameManager nie dziedziczy po Application więc żeby dostać się do folderu strings.xml
     * musimy użyć kontekstu aplikacji(Application Context);
     * możemy użyć kontekstu aplikacji w dowolnym miejscu ze swojej aplikacji
     * dlatego stworzyłam klasę AppApplication aby zwróciła mi ten kontest
     * dodatkowo w manifeście dodałam android:name=".AppApplication"
     * !!!UWAGA!!!
     */

    fun startNewGame() : GameState {
        lettersUsed = ""
        currentTries = 0
        gallows = R.drawable.one
        val array: Array<String> = applicationContext.resources.getStringArray(R.array.countries_array)
        val randomIndex = Random.nextInt(0, array.size)
        wordToGuess = array[randomIndex] // losujemy słowo
        generateUnderscores(wordToGuess)
        return getGameState()
    }

    // generujemy tele podłóg ile wynosi długość słowa
    private fun generateUnderscores(wordToGuess: String) {
        val stringBuilder = StringBuilder()
        wordToGuess.forEach { char ->
            if (char == ' ') {
                stringBuilder.append(' ')
            } else {
                stringBuilder.append("_")
            }
        }
        underscoreWord = stringBuilder.toString()
    }

    // funkcja pojedynczego ruchu
    // odpowiednio wypełnia pusty wyraz odgadniętymi literami
    fun step(letter: Char): GameState {

        // wybraliśmy ponownie literę która już była wcześniej wybrana
        if (lettersUsed.contains(letter)) {
            return GameState.Running(lettersUsed, underscoreWord, gallows)
        }

        lettersUsed += letter // dodajemy literę do uzytych liter
        val indexes = mutableListOf<Int>() // lista indeksów na których bedzie dany letter

        // zapamiętujemy na których indeksach jest letter którą prezkazaliśmy w metodzie
        wordToGuess.forEachIndexed { index, char ->
            if (char.equals(letter, true)) {
                indexes.add(index)
            }
        }

        /*** This is how replace a character at a specific index in a Kotlin string
         *  fun main() {
                var string = "Kotlin 1.3"
                val char = '_'
                val index = 6

                val sb = StringBuilder(string).also { it.setCharAt(index, char) }
                string = sb.toString()

                println(string)        // Kotlin_1.3
                }
         */
        // podmieniamy underscore ze znalezioną literą:  _ _ _ _ _ _ _ -> E _ _ _ _ _ _
        var finalUnderscoreWord = underscoreWord
        indexes.forEach { index ->
            val stringBuilder = StringBuilder(finalUnderscoreWord).also { it.setCharAt(index, letter) }
            finalUnderscoreWord = stringBuilder.toString()
        }

        // jeżeli nie trafiliśmy na żadną literkę
        if (indexes.isEmpty()) {
            currentTries++
        }

        underscoreWord = finalUnderscoreWord
        return getGameState()
    }

    // zwraca odpowiedni obrazek wisielca
    private fun getGallowsDrawable(): Int {
        return when (currentTries) {
            0 -> R.drawable.one
            1 -> R.drawable.twotwo
            2 -> R.drawable.threethree
            3 -> R.drawable.fourfour
            4 -> R.drawable.fivefive
            5 -> R.drawable.sixsix
            6 -> R.drawable.sevenseven
            7 -> R.drawable.eighteight
            8 -> R.drawable.ninenie
            9 -> R.drawable._21
            else -> R.drawable._21
        }
    }

    // zwracamy stan gry
    private fun getGameState(): GameState {
        if (underscoreWord.equals(wordToGuess, true)) {
            return GameState.Won(wordToGuess)
        }

        if (currentTries == maxTries) {
            return GameState.Lost(wordToGuess)
        }

        gallows = getGallowsDrawable()
        return GameState.Running(lettersUsed, underscoreWord, gallows)
    }
}