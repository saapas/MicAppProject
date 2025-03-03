package com.example.micproject.record

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}