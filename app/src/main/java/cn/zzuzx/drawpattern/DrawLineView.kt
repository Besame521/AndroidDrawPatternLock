package cn.zzuzx.drawpattern

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.annotation.ColorRes

/**
 * 该类用来绘制已连接的线条，位于第二层
 * @property mPaint Paint
 * @property result MutableList<Point>
 * @constructor
 */
class DrawLineView(context: Context) : View(context) {
    private lateinit var mPaint: Paint
    private var result = mutableListOf<Int>()
    // 保存节点位置信息
    var pointList = mutableListOf<Point>()

    init {
        initPaint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (result.size > 1) {
            var stPoint = pointList[result[0]]
            for (item in 1 until result.size) {
                canvas?.drawLine(stPoint.x, stPoint.y, pointList[result[item]].x, pointList[result[item]].y, mPaint)
                stPoint = pointList[result[item]]
            }
        }
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        mPaint = Paint().apply {
            color = context.resources.getColor(R.color.common_blue)
            style = Paint.Style.FILL
            strokeWidth = 7F
            strokeJoin = Paint.Join.ROUND
        }
    }

    fun drawLine(data:MutableList<Int>){
        result = data
        invalidate()
    }

    /**
     * 修改线条颜色
     * @param colorId Int
     */
    fun changeLineColor(@ColorRes colorId:Int){
        mPaint.color = context.resources.getColor(colorId)
        invalidate()
    }

    fun clear(){
        mPaint.color = context.resources.getColor(R.color.common_blue)
        result.clear()
        invalidate()
    }
}