package nktns.todo.main

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import nktns.todo.R
import nktns.todo.splash.SplashFragmentFirst
import nktns.todo.splash.SplashFragmentSecond

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getPreferences(MODE_PRIVATE)
        setContentView(R.layout.activity_main)
        Log.e(TAG, prefs.getBoolean("firstRun", true).toString())
        if (prefs.getBoolean("firstRun", true)) {
            showSplashScreenFirst()
            prefs.edit().putBoolean("firstRun", false).apply()
        } else if (savedInstanceState == null) {
            showMainScreen()
        }
    }

    fun showMainScreen() {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.animator.slide_from_right,
                0
            )
            .replace(R.id.fragment_container_view, MainFragment())
            .commit()
    }

    private fun showSplashScreenFirst() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, SplashFragmentFirst())
            .setCustomAnimations(
                0,
                R.animator.slide_in_left
            )
            .commit()
    }

    fun showSplashScreenSecond() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.slide_from_right,
                R.animator.slide_in_left
            )
            .replace(R.id.fragment_container_view, SplashFragmentSecond())
            .commit()
    }
}
