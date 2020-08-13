/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.doublelift

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.Px

/** DoubleLiftLayout expands and collapses a layout horizontally and vertically sequentially. */
@Suppress("unused")
class DoubleLiftLayout
@JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

  @Px var foldedWidth: Int = 0
  @Px var foldedHeight: Int = 0
  @Px var liftedWith: Int = 0
  @Px var liftedHeight: Int = 0
  var isExpanded: Boolean = false
  var liftStartOrientation: LiftStartOrientation = LiftStartOrientation.HORIZONTAL
  var liftHorizontalDuration: Long = 500L
  var liftVerticalDuration: Long = 300L
  var liftAnimation: LiftAnimation = LiftAnimation.NORMAL
  var autoExpand: Boolean = false
  var autoCollapse: Boolean = true
  var onExpandListener: OnExpandListener? = null
  @Px var cornerRadius: Int = 4
    set(value) {
      field = value
      updateDoubleLifeLayout()
    }
  private var isLifting: Boolean = false

  init {
    getAttrs(attributeSet, defStyle)
  }

  private fun getAttrs(attributeSet: AttributeSet?, defStyleAttr: Int) {
    val typedArray = context.obtainStyledAttributes(
      attributeSet,
      R.styleable.DoubleLiftLayout,
      defStyleAttr,
      0
    )
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun setTypeArray(a: TypedArray) {
    this.foldedWidth =
      a.getDimensionPixelSize(
        R.styleable.DoubleLiftLayout_doubleLift_foldedWidth,
        this.foldedWidth
      )
    this.foldedHeight =
      a.getDimensionPixelSize(
        R.styleable.DoubleLiftLayout_doubleLift_foldedHeight,
        this.foldedHeight
      )
    this.cornerRadius =
      a.getDimensionPixelSize(
        R.styleable.DoubleLiftLayout_doubleLift_cornerRadius,
        this.cornerRadius
      )
    val liftStartOrientation =
      a.getInteger(
        R.styleable.DoubleLiftLayout_doubleLift_startOrientation,
        this.liftStartOrientation.value
      )
    when (liftStartOrientation) {
      LiftStartOrientation.HORIZONTAL.value -> this.liftStartOrientation =
        LiftStartOrientation.HORIZONTAL
      LiftStartOrientation.VERTICAL.value -> this.liftStartOrientation =
        LiftStartOrientation.VERTICAL
    }
    val animation =
      a.getInteger(
        R.styleable.DoubleLiftLayout_doubleLift_animation,
        this.liftAnimation.value
      )
    when (animation) {
      LiftAnimation.NORMAL.value -> this.liftAnimation = LiftAnimation.NORMAL
      LiftAnimation.ACCELERATE.value -> this.liftAnimation = LiftAnimation.ACCELERATE
      LiftAnimation.BOUNCE.value -> this.liftAnimation = LiftAnimation.BOUNCE
      LiftAnimation.OVERSHOOT.value -> this.liftAnimation = LiftAnimation.OVERSHOOT
    }

    this.liftHorizontalDuration =
      a.getInt(
        R.styleable.DoubleLiftLayout_doubleLift_horizontalDuration,
        this.liftHorizontalDuration.toInt()
      ).toLong()
    this.liftVerticalDuration =
      a.getInteger(
        R.styleable.DoubleLiftLayout_doubleLift_verticalDuration,
        this.liftVerticalDuration.toInt()
      ).toLong()
    this.autoExpand =
      a.getBoolean(R.styleable.DoubleLiftLayout_doubleLift_autoExpand, this.autoExpand)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    initializeBackground()
    updateDoubleLifeLayout()
  }

  private fun initializeBackground() {
    if (this.background is ColorDrawable) {
      this.background = GradientDrawable().apply {
        cornerRadius = this@DoubleLiftLayout.cornerRadius.toFloat()
        setColor((background as ColorDrawable).color)
      }
    }
  }

  private fun updateDoubleLifeLayout() {
    post {
      this.liftedWith = width
      this.liftedHeight = height
      updateLayoutParams {
        width = foldedWidth
        height = foldedHeight
      }
      setVisibilityChildren(false)
      if (this.autoExpand) {
        expand()
      }
    }
  }

  /**
   * This functionality is for supporting Java language.
   * Expand the width and height size sequentially.
   */
  fun expand() = expand { }

  /** Expand the width and height size sequentially. */
  fun expand(doAfterLift: () -> Unit) {
    post {
      if (!this.isExpanded && !this.isLifting) {
        this.isExpanded = true
        this.isLifting = true
        lift(doAfterLift)
      }
    }
  }

  /**
   * This functionality is for supporting Java language.
   * Collapse the width and height size sequentially.
   */
  fun collapse() = collapse { }

  /** Collapse the width and height size sequentially. */
  fun collapse(doAfterLift: () -> Unit) {
    post {
      if (this.isExpanded && !this.isLifting) {
        this.isExpanded = false
        this.isLifting = true
        liftReverse(doAfterLift)
      }
    }
  }

  private fun lift(doAfterLift: () -> Unit = {}) {
    setVisibilityChildren(false)
    when (this.liftStartOrientation) {
      LiftStartOrientation.HORIZONTAL -> liftHorizontal(VOID, FULLY) {
        liftVertical(VOID, FULLY) { doAfter(doAfterLift) }
      }
      LiftStartOrientation.VERTICAL -> liftVertical(VOID, FULLY) {
        liftHorizontal(VOID, FULLY) { doAfter(doAfterLift) }
      }
    }
  }

  private fun liftReverse(doAfterLift: () -> Unit = {}) {
    setVisibilityChildren(false)
    when (this.liftStartOrientation) {
      LiftStartOrientation.HORIZONTAL -> liftVertical(FULLY, VOID) {
        liftHorizontal(FULLY, VOID) { doAfter(doAfterLift) }
      }
      LiftStartOrientation.VERTICAL -> liftHorizontal(FULLY, VOID) {
        liftVertical(FULLY, VOID) { doAfter(doAfterLift) }
      }
    }
  }

  private fun doAfter(doAfterLift: () -> Unit = {}) {
    this.onExpandListener?.onExpand(this.isExpanded)
    this.isLifting = false
    setVisibilityChildren(true)
    doAfterLift()
  }

  private fun liftHorizontal(liftStart: Float, liftEnd: Float, doAfterLift: () -> Unit) {
    ValueAnimator.ofFloat(liftStart, liftEnd).apply {
      duration = liftHorizontalDuration
      doAfterFinishLift { doAfterLift() }
      applyInterpolator(liftAnimation)
      addUpdateListener {
        val value = it.animatedValue as Float
        updateLayoutParams {
          when (liftStart) {
            VOID -> {
              var target = foldedWidth + (liftedWith * value).toInt()
              if (target >= liftedWith) target = liftedWith
              width = target
            }
            FULLY -> {
              var target = (liftedWith * value).toInt()
              if (target <= foldedWidth) target = foldedWidth
              width = target
            }
          }
        }
      }
      start()
    }
  }

  private fun liftVertical(liftStart: Float, liftEnd: Float, doAfterLift: () -> Unit) {
    ValueAnimator.ofFloat(liftStart, liftEnd).apply {
      duration = liftVerticalDuration
      doAfterFinishLift { doAfterLift() }
      applyInterpolator(liftAnimation)
      addUpdateListener {
        val value = it.animatedValue as Float
        updateLayoutParams {
          var target = foldedHeight + (liftedHeight * value).toInt()
          if (target >= liftedHeight) target = liftedHeight
          height = target
        }
      }
      start()
    }
  }

  private fun setVisibilityChildren(visible: Boolean) {
    for (index in 0 until childCount) {
      getChildAt(index).visible(visible)
    }
  }

  /** Sets an [OnExpandListener] using a lambda. */
  fun setOnExpandListener(block: (Boolean) -> Unit) {
    this.onExpandListener = object : OnExpandListener {
      override fun onExpand(isExpanded: Boolean) {
        block(isExpanded)
      }
    }
  }

  /** Builder class for creating [DoubleLiftLayout]. */
  @DoubleLiftLayoutDsl
  class Builder(context: Context) {
    private val doubleLiftLayout = DoubleLiftLayout(context)

    fun setFoldedWidth(@Px value: Int) = apply { this.doubleLiftLayout.foldedWidth = value }
    fun setFoldedHeight(@Px value: Int) = apply { this.doubleLiftLayout.foldedHeight = value }
    fun setCornerRadius(@Px value: Int) = apply { this.doubleLiftLayout.cornerRadius = value }
    fun setLiftStartOrientation(value: LiftStartOrientation) =
      apply { this.doubleLiftLayout.liftStartOrientation = value }

    fun setLiftHorizontalDuration(value: Long) =
      apply { this.doubleLiftLayout.liftHorizontalDuration = value }

    fun setLiftVerticalDuration(value: Long) =
      apply { this.doubleLiftLayout.liftVerticalDuration = value }

    fun setLiftAnimation(value: LiftAnimation) =
      apply { this.doubleLiftLayout.liftAnimation = value }

    fun setAutoDoubleLift(value: Boolean) = apply { this.doubleLiftLayout.autoExpand = value }
    fun setAutoCollapse(value: Boolean) = apply { this.doubleLiftLayout.autoCollapse = value }
    fun setOnExpandListener(value: OnExpandListener) =
      apply { this.doubleLiftLayout.onExpandListener = value }

    fun setOnExpandListener(block: (Boolean) -> Unit) = apply {
      this.doubleLiftLayout.onExpandListener = object : OnExpandListener {
        override fun onExpand(isExpanded: Boolean) {
          block(isExpanded)
        }
      }
    }

    fun build() = this.doubleLiftLayout
  }
}
