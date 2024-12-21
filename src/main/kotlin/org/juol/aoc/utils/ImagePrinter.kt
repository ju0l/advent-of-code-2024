package org.juol.aoc.utils

import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

private fun createImageWithText(text: String): BufferedImage {
    val lines = text.lines()
    val bufferedImage = BufferedImage(lines.size * 15 + 20, lines.size * 15 + 20, BufferedImage.TYPE_INT_RGB)
    val g = bufferedImage.graphics
    g.font = Font("Hack", Font.PLAIN, 11)

    lines.forEachIndexed { i, row ->
        g.drawString(row, 5, i * 15 + 10)
    }

    return bufferedImage
}

fun printText(
    text: String,
    outputFile: File,
) {
    try {
        val bi = createImageWithText(text)
        ImageIO.write(bi, "png", outputFile)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
