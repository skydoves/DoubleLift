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

/** DoubleLiftLayout expands and collapses a layout's horizontal and vertical sequentially. */
@Suppress("unused")
class DoubleLiftLayout : FrameLayout {

  var foldedWidth: Int = 0
  var foldedHeight: Int = 0
  var liftedWith: Int = 0
  var liftedHeight: Int = 0
  var isExpanded: Boolean = false
  var liftStartOrientation: LiftStartOrientation = LiftStartOrientation.HORIZONTAL
  var liftHorizontalDuration: Long = 500L
  var liftVerticalDuration: Long = 300L
  var autoExpand: Boolean = false
  var autoCollapse: Boolean = true
  var onExpandListener: OnExpandListener? = null
  var cornerRadius: Int = 4
    set(value) {
      field = value
      updateDoubleLifeLayout()
    }
  private var isLifting: Boolean = false

  constructor(context: Context) : super(context)

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    getAttrs(attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
    getAttrs(attributeSet, defStyle)
  }

  private fun getAttrs(attributeSet: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DoubleLiftLayout)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun getAttrs(attributeSet: AttributeSet, defStyleAttr: Int) {
    val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DoubleLiftLayout, defStyleAttr, 0)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun setTypeArray(a: TypedArray) {
    this.foldedWidth =
      a.getDimensionPixelSize(R.styleable.DoubleLiftLayout_doubleLift_foldedWidth, this.foldedWidth)
    this.foldedHeight =
      a.getDimensionPixelSize(R.styleable.DoubleLiftLayout_doubleLift_foldedHeight, this.foldedHeight)
    this.cornerRadius =
      a.getDimensionPixelSize(R.styleable.DoubleLiftLayout_doubleLift_cornerRadius, this.cornerRadius)
    val liftStartOrientation =
      a.getInteger(R.styleable.DoubleLiftLayout_doubleLift_startOrientation, this.liftStartOrientation.value)
    when (liftStartOrientation) {
      LiftStartOrientation.HORIZONTAL.value -> this.liftStartOrientation = LiftStartOrientation.HORIZONTAL
      LiftStartOrientation.VERTICAL.value -> this.liftStartOrientation = LiftStartOrientation.VERTICAL
    }
    this.liftHorizontalDuration =
      a.getInt(R.styleable.DoubleLiftLayout_doubleLift_horizontalDuration, this.liftHorizontalDuration.toInt()).toLong()
    this.liftVerticalDuration =
      a.getInteger(R.styleable.DoubleLiftLayout_doubleLift_verticalDuration, this.liftVerticalDuration.toInt()).toLong()
    this.autoExpand =
      a.getBoolean(R.styleable.DoubleLiftLayout_doubleLift_autoExpand, this.autoExpand)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    initializeBackground()
    updateDoubleLifeLayout()
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

  private fun initializeBackground() {
    if (this.background is ColorDrawable) {
      val gradientDrawable = GradientDrawable()
      gradientDrawable.cornerRadius = this.cornerRadius.toFloat()
      gradientDrawable.setColor((this.background as ColorDrawable).color)
      this.background = gradientDrawable
    }
  }

  /** Expand the width and height size sequentially. */
  fun expand(doAfterLift: () -> Unit = {}) {
    post {
      if (!this.isExpanded && !this.isLifting) {
        this.isExpanded = true
        this.isLifting = true
        lift(doAfterLift)
      }
    }
  }

  /** Collapse the width and height size sequentially. */
  fun collapse(doAfterLift: () -> Unit = {}) {
    post {
      if (this.isExpanded && !this.isLifting) {
        this.isExpanded = false
        this.isLifting = true
        liftReverse(doAfterLift)
      }
    }
  }

  private fun lift(doAfterLift: () -> Unit = {}) {
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
    val animator = ValueAnimator.ofFloat(liftStart, liftEnd)
    animator.duration = this.liftHorizontalDuration
    animator.addUpdateListener {
      val value = it.animatedValue as Float
      this.updateLayoutParams {
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
      value.doAfterFinishLift(liftEnd) { doAfterLift() }
    }
    animator.start()
  }

  private fun liftVertical(liftStart: Float, liftEnd: Float, doAfterLift: () -> Unit) {
    val animator = ValueAnimator.ofFloat(liftStart, liftEnd)
    animator.duration = this.liftVerticalDuration
    animator.addUpdateListener {
      val value = it.animatedValue as Float
      this.updateLayoutParams {
        var target = foldedHeight + (liftedHeight * value).toInt()
        if (target >= liftedHeight) target = liftedHeight
        height = target
      }
      value.doAfterFinishLift(liftEnd) { doAfterLift() }
    }
    animator.start()
  }

  private fun Float.doAfterFinishLift(liftLast: Float, doAfterLift: () -> Unit) {
    if (this == liftLast) {
      doAfterLift()
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

  @DoubleLiftLayoutDsl
  class Builder(context: Context) {
    private val doubleLiftLayout = DoubleLiftLayout(context)

    fun setFoldedWidth(value: Int) = apply { this.doubleLiftLayout.foldedWidth = value }
    fun setFoldedHeight(value: Int) = apply { this.doubleLiftLayout.foldedHeight = value }
    fun setCornerRadius(value: Int) = apply { this.doubleLiftLayout.cornerRadius = value }
    fun setLiftStartOrientation(value: LiftStartOrientation) = apply { this.doubleLiftLayout.liftStartOrientation = value }
    fun setLiftHorizontalDuration(value: Long) = apply { this.doubleLiftLayout.liftHorizontalDuration = value }
    fun setLiftVerticalDuration(value: Long) = apply { this.doubleLiftLayout.liftVerticalDuration = value }
    fun setAutoDoubleLift(value: Boolean) = apply { this.doubleLiftLayout.autoExpand = value }
    fun setAutoCollapse(value: Boolean) = apply { this.doubleLiftLayout.autoCollapse = value }
    fun setOnExpandListener(value: OnExpandListener) = apply { this.doubleLiftLayout.onExpandListener = value }
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
