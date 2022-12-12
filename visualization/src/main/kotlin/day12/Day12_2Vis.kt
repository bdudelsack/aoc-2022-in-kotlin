package day12_2

import kotlinx.coroutines.yield
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.color.palettes.colorSequence
import org.openrndr.extra.color.presets.BROWN
import org.openrndr.extra.color.presets.DARK_BLUE
import org.openrndr.extra.color.presets.DARK_GREEN
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.videoprofiles.gif
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.launch
import org.openrndr.shape.Rectangle
import utils.Point
import utils.map.Map2D
import utils.readInput

fun main() = application {
    configure {
        width = 830
        height = 410
    }

    program {
        extend(ScreenRecorder()) {
            gif()
        }

        val testInput = readInput("day12/input")
        val heightMap = Map2D.readFromLines(testInput) { c, _, _ -> c }
        val solver = Day12_2Solver(heightMap)

        val colors = colorSequence(
            0.0 to ColorRGBa.DARK_GREEN,
            0.5 to ColorRGBa.BROWN,
            1.0 to ColorRGBa.DARK_BLUE
        ) blend ('z' - 'a' + 1)


        abstract class AbstractMapDrawer<T>(val map: Map2D<T>, val gridW: Double = 20.0, val gridH: Double = 20.0) {

            val padding = 1

            fun draw(drawer: Drawer) {
                drawer.isolated {
                    map.forEach { c, x, y ->

                        val rect = rect(x,y)
                        drawCell(drawer, rect, c)
                    }
                }
            }

            fun rect(x: Int, y: Int) = Rectangle(
                x * gridW + padding,
                y * gridH + padding,
                gridW - padding * 2,
                gridH - padding * 2
            )

            abstract fun drawCell(drawer: Drawer, rect: Rectangle, value: T)

        }

        class HeightMapDrawer(map: Map2D<Char>, gridW: Double = 20.0, gridH: Double = 20.0): AbstractMapDrawer<Char>(map, gridW, gridH) {
            override fun drawCell(drawer: Drawer, rect: Rectangle, value: Char) {

                val color = when (value) {
                    'a' -> ColorRGBa.GREEN
                    'S' -> ColorRGBa.GREEN
                    'E' -> ColorRGBa.DARK_BLUE
                    else -> { colors[value - 'a'] }
                }

                drawer.fill = color
                drawer.stroke = null
                drawer.rectangle(rect)
            }
        }

        val mapTexture = renderTarget(width, height) {
            colorBuffer()
        }

        val waypointsTexture = renderTarget(width, height) {
            colorBuffer()
        }

        val pathTexture = renderTarget(width, height) {
            colorBuffer()
        }

        val gridW = width.toDouble() / heightMap.width
        val gridH = height.toDouble() / heightMap.height
        val mapDrawer =
            HeightMapDrawer(heightMap, gridW, gridH)

        drawer.isolatedWithTarget(mapTexture) {
            mapDrawer.draw(this)
        }

        launch {
            while(solver.queue.isNotEmpty()) {
                solver.step()

                drawer.isolatedWithTarget(waypointsTexture) {
                    solver.queue.forEach { pt ->
                        fill = ColorRGBa.BLACK
                        stroke = null
                        circle(pt.x * gridW + gridW / 2.0,pt.y * gridW + gridW / 2,2.0)
                    }
                }

                yield()
            }
        }

        launch {
            while(solver.solution == null) { yield() }

            val path = buildList<Point>{
                var currentPoint: Point? = solver.solution!!.first
                while(currentPoint != null) {
                    add(currentPoint)
                    currentPoint = solver.waypoints[currentPoint]?.prev
                }
            }

            path.forEach {
                val rect = mapDrawer.rect(it.x, it.y)

                drawer.isolatedWithTarget(pathTexture) {
                    fill = ColorRGBa.RED
                    stroke = null
                    rectangle(rect)
                }

                yield()
            }

            println("FINISH")
        }

        val composite = compose {
            draw {
                drawer.clear(ColorRGBa.BLACK)
            }

            layer {
                draw {
                    drawer.image(mapTexture.colorBuffer(0))
                }
            }

            layer {
                draw {
                    drawer.image(waypointsTexture.colorBuffer(0))
                }
            }

            layer {
                draw {
                    drawer.image(pathTexture.colorBuffer(0))
                }
            }
        }

        extend {
            composite.draw(drawer)
        }
    }
}
