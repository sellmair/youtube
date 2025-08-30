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
    val before = ClassLoader.getSystemResource("before.png").readBytes().readImage()
    val after = ClassLoader.getSystemResource("after.png").readBytes().readImage()
    val diff = compare(before, after)
    println("Done!")
}

fun compare(first: Image, second: Image): Image {
    @Language("GLSL")
    val diffShader = """
        uniform shader a;
        uniform shader b;
        
        half4 main(float2 coord) {
            half4 aColor = a.eval(coord);
            half4 bColor = b.eval(coord);
            return half4(abs(aColor.r - bColor.r), abs(aColor.g - bColor.g), abs(aColor.b - bColor.b), 1.0);
        }
    """.trimIndent()

    val shaderBuilder = RuntimeShaderBuilder(RuntimeEffect.makeForShader(diffShader))
    shaderBuilder.child("a", first.makeShader())
    shaderBuilder.child("b", second.makeShader())


    val resultBitmap = Bitmap()
    resultBitmap.allocN32Pixels(first.width, first.height)
    val resultCanvas = Canvas(resultBitmap)

    val paint = Paint()
    paint.shader = shaderBuilder.makeShader()

    resultCanvas.drawPaint(paint)
    resultCanvas.close()
    return Image.makeFromBitmap(resultBitmap)
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
