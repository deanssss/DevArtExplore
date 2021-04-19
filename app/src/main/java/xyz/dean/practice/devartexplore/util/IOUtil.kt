package xyz.dean.practice.devartexplore.util

import java.io.*

fun OutputStream.objectOutputStream(): ObjectOutputStream =
    ObjectOutputStream(this)

fun InputStream.objectInputStream(): ObjectInputStream =
    ObjectInputStream(this)

fun BufferedWriter.printWriter(): PrintWriter =
    PrintWriter(this)