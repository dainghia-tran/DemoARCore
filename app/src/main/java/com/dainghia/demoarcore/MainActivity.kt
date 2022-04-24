package com.dainghia.demoarcore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.ux.ArFragment
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arFragment = (supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment)

        arFragment.setOnTapPlaneGlbModel("models/pikachu.glb")
    }
}