package com.example

import kotlin.math.cos
import kotlin.math.sin

val width = 160
val height = 44
val buffer = CharArray(width * height) { ' ' }
val zBuffer = DoubleArray(width * height) { 0.0 }
val backgroundASCII = ' '

var A = 0.0f
var B = 0.0f
var C = 0.0f

const val cubeWidth = 20f
const val incrementSpeed = 1f
const val distanceFromCam = 60
const val k1 = 40f

fun main() {
    print("\u001b[2J")

    while (true) {
        buffer.fill(backgroundASCII)
        zBuffer.fill(0.0)

        var cubeX = -cubeWidth
        while (cubeX < cubeWidth) {
            cubeX += incrementSpeed
            var cubeY = -cubeWidth
            while (cubeY < cubeWidth) {
                cubeY += incrementSpeed
                calculateSurface(cubeX, cubeY, -cubeWidth, '.')
                calculateSurface(cubeWidth, cubeY, cubeX, '$')
                calculateSurface(-cubeWidth, cubeY, cubeX, '~')
                calculateSurface(-cubeX, cubeY, cubeWidth, '#')
                calculateSurface(cubeX, -cubeWidth, -cubeY, ';')
                calculateSurface(cubeX, cubeWidth, cubeY, '-')
            }
        }

        print("\u001b[H") // Move cursor to top-left
        for (k in buffer.indices) {
            if (k % width == 0 && k != 0) print("\n")
            print(buffer[k])
        }
        print("\u001b[H")

        A += 0.05f
        B += 0.05f

        Thread.sleep(16)
    }
}

fun calculateSurface(cubeX: Float, cubeY: Float, cubeZ: Float, ch: Char) {
    val sinA = sin(A)
    val cosA = cos(A)
    val sinB = sin(B)
    val cosB = cos(B)

    val x = cubeY * sinA * sinB * cos(C) - cubeZ * cosA * sinB * cos(C) + cubeY * cosA * sin(C) + cubeZ * sinA * sin(C) + cubeX * cosB * cos(C)
    val y = cubeY * cosA * cos(C) + cubeZ * sinA * cos(C) - cubeY * sinA * sinB * sin(C) + cubeZ * cosA * sinB * sin(C) - cubeX * cosB * sin(C)
    val z = cubeZ * cosA * cosB - cubeY * sinA * cosB + cubeX * sinB + distanceFromCam

    val ooz = 1.0 / z

    val xp = (width / 2 + k1 * ooz * x * 2).toInt()
    val yp = (height / 2 + k1 * ooz * y).toInt()

    val idx = xp + yp * width
    if (idx in buffer.indices && ooz > zBuffer[idx]) {
        zBuffer[idx] = ooz
        buffer[idx] = ch
    }
}
