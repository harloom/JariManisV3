package com.app.jarimanis.ui.PengaturanAkun

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.data.repository.profile.ProfileRepositoryImp
import com.app.jarimanis.ui.PengaturanAkun.BottomSheet.CallbackSuccessListener
import com.app.jarimanis.ui.PengaturanAkun.BottomSheet.UbahSheet
import com.app.jarimanis.ui.dashboard.DashboardViewModel
import com.app.jarimanis.utils.G4Engine
import com.app.jarimanis.utils.GifSizeFilter
import com.app.jarimanis.utils.Key.DATAUSERNOW
import com.app.jarimanis.utils.Key.TYPE_NUMBERPHONE
import com.app.jarimanis.utils.Key.TYPE_USERNAME
import com.app.jarimanis.utils.Key.USERSETTING
import com.app.jarimanis.utils.RequestCode.REQUEST_CODE_IMAGE
import com.app.jarimanis.utils.TimeFormater
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.pengaturan_akun_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alhazmy13.gota.Gota
import net.alhazmy13.gota.GotaResponse
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class PengaturanAkunFragment : Fragment(), CallbackSuccessListener, Gota.OnRequestPermissionsBack {
    override fun onRequestBack(requestId: Int, gotaResponse: GotaResponse) {
        if(gotaResponse.isAllGranted){
            loadImageFromGallery()
        }

    }

    override fun onSuccess() {
        vm.refress()
    }
    private val repoProfileImp: ProfileRepositoryImp = get()

    companion object {
        fun newInstance() = PengaturanAkunFragment()
    }


    private val  vm: DashboardViewModel by viewModel()
    private var userDataNow : Result? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pengaturan_akun_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Pengaturan Akun"
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.myProfile.observe(this@PengaturanAkunFragment, Observer {
            userDataNow = it
            tv_displayName.text = it.nameUser
            tv_displayEmail.text = it.emailUser
            tv_displayNumber.text = it.numberPhone
            it.birthDayUser?.let {s->
                tv_displayBirthDay.text = TimeFormater.StringToDate(s)
            }
            initImage(it.thumbail)
        })

        onclikChange()

    }


    private fun onclikChange(){
        btn_ubahName.setOnClickListener {
            val bts = UbahSheet(this@PengaturanAkunFragment)
            val bundle  = bundleOf(USERSETTING to TYPE_USERNAME , DATAUSERNOW to userDataNow )
            bts.arguments = bundle
            bts.show(childFragmentManager,"ChangeUserProfile")
        }
        btn_ubahPhone.setOnClickListener {
            val bts = UbahSheet(this@PengaturanAkunFragment)
            val bundle  = bundleOf(USERSETTING to TYPE_NUMBERPHONE , DATAUSERNOW to userDataNow )
            bts.arguments = bundle
            bts.show(childFragmentManager,"ChangeUserProfile")
        }
        ivProfPict.setOnClickListener {
           permission()
        }


    }

    private fun initImage(s : String?){
        try {
            s.let {
                Glide.with(this@PengaturanAkunFragment).load(s).into(ivProfPict)
            }

        }catch (e : Exception){

        }
    }

    private fun permission(){
        Gota.Builder(activity).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
            .setListener(this@PengaturanAkunFragment).check()
    }
    private fun loadImageFromGallery() {
        Matisse.from(this@PengaturanAkunFragment)
            .choose(MimeType.ofImage())
            .countable(true)
            .showSingleMediaType(true)
            .maxSelectable(1)
            .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(G4Engine())
            .forResult(REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            val mSelected = Matisse.obtainPathResult(data)[0]
            renderAndUpload(mSelected)

        }
    }

    private fun renderAndUpload(mSelected: String){
        try {
            val storage = FirebaseStorage.getInstance()
            val file = Uri.fromFile(File(mSelected))
            val ref = storage.reference.child("users/${TokenUser.idUser}")
            val uploadTask = ref.putFile(file)
            uploadTask.addOnProgressListener {
                progress_circular.visibility = View.VISIBLE
            }
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    CoroutineScope(IO).launch{
                        val respon = repoProfileImp.ubahFoto(downloadUri.toString())
                        withContext(context = Main){
                            if(respon.isSuccessful){
                                progress_circular.visibility = View.GONE
                                Glide.with(this@PengaturanAkunFragment).load(downloadUri).into(ivProfPict)
                            }
                        }


                    }


                } else {

                }
            }

        }catch (e  : Exception){
            println(e.message)
        }
    }


}
