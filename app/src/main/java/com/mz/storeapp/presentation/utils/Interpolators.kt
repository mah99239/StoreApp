
package com.mz.storeapp.presentation.utils

import android.animation.TimeInterpolator
import androidx.core.view.animation.PathInterpolatorCompat

/**
 * Standard easing.
 *
 * Elements that begin and end at rest use standard easing. They speed up quickly and slow down
 * gradually, in order to emphasize the end of the transition.
 */
val FAST_OUT_SLOW_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0.4f, 0f, 0.2f, 1f)
}