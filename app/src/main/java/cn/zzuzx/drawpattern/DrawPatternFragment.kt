package cn.zzuzx.drawpattern

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.zzuzx.drawpattern.databinding.FragmentDrawPatternBinding

class DrawPatternFragment : DialogFragment() {
    private lateinit var binding: FragmentDrawPatternBinding
    private var resCache = ""
    var setCallback: ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrawPatternBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.back.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.drawPatternView.resCallback = { str, list ->
            handleSet(str, list)
        }
        binding.reset.setOnClickListener {
            binding.notice.apply {
                text = "请绘制解锁图案"
                setTextColor(context.resources.getColor(R.color.common_text))
            }
            binding.drawPatternView.handleClear()
            resCache = ""
            it.visibility = View.GONE
        }
    }

    /**
     * 处理设置逻辑
     * @param str String
     * @param list MutableList<Point>
     */
    private fun handleSet(str: String, list: MutableList<Int>):Boolean {
        binding.res.clearSelect()
        if (str.length < 4) {
            binding.notice.apply {
                text = "最少连接4个点，请重新绘制"
                setTextColor(context.resources.getColor(R.color.draw_pattern_error))
            }
            return true
        } else {
            if (resCache.isBlank()) {
                // 第一次绘制，缓存结果
                resCache = str
                binding.res.setSelect(list)
                binding.notice.apply {
                    text = "再次绘制解锁图案"
                    setTextColor(context.resources.getColor(R.color.common_text))
                }
                return true
            } else {
                // 第二次绘制，对比结果
                if (resCache == str) {
                    setCallback?.invoke(resCache)
                    // 绘制成功，关闭页面
                    dismissAllowingStateLoss()
                    return true
                } else {
                    binding.notice.apply {
                        text = "两次绘制不一样,请重新绘制"
                        setTextColor(context.resources.getColor(R.color.draw_pattern_error))
                    }
                    binding.reset.visibility = View.VISIBLE
                    return false
                }
            }
        }
    }
}