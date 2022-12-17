package com.example.leagueoftbcoupon.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.leagueoftbcoupon.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }


    private fun initView() {
        val fragmentContainer = findNavController(R.id.taobao_fragment_container_view)
        mainBottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.fragment_home->{
                    fragmentContainer.navigate(R.id.fragment_home)

                }

//                R.id.fragment_selected->{
//                    fragmentContainer.navigate(R.id.fragment_selected)
//                }

                R.id.fragment_on_sell->{
                    fragmentContainer.navigate(R.id.fragment_on_sell)
                }

                R.id.fragment_search->{
                    fragmentContainer.navigate(R.id.fragment_search)
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}