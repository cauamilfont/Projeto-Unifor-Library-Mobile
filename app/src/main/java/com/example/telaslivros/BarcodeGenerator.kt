package com.example.telaslivros

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

object BarcodeGenerator {

    fun generateBarcode(content: String, width: Int = 600, height: Int = 200): Bitmap? {

        if (content.isBlank()) return null

        return try {

            val writer = MultiFormatWriter()

            val bitMatrix = writer.encode(
                content,
                BarcodeFormat.CODE_128,
                width,
                height
            )


            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)

            return bitmap

        } catch (e: WriterException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
