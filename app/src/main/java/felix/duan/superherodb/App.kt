package felix.duan.superherodb

import android.app.Application
import android.content.Context
import felix.duan.superherodb.repo.LocalRepo

class App : Application() {

    companion object {
        private const val TAG = "SuperHeroApplication"
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        LocalRepo.init(this@App)
    }
}