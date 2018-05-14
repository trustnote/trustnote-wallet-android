package org.trustnote.wallet.biz.wallet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import org.trustnote.wallet.uiframework.BaseActivity
import org.trustnote.wallet.util.AndroidUtils
import org.trustnote.wallet.*


class CreateWalletActivity : BaseActivity() {

    override fun injectDependencies(graph: TApplicationComponent) {
    }

    private lateinit var mPager: ViewPager
    private lateinit var mPagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (CreateWalletModel.getCreationProgress() == CREATE_WALLET_STATUS.FINISHED) {
            finish()
            startMainActivityWithMenuId(R.id.action_home)
        }

        setupUISettings()

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_create_wallet)

        mPager = findViewById(R.id.view_pager)
        mPagerAdapter = PagerAdapter(supportFragmentManager, mPager!!)

        mPager.adapter = mPagerAdapter

        val pageChangeListener = object : OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

        }

        AndroidUtils.hideStatusBar(this, true)

    }

    companion object {
        @JvmStatic
        fun startMe() {
            val intent = Intent(TApp.context, CreateWalletActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            TApp.context.startActivity(intent)
        }
    }


    fun adjustUIBySetting(pageSetting: PageSetting) {
        AndroidUtils.hideStatusBar(this, !pageSetting.showStatusBar)
        findViewById<View>(R.id.titlebar_back_arrow).visibility = (if (pageSetting.showBackArrow) View.VISIBLE else View.INVISIBLE)
    }
}

class PagerAdapter(fm: FragmentManager, private val pager: ViewPager) : FragmentStatePagerAdapter(fm) {
    private var allPageLayoutIds: List<Int> = allAllPageIds()
    private val cacheFragement: MutableMap<Int, CreateWalletFragment> = mutableMapOf()

    override fun getItem(position: Int): Fragment {
        val f = CreateWalletFragment(allPageLayoutIds[position], pager)

        cacheFragement[position] = f
        return f
    }

    override fun getCount(): Int {
        return allPageLayoutIds.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any ) {
        cacheFragement[position]!!.onShowPage()
    }
}
