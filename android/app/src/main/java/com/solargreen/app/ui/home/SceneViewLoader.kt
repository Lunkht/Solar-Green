package com.solargreen.app.ui.home

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

object SceneViewLoader {

    /** Share of the SceneView the house is allowed to occupy, per axis. */
    private const val VIEWPORT_FILL = 0.9f

    /** The camera never gets closer to the house than this multiple of its half depth. */
    private const val MIN_CAMERA_CLEARANCE = 1.2f

    /** Downward tilt in degrees, so the roof panels stay visible. */
    private const val CAMERA_ELEVATION = 8f

    private const val MIN_ZOOM = 0.5f
    private const val MAX_ZOOM = 1.8f

    @JvmStatic
    fun loadHouseModel(context: Context, sceneView: SceneView) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val modelInstance = sceneView.modelLoader.createModelInstance("solar_house.glb")
                    ?: sceneView.modelLoader.createModelInstance("House.glb")

                if (modelInstance == null) return@launch

                var zoom = 1f
                val modelNode = ModelNode(modelInstance = modelInstance)
                sceneView.addChildNode(modelNode)

                fun applyZoom() {
                    modelNode.scale = Float3(zoom)
                    centerOnBoundingBox(modelNode)
                }

                // The SceneView width depends on the screen, so reframe on every layout pass.
                sceneView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                    applyZoom()
                    frameModel(sceneView, modelNode)
                }

                setupTouchInteractions(context, sceneView, modelNode) { zoom = it }

                applyZoom()
                frameModel(sceneView, modelNode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Moves the house bounding box center onto the world origin, so it spins and zooms
     * around its own middle instead of around the glTF origin.
     */
    private fun centerOnBoundingBox(modelNode: ModelNode) {
        val center = modelNode.center
        val scale = modelNode.scale.x
        modelNode.position = Position(
            x = -center.x * scale,
            y = -center.y * scale,
            z = -center.z * scale
        )
    }

    /**
     * Pulls the camera back until the house fills [VIEWPORT_FILL] of the SceneView, whatever
     * the view aspect ratio is.
     *
     * For the projection matrix in use, a plane at a view depth of `d` spans `d / m00` and
     * `d / m11` world units around the camera axis. The closest point of the house sits at
     * `distance - halfDepth`, and that is the plane where the house looks biggest on screen.
     */
    private fun frameModel(sceneView: SceneView, modelNode: ModelNode) {
        if (sceneView.width <= 0 || sceneView.height <= 0) return

        val camera = sceneView.cameraNode
        camera.updateProjection()
        val projection = camera.projectionTransform
        val m00 = projection[0, 0]
        val m11 = projection[1, 1]
        if (m00 == 0f || m11 == 0f) return

        val elevation = CAMERA_ELEVATION.toRadians()
        val cosElevation = cos(elevation)
        val sinElevation = sin(elevation)

        // Half extents of the house, once expressed in the tilted camera frame.
        val extents = modelNode.extents * modelNode.scale.x
        val halfWidth = abs(extents.x) / 2f
        val halfHeight = (abs(extents.y) * cosElevation + abs(extents.z) * sinElevation) / 2f
        val halfDepth = (abs(extents.z) * cosElevation + abs(extents.y) * sinElevation) / 2f

        val distance = max(
            max(halfWidth * m00, halfHeight * m11) / VIEWPORT_FILL + halfDepth,
            halfDepth * MIN_CAMERA_CLEARANCE
        )

        camera.position = Position(
            x = 0f,
            y = distance * sinElevation,
            z = distance * cosElevation
        )
        camera.lookAt(Position())
    }

    private fun setupTouchInteractions(
        context: Context,
        sceneView: SceneView,
        modelNode: ModelNode,
        onZoomChanged: (Float) -> Unit
    ) {
        var previousX = 0f
        var previousY = 0f

        val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val newZoom = (modelNode.scale.x * detector.scaleFactor).coerceIn(MIN_ZOOM, MAX_ZOOM)
                onZoomChanged(newZoom)
                modelNode.scale = Float3(newZoom)
                centerOnBoundingBox(modelNode)
                return true
            }
        })

        sceneView.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)

            if (event.pointerCount == 1 && !scaleGestureDetector.isInProgress) {
                val x = event.x
                val y = event.y

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        previousX = x
                        previousY = y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = x - previousX
                        val currentRotationY = modelNode.rotation.y
                        modelNode.rotation = Float3(
                            modelNode.rotation.x,
                            currentRotationY + deltaX * 0.5f,
                            modelNode.rotation.z
                        )
                        previousX = x
                        previousY = y
                    }
                }
            }
            true
        }
    }

    private fun Float.toRadians() = this * kotlin.math.PI.toFloat() / 180f
}