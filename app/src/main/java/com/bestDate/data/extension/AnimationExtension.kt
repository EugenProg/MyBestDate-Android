package com.bestDate.data.extension

import android.animation.Animator
import android.animation.AnimatorSet

/**
 * An extension to listen to animation is end
 * */
fun AnimatorSet.setupOnListener(
    start: ((Animator?) -> Unit)? = null,
    end: ((Animator?) -> Unit)? = null,
    cancel: ((Animator?) -> Unit)? = null,
    repeat: ((Animator?) -> Unit)? = null,
    ) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            start?.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animator?) {
            end?.invoke(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
            cancel?.invoke(animation)
        }

        override fun onAnimationRepeat(animation: Animator?) {
            repeat?.invoke(animation)
        }
    })
}