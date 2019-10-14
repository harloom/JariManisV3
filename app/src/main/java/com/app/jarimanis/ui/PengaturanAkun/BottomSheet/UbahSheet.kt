package com.app.jarimanis.ui.PengaturanAkun.BottomSheet

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.data.repository.profile.ProfileRepositoryImp
import com.app.jarimanis.utils.Key.DATAUSERNOW
import com.app.jarimanis.utils.Key.TYPE_DATEUSER
import com.app.jarimanis.utils.Key.TYPE_NUMBERPHONE
import com.app.jarimanis.utils.Key.TYPE_USERNAME
import com.app.jarimanis.utils.Key.USERSETTING
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get

class UbahSheet constructor(val callbackSuccessListener: CallbackSuccessListener) : BottomSheetDialogFragment() {

    val repoProfileImp: ProfileRepositoryImp = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_setting, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickFun()
        val orderChange = arguments?.getInt(USERSETTING)
        val userNow = arguments?.getParcelable<Result>(DATAUSERNOW)
        orderChange?.let { it ->
            when (it) {
                TYPE_USERNAME -> {
                    tv_ubah.text = "Masukan Nama Anda"
                    etValue.setText(userNow?.nameUser)
                    btn_simpan.setOnClickListener {
                        CoroutineScope(Main).launch {
                            val respon =  repoProfileImp.ubahNama(etValue.text.toString())
                            callbackSuccessListener.onSuccess()
                            this@UbahSheet.dismiss()
                        }

                    }


                }
                TYPE_NUMBERPHONE -> {
                    tv_ubah.text = "Masukan Nomer Handphone Anda"
                    etValue.inputType = InputType.TYPE_CLASS_NUMBER
                    etValue.setText(userNow?.numberPhone)
                    btn_simpan.setOnClickListener {
                        CoroutineScope(Main).launch {
                            val respon =  repoProfileImp.ubahNumberPhone(etValue.text.toString())
                            callbackSuccessListener.onSuccess()
                            this@UbahSheet.dismiss()
                        }
                    }

                }
                else -> {

                }

            }
        }
    }


    private fun onClickFun() {
        btn_batal.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

interface CallbackSuccessListener{
    fun onSuccess()
}