package com.dainghia.demoarcore

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.dainghia.demoarcore.databinding.ActivityMainBinding
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.scene.await
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView
    private val scene get() = arSceneView.scene
    private var modelName: String = "pikachu.glb"
    private var isShowList:Boolean = false
    private var model: Renderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listModel = listOf("pikachu.glb", "little_man.glb", "google.glb")

        val adapter = ModelListAdapter(listModel, object : ModelListAdapter.OnClickItemListener {
            override fun onClickItem(fileName: String) {
                modelName = fileName
                setVisibilityChooseModelMenu(false)
                lifecycleScope.launch {
                    loadModels()
                }
            }
        })

        val recyclerView: RecyclerView = binding.rvModels
        recyclerView.adapter = adapter

        binding.fab.setOnClickListener{
            setVisibilityChooseModelMenu(!isShowList)
            isShowList = !isShowList
        }

        arFragment = (supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment)

        arFragment.setOnTapArPlaneListener(::onTapPlane)

        lifecycleScope.launchWhenCreated {
            loadModels()
        }
    }

    private suspend fun loadModels() {
        Toast.makeText(this, "Loading $modelName", Toast.LENGTH_SHORT).show()
        model = ModelRenderable.builder()
            .setSource(this, Uri.parse("models/$modelName"))
            .setIsFilamentGltf(true)
            .await()

        Toast.makeText(this, "Loaded $modelName", Toast.LENGTH_SHORT).show()
    }

    private fun onTapPlane(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        if (model == null) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the Anchor.
        scene.addChild(AnchorNode(hitResult.createAnchor()).apply {
            // Create the transformable model and add it to the anchor.
            addChild(TransformableNode(arFragment.transformationSystem).apply {
                renderable = model
                renderableInstance.animate(true).start()
            })
        })
    }

    fun setVisibilityChooseModelMenu(show:Boolean){
        val transition: Transition = Slide()
        transition.duration = 200
        transition.addTarget(binding.rvModels)
        TransitionManager.beginDelayedTransition((binding.root as ViewGroup), transition)

        binding.rvModels.visibility = if(show) View.VISIBLE else View.INVISIBLE
        binding.overlay.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }
}