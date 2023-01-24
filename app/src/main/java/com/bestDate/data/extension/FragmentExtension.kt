package com.bestDate.data.extension

import android.view.View
import androidx.annotation.AnimRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bestDate.R

fun Fragment.open(fragment: Fragment, containerView: View, animationType: AnimationType = AnimationType.VERTICAL) {
    this.childFragmentManager.commit {
        setCustomAnimations(animationType.inAnimation, animationType.outAnimation)
        replace(containerView.id, fragment)
    }
}

inline fun Fragment.close(fragment: Fragment, containerView: View,
                          animationType: AnimationType = AnimationType.VERTICAL,
                          crossinline body: () -> Unit) {
    animationType.startAnimation(containerView)

    postDelayed({
        childFragmentManager.commit {
            fragment.let {
                remove(it)
                containerView.isVisible = false
                containerView.hideKeyboard()
                body()

                animationType.endAnimation(containerView)

                postDelayed({
                    containerView.isVisible = true
                }, 20)
            }
        }
    }, 350)
}

enum class AnimationType(@AnimRes val inAnimation: Int, @AnimRes val outAnimation: Int) {
    VERTICAL(R.anim.push_up_in, R.anim.push_up_out),
    HORIZONTAL(R.anim.slide_in_left, R.anim.slide_out_left),
    SCALE(R.anim.scale_in, R.anim.scale_out);

    fun startAnimation(containerView: View) {
        when(this) {
            VERTICAL -> {
                containerView.animate()
                    .translationY((containerView.height).toFloat())
                    .setDuration(300)
                    .start()
            }
            HORIZONTAL -> {
                containerView.animate()
                    .translationX((containerView.width).toFloat())
                    .setDuration(300)
                    .start()
            }
            SCALE -> {
                containerView.animate()
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .setDuration(300)
                    .start()
            }
        }
    }

    fun endAnimation(containerView: View) {
        when(this) {
            VERTICAL -> {
                containerView.animate()
                    .translationY(0.0f)
                    .setDuration(10)
                    .start()
            }
            HORIZONTAL -> {
                containerView.animate()
                    .translationX(0.0f)
                    .setDuration(10)
                    .start()
            }
            SCALE -> {
                containerView.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(10)
                    .start()
            }
        }
    }
}