package cn.zzuzx.drawpattern

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class GestureView(context: Context) : View(context) {
    private var lineStartX = 0F
    private var lineStartY = 0F
    private var lineEndX = 0F
    private var lineEndY = 0F
    // 节点选中距离范围
    private val baseDistance = sqrt(80.dp2px.toDouble())
    private var isFinish = false
    private var isStart = false
    private lateinit var mPaint: Paint

    // 保存节点位置信息
    var pointList = mutableListOf<Point>()

    // 结果回调
    var dataCallback: ((data: Point?) -> Unit)? = null
    var clearCallback: (() -> Unit)? = null
    var finishCallback: (() -> Unit)? = null

    init {
        initView()
    }

    private fun initView() {
        mPaint = Paint()
        mPaint.apply {
            color = context.resources.getColor(R.color.common_blue)
            style = Paint.Style.FILL
            strokeWidth = 5F
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 清除结果记录
                    clearCallback?.invoke()
                    isFinish = false
                    isStart = false
                    lineStartX = event.x
                    lineStartY = event.y
                    lineEndX = event.x
                    lineEndY = event.y
                    // 触摸点坐标位置处理
                    handleMove(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    lineEndX = event.x
                    lineEndY = event.y
                    // 触摸点坐标位置处理
                    handleMove(event.x, event.y)
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    isFinish = true
                    // 触摸点坐标位置处理
                    handleMove(event.x, event.y)
                    finishCallback?.invoke()
                    // 重置当前节点的选中情况
                    pointList.forEach {
                        it.isSelected = false
                    }
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 绘制跟随手指的线条
        if (!isFinish && isStart) {
            canvas?.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, mPaint)
            Log.i("===","${canvas?.isHardwareAccelerated}")
        }
    }

    /**
     * 处理手指移动
     * @param x Float
     * @param y Float
     */
    private fun handleMove(x: Float, y: Float) {
        val point = eventToPointIndex(x, y)
        if (point != null) {
            // 当前坐标有节点被选中，则更新线段的起始坐标为该节点的中心
            if (!point.isSelected){
                lineStartX = point.x
                lineStartY = point.y
                point.isSelected = true
                isStart = true
                // 每次有节点选中都需要通知重绘线段
                dataCallback?.invoke(point)
            }
        }
        // 重新绘制
        invalidate()
    }

    /**
     * 将触摸点的坐标根据阈值（节点圆形的半径）转换为节点的坐标。
     * 计算逻辑为循环所有节点，计算与当前坐标的距离，如果小于半径，则判定当前节点激活。
     * @param x Float
     * @param y Float
     */
    private fun eventToPointIndex(x: Float, y: Float): Point? {
        for (item in pointList) {
            if(!item.isSelected) {
                val res = baseDistance - (sqrt(x - item.x) + sqrt(y - item.y))
                Log.i("===","$res")
                if (res >= 0) {
                    return item
                }
            }
        }
        return null
    }
}