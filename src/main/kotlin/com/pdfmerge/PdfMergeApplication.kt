package com.pdfmerge


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.IOException


/**
 * Infamous main method.
 *
 * @param args Command line arguments, should be at least 3.
 *
 * @throws IOException If there is an error parsing the document.
 */

fun main(args: Array<String>) {
        System.setProperty("apple.awt.UIElement", "true")
        val merge = PDFMerger()
        merge.merge(args)

}

//fun savePDf(filesPat}


