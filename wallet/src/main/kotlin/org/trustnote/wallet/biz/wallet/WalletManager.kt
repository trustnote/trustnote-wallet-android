package org.trustnote.wallet.biz.wallet

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.trustnote.wallet.js.JSApi
import org.trustnote.wallet.util.Prefs


object WalletManager {

    //TODO: when create new model, should close all hub listener.
    val mWalletEventCenter: Subject<Boolean> = PublishSubject.create()

    lateinit var model: WalletModel
    init {
        if (Prefs.profileExist()) {
            model = WalletModel()
        }
    }

    fun getCurrentWalletDbTag(): String {
        return model.mProfile.dbTag
    }

    fun getProfile(): TProfile {
        return model.mProfile
    }

//    fun initWithMnemonic(removeMnemonic: Boolean) {
//        model = WalletModel(Prefs.getTmpMnemonic(), removeMnemonic)
//    }

    fun initWithMnemonic(mnemonic: String, removeMnemonic: Boolean, privKey: String = "") {
        if (Prefs.profileExist()) {
            model.destruct()
        }
        model = WalletModel(mnemonic, removeMnemonic, privKey)
    }

//    fun getTmpMnemonic(): String {
//        return Prefs.getTmpMnemonic()
//    }

    fun getOrCreateMnemonic(): String {
        return JSApi().mnemonicSync()
    }

    fun isExist(): Boolean {
        return Prefs.profileExist()
    }

}