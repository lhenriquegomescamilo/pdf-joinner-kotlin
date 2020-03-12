package com.pdfmerge

import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import java.io.FileInputStream
import java.io.IOException

class PDFMerger internal constructor() {
    @Throws(IOException::class)
    internal fun merge(args: Array<String>) {
        val firstFileArgPos = 0
        if (args.size - firstFileArgPos < 3) {
            usage()
        }
        val merger = PDFMergerUtility()
        merger.documentMergeMode = PDFMergerUtility.DocumentMergeMode.OPTIMIZE_RESOURCES_MODE
        merger.acroFormMergeMode = PDFMergerUtility.AcroFormMergeMode.JOIN_FORM_FIELDS_MODE
        val destinationFileName = args[args.size - 1]
        for (i in firstFileArgPos until args.size - 1) merger.addSource(FileInputStream(args[i]))

        merger.destinationFileName = destinationFileName
        val setupTempFileOnly = MemoryUsageSetting.setupTempFileOnly()
        merger.mergeDocuments(setupTempFileOnly)
        System.gc()
        AddBookmarkExistingPdfDocument.addBookmark(filePath = destinationFileName, memorySettings = setupTempFileOnly)

    }

    companion object {
        /**
         * This will print the usage requirements and exit.
         */
        private fun usage() {
            val message = """Usage: java -jar pdfbox-app-x.y.z.jar PDFMerger <inputfiles 2..n> <outputfile>
                Options:
                  <inputfiles 2..n> : 2 or more source PDF documents to merge
                  <outputfile>      : The PDF document to save the merged documents to
            """
            System.err.println(message)
            System.exit(1)
        }
    }
}
