package com.education.flashwiseapp.common.util

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun getProhibitedWordsList(context: Context): List<String> {

    val prohibitedWords = mutableListOf<String>()

    val assetManager = context.assets

    try {
        val inputStream = assetManager.open("textfiles/prohibited_words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            prohibitedWords.add(line)
            line = reader.readLine()
        }
        reader.close()
    } catch (e: IOException) {
        Log.e("UtilFunctions", "Exception: $e")
    }

    return prohibitedWords
}