package com.example.mrsmuarestaurant.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.mrsmuarestaurant.R
import com.example.mrsmuarestaurant.databinding.ActivityLoginBinding
import com.example.mrsmuarestaurant.Api.ApiConfig
import com.example.mrsmuarestaurant.helper.SharedPref
import com.example.mrsmuarestaurant.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var s: SharedPref

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        s = SharedPref(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        switchscreen()

        binding.btnRegister.setOnClickListener {
            register()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun register() {
            if (binding.edtSName.text.toString().isEmpty()) {
                binding.edtSName.error = "Tên không được để trống"
                binding.edtSName.requestFocus()
                return
            } else if (binding.edtSEmail.text.toString().isEmpty()) {
                binding.edtSEmail.error = "Email không được để trống"
                binding.edtSEmail.requestFocus()
                return
            } else if (binding.edtSPhone.text.toString().isEmpty()) {
                binding.edtSPhone.error = "Số điện thoại không được để trống"
                binding.edtSPhone.requestFocus()
                return
            } else if (binding.edtSPassword.text.toString().isEmpty()) {
                binding.edtSPassword.error = "Mật khẩu không được để trống"
                binding.edtSPassword.requestFocus()
                return
            }

            ApiConfig.instanceRetrofit.register(
                binding.edtSName.text.toString(),
                binding.edtSEmail.text.toString(),
                binding.edtSPhone.text.toString(),
                binding.edtSPassword.text.toString()
            ).enqueue(object : Callback<ResponModel> {

                override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error:" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    val respon = response.body()!!
                    if (respon.success == 1) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng ký thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error:" + respon.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

    private fun login() {
        if (binding.edtLEmail.text.toString().isEmpty()) {
            binding.edtLEmail.error = "Email không được để trống"
            binding.edtLEmail.requestFocus()
            return
        } else if (binding.edtLPassword.text.toString().isEmpty()) {
            binding.edtLPassword.error = "Mật khẩu không được để trống"
            binding.edtLPassword.requestFocus()
            return
        }


        ApiConfig.instanceRetrofit.login(
            binding.edtLEmail.text.toString(),
            binding.edtLPassword.text.toString()
        ).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                if (respon.success == 1) {
                    s.setStatusLogin(true)
                    s.setUser(respon.user)
                    s.setString(s.name, respon.user.name)
                    s.setString(s.email, respon.user.email)
                    s.setString(s.phone, respon.user.phone)
                    val intent = Intent(this@LoginActivity, BottomBarActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@LoginActivity, "Xin chào " + respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //Hiện ẩn nút đn đk
    @RequiresApi(Build.VERSION_CODES.M)
    private fun switchscreen(){
        binding.singUp.setOnClickListener{
            binding.singUp.background = resources.getDrawable(R.drawable.switch_trcks,null)
            binding.singUp.setTextColor(resources.getColor(R.color.textColor,null))
            binding.logIn.background = null
            binding.singUpLayout.visibility = View.VISIBLE
            binding.logInLayout.visibility = View.GONE
            binding.logIn.setTextColor(resources.getColor(R.color.colorPrimary,null))
        }
        binding.logIn.setOnClickListener {
            binding.singUp.background = null
            binding.singUp.setTextColor(resources.getColor(R.color.colorPrimary,null))
            binding.logIn.background = resources.getDrawable(R.drawable.switch_trcks,null)
            binding.singUpLayout.visibility = View.GONE
            binding.logInLayout.visibility = View.VISIBLE
            binding.logIn.setTextColor(resources.getColor(R.color.textColor,null))
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, BottomBarActivity::class.java))
        }
    }

}