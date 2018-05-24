package org.trustnote.wallet.biz.msgs

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.trustnote.wallet.TApp
import org.trustnote.wallet.biz.MainActivity
import org.trustnote.wallet.biz.wallet.WalletManager
import org.trustnote.wallet.uiframework.FragmentBase

abstract class FragmentMsgsBase : FragmentBase() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    //TODO: empty constructor.
    fun getMyActivity(): MainActivity {
        return activity as MainActivity
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.clear() //Empty the old menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {

        super.onResume()

//        val d = WalletManager.model.mWalletEventCenter.observeOn(AndroidSchedulers.mainThread()).subscribe {
//            updateUI()
//        }
//        disposables.add(d)

    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }


}

