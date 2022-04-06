package com.binar.roomchapter6.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.roomchapter6.AppExecutors
import com.binar.roomchapter6.R
import com.binar.roomchapter6.StudentAdapter
import com.binar.roomchapter6.room.StudentDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var mDB : StudentDatabase? = null

    private var mExecutors: AppExecutors? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mExecutors = AppExecutors()

        mDB = StudentDatabase.getInstance(this)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        fetchData()

        fabAdd.setOnClickListener {
            val keActivityAdd = Intent(this, AddActivity::class.java)
            startActivity(keActivityAdd)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    fun fetchData(){
        mExecutors?.diskIO()?.execute {
            Log.d("MainActivity", Thread.currentThread().name)
            val listStudent = mDB?.studentDao()?.getAllStudent()

            mExecutors?.mainThread()?.execute {
                listStudent?.let {
                    Log.d("MainActivity", Thread.currentThread().name)
                    val adapter = StudentAdapter(it)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StudentDatabase.destroyInstance()
    }
}