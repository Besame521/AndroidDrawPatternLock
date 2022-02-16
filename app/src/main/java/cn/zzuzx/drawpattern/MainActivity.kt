package cn.zzuzx.drawpattern

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.zzuzx.drawpattern.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.set.setOnClickListener {
            DrawPatternFragment().apply {
                setCallback = {
                    if (it.isNotEmpty()){
                        AppInfo.putGesturePattern(it)
                        Toast.makeText(this@MainActivity, "绘制解锁已设置", Toast.LENGTH_SHORT).show()
                    }
                }
                show(supportFragmentManager,"settingPattern")
            }
        }
        binding.check.setOnClickListener {
            if (AppInfo.getGesturePattern().isEmpty()){
                Toast.makeText(this@MainActivity, "未设置绘制解锁", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CheckDrawPatternFragment().apply {
                checkCallback = {
                    if (it){
                        //TODO 验证通过操作
                        Toast.makeText(this@MainActivity, "验证通过", Toast.LENGTH_SHORT).show()
                    }else {
                        //TODO 超过验证次数操作
                        Toast.makeText(this@MainActivity, "超过验证次数", Toast.LENGTH_SHORT).show()
                    }
                }
                forgetCallback = {
                    //TODO 忘记密码操作
                    Toast.makeText(this@MainActivity, "点击忘记密码", Toast.LENGTH_SHORT).show()
                }
                show(supportFragmentManager,"drawPattern")
            }
        }
        binding.clear.setOnClickListener {
            AppInfo.clear()
        }
    }
}