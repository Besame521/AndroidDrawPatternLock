package cn.zzuzx.drawpattern

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.zzuzx.drawpattern.databinding.FragmentCheckPatternBinding

class CheckDrawPatternFragment : DialogFragment() {
    private lateinit var binding: FragmentCheckPatternBinding
    private var checkCount = 4
    var checkCallback: ((Boolean) -> Unit)? = null
    var forgetCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
        isCancelable = false
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckPatternBinding.inflate(inflater)
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
            handleCheck(str)
        }
        binding.forgetPass.setOnClickListener {
            binding.drawPatternView.handleClear()
            forgetCallback?.invoke()
        }
        dialog?.setOnKeyListener{ _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                true
            }
            false
        }
    }

    /**
     * 处理校验逻辑
     * @param str String
     */
    private fun handleCheck(str: String):Boolean {
        val res = str == AppInfo.getGesturePattern()
        if (res) {
            checkCallback?.invoke(true)
            dismissAllowingStateLoss()
            return true
        } else {
            checkCount--
            binding.notice.apply {
                text = "请输入解锁图案，您还有$checkCount 次机会"
                setTextColor(context.resources.getColor(R.color.draw_pattern_error))
            }
            if (checkCount <= 0) {
                // 当次数验证超过4次，需要重新登录
                checkCallback?.invoke(false)
            }
            return false
        }
    }
}