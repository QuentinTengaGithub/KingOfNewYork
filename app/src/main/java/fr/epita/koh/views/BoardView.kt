package fr.epita.koh.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import fr.epita.koh.R

/**
 * TODO: document your custom view class.
 */
class BoardView : View {

    private var boardTexture: Drawable? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.BoardView, defStyle, 0
        )

        val boardImg = resources.assets.open("newYorkMap.jpg");
        boardTexture = Drawable.createFromStream(boardImg, "newYorkMap");
        boardTexture?.callback = this
        boardImg.close();

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom


        // Draw the example drawable on top of the text.
        boardTexture?.let {
            it.setBounds(
                paddingLeft,
                paddingTop,
                paddingLeft + contentWidth,
                paddingTop + contentHeight
            )
            it.draw(canvas)
        }
    }
}