package com.example.mobcomfinal

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun QRCodeImage(data: String, modifier: Modifier) {
    val size = 400
    val bitmap = remember(data) {
    val writer = com.google.zxing.qrcode.QRCodeWriter()
    val bitMatrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE,size,size)

    Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).apply {
        for (x in 0 until size) {
            for(y in 0 until size) {
                setPixel(x, y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                 }
            }
        }
    }
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "QR Code",
        modifier = Modifier.size(200.dp)
    )
}
