package varunbehl.algebra.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import varunbehl.algebra.R

class CustomLinearLayout : LinearLayout {
    private var curveDimension = 0f
    private var normalColor = resources.getColor(R.color.colorPrimary)
    private var gradientOrientation: Int = 0

    private var pressedColor = resources.getColor(R.color.colorPrimaryDark)

    fun setPressedColor(pressedColor: Int) {
        this.pressedColor = pressedColor
        setBg()
    }

    fun setNormalColor(normalColor: Int) {
        this.normalColor = normalColor
        setBg()
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun setGradientColorWithRadius(orientation: GradientDrawable.Orientation, primaryColor: Int, primaryDarkColor: Int, cardCornerRadius: Float) {
        val gradientDrawable = GradientDrawable(orientation, intArrayOf(primaryColor, primaryDarkColor))
        gradientDrawable.cornerRadius = cardCornerRadius
        setBackgroundDrawable(gradientDrawable)
    }

    fun setGradientColor(orientation: GradientDrawable.Orientation, primaryColor: Int, primaryDarkColor: Int) {
        val gradientDrawable = GradientDrawable(orientation, intArrayOf(primaryColor, primaryDarkColor))
        setBackgroundDrawable(gradientDrawable)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.CustomLinearLayout, defStyle, 0)

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        curveDimension = a.getDimension(
                R.styleable.CustomLinearLayout_curveDimension,
                curveDimension)
        normalColor = a.getColor(R.styleable.CustomLinearLayout_normalColor, normalColor)
        pressedColor = a.getColor(R.styleable.CustomLinearLayout_pressedColor, pressedColor)
        val gradientStart = a.getColor(R.styleable.CustomLinearLayout_gradient_start, resources.getColor(R.color.colorPrimary))
        val gradientEnd = a.getColor(R.styleable.CustomLinearLayout_gradient_end, resources.getColor(R.color.colorPrimaryDark))
        gradientOrientation = a.getInt(R.styleable.CustomLinearLayout_gradient_orientation, 6)
        val cornerRadius = a.getDimension(R.styleable.CustomLinearLayout_corner_radius, resources.getDimension(R.dimen.zero))

        a.recycle()
        setBg()
        if (gradientStart != resources.getColor(R.color.colorPrimary) || gradientEnd != resources.getColor(R.color.colorPrimaryDark)) {
            setGradientColorWithRadius(GradientDrawable.Orientation.LEFT_RIGHT, gradientStart, gradientEnd, cornerRadius)
        }

    }

    private fun setBg() {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed),
                getRectDrawable(curveDimension, pressedColor))
        states.addState(intArrayOf(android.R.attr.state_focused),
                getRectDrawable(curveDimension, pressedColor))
        states.addState(intArrayOf(),
                getRectDrawable(curveDimension, normalColor))
        this.background = states
    }

    fun getGradientOrientation(gradientOrientation: Int): GradientDrawable.Orientation? {
        when (gradientOrientation) {
            0 ->
                /**
                 * draw the gradient from the top to the bottom
                 */
                return GradientDrawable.Orientation.TOP_BOTTOM
            1 ->
                /**
                 * draw the gradient from the top-right to the bottom-left
                 */
                return GradientDrawable.Orientation.TR_BL
            2 ->
                /**
                 * draw the gradient from the right to the left
                 */
                return GradientDrawable.Orientation.RIGHT_LEFT
            3 ->

                /**
                 * draw the gradient from the bottom-right to the top-left
                 */
                return GradientDrawable.Orientation.BR_TL
            4 ->
                /**
                 * draw the gradient from the bottom to the top
                 */
                return GradientDrawable.Orientation.BOTTOM_TOP
            5 ->
                /**
                 * draw the gradient from the bottom-left to the top-right
                 */
                return GradientDrawable.Orientation.BL_TR
            6 ->
                /**
                 * draw the gradient from the left to the right
                 */
                return GradientDrawable.Orientation.LEFT_RIGHT
            7 ->
                /**
                 * draw the gradient from the top-left to the bottom-right
                 */
                return GradientDrawable.Orientation.TL_BR
        }
        return null
    }

    fun getCurveDimension(): Float {
        return curveDimension
    }

    fun setCurveDimension(curveDimension: Float) {
        this.curveDimension = curveDimension
        setBg()
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    companion object {

        fun getRectDrawable(radius: Float, color: Int): Drawable {
            val drawable = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(color, color))
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadius = radius
            return drawable
        }

        fun getRectDrawable(radii: FloatArray, color: Int): Drawable {
            val drawable = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(color, color))
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadii = radii
            return drawable
        }

        fun getRectDrawable(radius: Float, colors: IntArray): Drawable {
            val drawable = GradientDrawable(GradientDrawable.Orientation.TL_BR, colors)
            drawable.shape = GradientDrawable.RECTANGLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
            drawable.cornerRadius = radius
            return drawable
        }

        fun getCircularDrawable(color: Int, alpha: Int): Drawable {
            val drawable = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(color, color))
            drawable.shape = GradientDrawable.OVAL
            drawable.alpha = alpha
            return drawable
        }
    }
}