package cn.zzuzx.drawpattern

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import cn.zzuzx.drawpattern.databinding.ViewDrawPatternResNodeBinding

class DrawResView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(
    context,
    attrs
)  {
    private lateinit var binding: ViewDrawPatternResNodeBinding

    private val nodeIds = arrayOf(
        R.id.gray_ring_gesture_node1,
        R.id.gray_ring_gesture_node2,
        R.id.gray_ring_gesture_node3,
        R.id.gray_ring_gesture_node4,
        R.id.gray_ring_gesture_node5,
        R.id.gray_ring_gesture_node6,
        R.id.gray_ring_gesture_node7,
        R.id.gray_ring_gesture_node8,
        R.id.gray_ring_gesture_node9
    )
    init {
        initView()
    }

    private fun initView(){
        binding = ViewDrawPatternResNodeBinding.inflate(LayoutInflater.from(context),this,false)
        addView(binding.root, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    /**
     * 新增选中状态
     * @param res Int
     */
    fun setSelect(res:MutableList<Int>){
        val newList = mutableListOf<Int>()
        newList.addAll(res)
        newList.forEach {
            binding.root.findViewById<View>(nodeIds[it])?.isSelected = true
        }
    }

    /**
     * 清理所有的选中状态
     */
    fun clearSelect(){
        nodeIds.forEach {
            binding.root.findViewById<View>(it)?.isSelected = false
        }
    }
}