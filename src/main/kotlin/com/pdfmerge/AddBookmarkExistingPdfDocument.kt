package com.pdfmerge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PageMode
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
import java.io.FileInputStream
import java.io.IOException


object AddBookmarkExistingPdfDocument {
    @JvmStatic
    fun addBookmark(filePath: String, memorySettings: MemoryUsageSetting) {
        try {
            PDDocument.load(FileInputStream(filePath), memorySettings).use { document ->
                processBookmarks(document, filePath)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun processBookmarks(document: PDDocument, filePath: String) {
        try {
            val documentOutline = PDDocumentOutline()
            document.documentCatalog.documentOutline = documentOutline
            val pagesOutline = PDOutlineItem()
            pagesOutline.title = "All Pages"
            documentOutline.addLast(pagesOutline)
            for (i in 0 until document.numberOfPages) {
                createBookmark(document, i, pagesOutline)
            }
            pagesOutline.openNode()
            documentOutline.openNode()
            document.documentCatalog.pageMode = PageMode.USE_OUTLINES
            document.save(filePath)
        } finally {
            document.close()
        }
    }

    private fun createBookmark(document: PDDocument, i: Int, pagesOutline: PDOutlineItem)  {
        val pageDestination: PDPageDestination = PDPageFitWidthDestination()
        document.pages.count
        pageDestination.page = document.getPage(i)
        val children =  generateBookmark(pageDestination, "Document Page" + (i + 1))
        val bookmarkDeferred =  generateBookmark(pageDestination, "Document Page" + (i + 1))
        val bookmark = bookmarkDeferred
        bookmark.addLast(children)
        pagesOutline.addLast(bookmark)
    }

    private fun generateBookmark(pageDestination: PDPageDestination, title: String): PDOutlineItem {
        val children = PDOutlineItem()
        children.destination = pageDestination
        children.title = title
        return children
    }
}
