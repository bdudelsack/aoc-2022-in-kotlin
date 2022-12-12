package day12_2

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
import org.openrndr.extra.timer.repeat
import org.openrndr.extra.videoprofiles.gif
import org.openrndr.extra.videoprofiles.h265
import org.openrndr.extra.videoprofiles.prores
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.shape.Rectangle
import utils.map.Map2D
import utils.readInput

fun main() = application {
    configure {
        width = 830
        height = 410
    }

    program {
        extend(ScreenRecorder()) {
            prores()
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

        class WaypointsDrawer(map: Map2D<Day12_2Solver.Waypoint?>, gridW: Double = 20.0, gridH: Double = 20.0): AbstractMapDrawer<Day12_2Solver.Waypoint?>(map, gridW, gridH) {
            override fun drawCell(drawer: Drawer, rect: Rectangle, value: Day12_2Solver.Waypoint?) {
                value?.run {
                    drawer.stroke = null
                    drawer.fill = ColorRGBa.BLACK
                    drawer.circle(rect.center, 2.0)
                }
            }
        }


        val mapTexture = renderTarget(width, height) {
            colorBuffer()
        }

        val mapDrawer =
            HeightMapDrawer(heightMap, width.toDouble() / heightMap.width, height.toDouble() / heightMap.height)

        val waypointsDrawer = WaypointsDrawer(solver.waypoints, width.toDouble() / heightMap.width, height.toDouble() / heightMap.height)

        drawer.isolatedWithTarget(mapTexture) {
            mapDrawer.draw(this)
        }

        repeat(0.04) {
            if(solver.queue.isNotEmpty()) {
                solver.step()
            }

            drawer.isolatedWithTarget(mapTexture) {
                waypointsDrawer.draw(this)

                if(solver.solution != null) {
                    stroke = null
                    fill = ColorRGBa.RED
                    rectangle(waypointsDrawer.rect(solver.solution!!.first.x, solver.solution!!.first.y))
                }
            }
        }

        extend {
            drawer.image(mapTexture.colorBuffer(0))
        }
    }
}
