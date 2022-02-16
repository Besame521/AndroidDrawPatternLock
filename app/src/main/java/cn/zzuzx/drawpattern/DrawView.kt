package cn.zzuzx.drawpattern

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import cn.zzuzx.drawpattern.R

/**
 * 对绘制选中节点的结果进行记录处理。
 * 动态改变节点状态和节点间连线的绘制。
 */
class DrawView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(
    context,
    attrs
) {
    private lateinit var mView: View
    private lateinit var mLineView: DrawLineView
    private lateinit var mDrawView: GestureView
    private var strBuilder = StringBuilder("")
    private var isError = false
    private val node = arrayOf(
        0, 1, 2,
        3, 4, 5,
        6, 7, 8
    )
    // 以下两个数组用来记录间隔选中节点的情况
    private val endNodes = arrayOf(
        arrayOf(2, 6, 8), arrayOf(7), arrayOf(0, 6, 8),
        arrayOf(5), arrayOf(), arrayOf(3),
        arrayOf(0, 2, 8), arrayOf(1), arrayOf(0, 2, 6)
    )
    private val midNodes = arrayOf(
        arrayOf(1, 2, 4), arrayOf(4), arrayOf(1, 4, 5),
        arrayOf(4), arrayOf(), arrayOf(4),
        arrayOf(3, 2, 7), arrayOf(4), arrayOf(4, 5, 7)
    )

    // 保存节点位置信息
    var pointList = mutableListOf<Point>()

    // 绘制结果
    private var resultOfIndex = mutableListOf<Int>()
    // 结果回调，当返回值为false时，会将已绘制的结点标红
    var resCallback: ((String, MutableList<Int>) -> Boolean)? = null

    private val nodeIds = arrayOf(
        R.id.draw_pattern_gesture_node1,
        R.id.draw_pattern_gesture_node2,
        R.id.draw_pattern_gesture_node3,
        R.id.draw_pattern_gesture_node4,
        R.id.draw_pattern_gesture_node5,
        R.id.draw_pattern_gesture_node6,
        R.id.draw_pattern_gesture_node7,
        R.id.draw_pattern_gesture_node8,
        R.id.draw_pattern_gesture_node9
    )

    init {
        initView()
    }

    private fun initView() {
        // 加载节点View
        mView = LayoutInflater.from(context).inflate(R.layout.view_draw_pattern_node, this, false)
        addView(mView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        // 加载直线绘制View
        mLineView = DrawLineView(context)
        addView(mLineView, LayoutParams(300.dp2px, 300.dp2px))
        // 加载手势处理View
        mDrawView = GestureView(context).apply {
            dataCallback = { point ->
                point?.let {
                    val index = it.index
                    if (index !in resultOfIndex) {
                        // 间隔选中，自动选中中间节点
                        val nodeIndexArray = endNodes[index]
                        if(resultOfIndex.isNotEmpty() && nodeIndexArray.contains(resultOfIndex.last())){
                            // 获取中间节点的index
                            val midIndex = midNodes[index][nodeIndexArray.indexOf(resultOfIndex.last())]
                            resultOfIndex.add(midIndex)
                            strBuilder.append(midIndex)
                            // 根据数据来控制节点的状态
                            mView.findViewById<View>(nodeIds[midIndex]).isSelected = true
                        }
                        resultOfIndex.add(index)
                        strBuilder.append(index)
                        // 根据数据通知线条绘制层进行线条绘制
                        mLineView.drawLine(resultOfIndex)
                        // 根据数据来控制节点的状态
                        mView.findViewById<View>(nodeIds[index]).isSelected = true
                    }
                }
            }
            clearCallback = {
                handleClear()
            }
            finishCallback = {
                // 返回结果
                val str = strBuilder.toString()
                var res = true
                if (str.isNotEmpty()) {
                    res = resCallback?.invoke(str, resultOfIndex) == true
                }
                // 处理验证错误变红逻辑
                // 当验证错误时，需要先标红再清理
                if (res) {
                    handleClear()
                    // 绘制结束，节点恢复为默认状态。
                    nodeIds.forEach {
                        mView.findViewById<View>(it).isSelected = false
                    }
                }else {
                    changeState()
                }
            }
        }
        addView(mDrawView, LayoutParams(300.dp2px, 300.dp2px))
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 初始化各个节点的坐标位置
        var index = 0
        for (row in 1..3) {
            val y = (2 * row - 1) * measuredHeight / 6F
            for (column in 1..3) {
                val x = (2 * column - 1) * measuredWidth / 6F
                pointList.add(Point(x, y, index++,false))
            }
        }
        mDrawView.pointList = pointList
        mLineView.pointList = pointList
    }

    /**
     * 清理逻辑
     */
    fun handleClear() {
        if (isError){
            resultOfIndex.forEach {
                mView.findViewById<View>(nodeIds[it]).background = context.resources.getDrawable(R.drawable.draw_pattern)
            }
            // 绘制结束，节点恢复为默认状态。
            nodeIds.forEach {
                mView.findViewById<View>(it).isSelected = false
            }
            isError = false
        }
        resultOfIndex.clear()
        mLineView.clear()
        strBuilder.setLength(0)
        invalidate()
    }

    /**
     *
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeState() {
        isError = true
        mLineView.changeLineColor(R.color.draw_pattern_error)
        resultOfIndex.forEach {
            mView.findViewById<View>(nodeIds[it]).background = context.resources.getDrawable(R.drawable.draw_pattern_error)
        }
        invalidate()
    }
}