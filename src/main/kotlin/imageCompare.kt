import org.intellij.lang.annotations.Language
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import org.jetbrains.skia.Image
import org.jetbrains.skia.Paint
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder
import org.jetbrains.skiko.toBufferedImage
import java.awt.image.BufferedImage

fun main() {
    val before = ClassLoader.getSystemResource("before.png").readBytes()
    val after = ClassLoader.getSystemResource("after.png").readBytes()

    val beforeImage = before.readImage()
    val afterImage = after.readImage()
    val diffImage = compare(beforeImage, afterImage)
    println("Done")
}

fun ByteArray.readImage(): Image {
    val codec = Codec.makeFromData(Data.makeFromBytes(this))
    val bitmap = Bitmap()
    bitmap.allocN32Pixels(codec.width, codec.height)
    codec.readPixels(bitmap)
    return Image.makeFromBitmap(bitmap)
}

fun Image.toBufferedImage(): BufferedImage {
    return Bitmap.makeFromImage(this).toBufferedImage()
}

fun compare(first: Image, second: Image): Image {
    @Language("GLSL")
    val diffShader = """
        uniform shader first;
        uniform shader second;
        
        half4 main(float2 coord) {
           half4 firstColor = first.eval(coord);
           half4 secondColor = second.eval(coord);
           
           return half4(
               abs(firstColor.r - secondColor.r),
               abs(firstColor.g - secondColor.g),
               abs(firstColor.b - secondColor.b),
               1.0
           );
        }
    """.trimIndent()

    val shaderBuilder = RuntimeShaderBuilder(RuntimeEffect.makeForShader(diffShader))
    shaderBuilder.child("first", first.makeShader())
    shaderBuilder.child("second", second.makeShader())

    val resultBitmap = Bitmap()
    resultBitmap.allocN32Pixels(first.width, first.height)

    val resultCanvas = Canvas(resultBitmap)

    val paint = Paint()
    paint.shader = shaderBuilder.makeShader()

    resultCanvas.drawPaint(paint)
    resultCanvas.close()

    return Image.makeFromBitmap(resultBitmap)
}
