package day10

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.timer.repeat
import org.openrndr.extra.videoprofiles.gif
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.shape.Rectangle
import utils.readInput

val lines = readInput("day10/input")
val device = Device(lines)
val bits = device.bits()

const val gridWidth = 20.0
const val gridHeight = 20.0
const val delay = 300L

class Display: Animatable() {
    private var cursorX = 0.0
    private var cursorY = 0.0

    private val display = renderTarget(40 * gridWidth.toInt(), 6 * gridHeight.toInt()) {
        colorBuffer()
    }

    fun update() {
        updateAnimation()
    }

    fun moveCursor(pos: Int) {
        val y = pos / 40
        val x = pos % 40

        moveCursor(x.toDouble(), y.toDouble())
    }

    private fun moveCursor(x: Double, y: Double) {
        ::cursorX.cancel()
        ::cursorX.animate(x, delay, Easing.CubicInOut)

        ::cursorY.cancel()
        ::cursorY.animate(y, delay, Easing.CubicInOut)
    }

    fun draw(drawer: Drawer) {
        drawer.image(display.colorBuffer(0))

        drawer.stroke = ColorRGBa.RED
        drawer.fill = null
        drawer.rectangle(cursorX * gridWidth ,cursorY * gridHeight, gridWidth, gridHeight)
    }

    fun drawBits(drawer: Drawer) {
        drawer.isolatedWithTarget(display) {
            for ((i, b) in bits.withIndex()) {
                val y = i / 40
                val x = i % 40

                drawer.stroke = null
                drawer.fill = if(b) ColorRGBa.YELLOW else ColorRGBa.GRAY
                drawer.rectangle(x * gridWidth, y * gridHeight, gridWidth, gridHeight)
            }
        }
    }

    fun drawBit(drawer: Drawer, pos: Int) {
        drawer.isolatedWithTarget(display) {
            val y = pos / 40
            val x = pos % 40

            val rect = Rectangle(x * gridWidth + 1, y * gridHeight + 1, gridWidth - 2, gridHeight - 2)

            drawer.stroke = null
            drawer.fill = if(bits[pos]) ColorRGBa.YELLOW else ColorRGBa(1.0,1.0,1.0,.1)
            drawer.rectangle(rect)
        }
    }
}

class Sprite(private val height: Double): Animatable() {
    private var x = 1.0

    fun update() {
        updateAnimation()
    }

    fun move(x: Double) {
        ::x.cancel()
        ::x.animate(x, delay, Easing.CubicInOut)
    }

    fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa(.0,.5,.5,.25)
        drawer.stroke = null

        drawer.rectangle((x - 1.0) * gridHeight, 0.0, gridWidth * 3, height)
    }
}

fun main() = application {
    configure {
        width = 800
        height = 120
    }

    program {
        var index = -1
        val display = Display()
        val sprite = Sprite(this.height.toDouble())

        extend(ScreenRecorder()) {
            gif()
        }

        repeat(delay / 1000.0, 40*6) {
            val spriteX = device.listX[index+1]

            if(index >= 0) {
                display.drawBit(drawer, index)
            }

            sprite.move(spriteX.toDouble())
            display.moveCursor(index+1)

            index++
        }

        extend {
            drawer.clear(ColorRGBa.BLACK)

            display.update()
            display.draw(drawer)

            sprite.update()
            sprite.draw(drawer)
        }
    }
}
