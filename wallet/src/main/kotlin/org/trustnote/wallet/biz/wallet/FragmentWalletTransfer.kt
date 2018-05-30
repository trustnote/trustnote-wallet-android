package org.trustnote.wallet.biz.wallet

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.trustnote.wallet.R
import org.trustnote.wallet.TApp
import org.trustnote.wallet.biz.MainActivity
import org.trustnote.wallet.biz.units.UnitComposer
import org.trustnote.wallet.uiframework.BaseActivity
import org.trustnote.wallet.util.AndroidUtils
import org.trustnote.wallet.util.MyThreadManager
import org.trustnote.wallet.util.TTTUtils
import org.trustnote.wallet.util.Utils
import org.trustnote.wallet.widget.InputPwdDialogFragment
import org.trustnote.wallet.widget.MyTextWatcher
import org.trustnote.wallet.widget.TMnAmount

class FragmentWalletTransfer : FragmentWalletBase() {

    override fun getLayoutId(): Int {
        return R.layout.f_wallet_transfer
    }

    lateinit var title: TextView
    lateinit var balance: TMnAmount
    lateinit var receiverText: EditText
    lateinit var scanIcon: View
    lateinit var receiveErr: TextView

    lateinit var amountText: EditText
    lateinit var amountErr: TextView
    lateinit var transferAll: TextView
    lateinit var btnConfirm: Button

    override fun initFragment(view: View) {
        super.initFragment(view)

        title = findViewById(R.id.transfer_title)

        balance = findViewById(R.id.transfer_balance)
        receiverText = findViewById(R.id.transfer_receiver_hint)
        scanIcon = findViewById(R.id.transfer_receiver_scan)
        receiveErr = findViewById(R.id.transfer_receiver_err)

        amountText = findViewById(R.id.transfer_amount)
        amountErr = findViewById(R.id.transfer_receiver_err_overflow)

        transferAll = findViewById(R.id.transfer_transfer_all)
        transferAll.setOnClickListener { setTransferAmount(credential.balance) }


        btnConfirm = findViewById(R.id.transfer_confirm)
        btnConfirm.setOnClickListener { transfer() }

        setupScan(scanIcon) { handleScanRes(it) }


        amountText.addTextChangedListener(MyTextWatcher(this))

        setTransferAddress("GI6TXYXRSB4JJZJLECF3F5DOTUZ5V7MX")
        setTransferAmount(105000)

    }

    private fun transfer() {
        var paymentInfo = PaymentInfo()
        paymentInfo.receiverAddress = receiverText.text.toString()
        paymentInfo.amount = TTTUtils.parseTTTAmount(amountText.text.toString())
        paymentInfo.walletId = credential.walletId

        val unitComposer = UnitComposer(paymentInfo)
        if (unitComposer.isOkToSendTx()) {
            askUserInputPwdForTransfer(unitComposer)
        } else {
            Utils.toastMsg("发送失败")
        }
    }


    private fun askUserInputPwdForTransfer(unitComposer: UnitComposer) {

        InputPwdDialogFragment.showMe(activity, {
            TApp.userAlreadyInputPwd = true
            unitComposer.startSendTx(activity as MainActivity)
            activity.onBackPressed()
        })

        MyThreadManager.instance.runInBack {
            unitComposer.composeUnits()
        }

    }

    private fun setTransferAmount(transferAmount: Long) {
        //TODO: how about fee?
        if (transferAmount <= credential.balance) {
            TTTUtils.formatMN(amountText, transferAmount)
            amountErr.visibility = View.INVISIBLE
        } else {
            amountErr.visibility = View.VISIBLE
        }
        updateUI()
    }


    private fun setTransferAddress(address: String) {
        if (TTTUtils.isValidAddress(address)) {
            receiverText.setText(address)
            receiveErr.visibility = View.INVISIBLE
        } else {
            receiverText.setText(address)
            receiveErr.visibility = View.VISIBLE
        }
        updateUI()
    }

    private fun handleScanRes(scanRes: String) {
        val paymentInfo = TTTUtils.parsePaymentFromQRCode(scanRes)
        setTransferAmount(paymentInfo.amount)
        setTransferAddress(paymentInfo.receiverAddress)
        updateUI()
    }

    override fun updateUI() {
        balance.setMnAmount(credential.balance)
        title.text = credential.walletName

        if (TTTUtils.isValidAddress(receiverText.text.toString())
                && TTTUtils.isValidAmount(amountText.text.toString(), credential.balance)) {
            AndroidUtils.enableBtn(btnConfirm)
        } else {
            AndroidUtils.disableBtn(btnConfirm)
        }
    }

}

