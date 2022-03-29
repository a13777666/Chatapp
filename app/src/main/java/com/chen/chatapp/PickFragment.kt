package com.chen.chatapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.chen.chatapp.databinding.FragmentPickBinding

class PickFragment: Fragment() {

    companion object {
        var TAG = PickFragment::class.java.simpleName
        val instance: PickFragment by lazy {
            PickFragment()
        }
    }
    val shareviewModel by activityViewModels<PickSharedViewModel>()

    private val ACTION_CAMERA_REQUEST_CODE = 100
    private val ACTION_ALBUM_REQUEST_CODE = 200
    lateinit var binding: FragmentPickBinding
//    val selectPictureFromGallery =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                Nowuser.headshot = it.toString()
//                //uri.toString()//content://ccc.ddd/sss/aaa
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPickBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = requireActivity() as MainActivity

        binding.bExit.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[3])
                commit()
            }
        }

        binding.bAlbum.setOnClickListener {
            takeImageFromAlbumWithIntent()
        }
        binding.bShot.setOnClickListener {
            takeImageFromCameraWithIntent()
        }
        //let photo match imageview
//        binding.ivTest.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    // 通過 intent 使用 Camera
    private fun takeImageFromCameraWithIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
    }

    // 通過 intent 使用 album
    private fun takeImageFromAlbumWithIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE)
    }

    //set Image
//    private fun displayImage(bitmap: Bitmap) {
//        binding.ivTest.setImageBitmap(bitmap)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("收到 result code $requestCode")

        when(requestCode) {
            ACTION_CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    //get photo
                    val avatar = data.extras?.get("data") as Bitmap
                    shareviewModel.avatar.value = avatar
                    Toast.makeText(requireContext(),"選取成功", Toast.LENGTH_SHORT).show()
                }
            }

            ACTION_ALBUM_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    //get photo
                    val resolver = activity?.contentResolver
                    val avatar = MediaStore.Images.Media.getBitmap(resolver, data?.data)
                    shareviewModel.avatar.value = avatar
                    Toast.makeText(requireContext(),"選取成功", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(requireContext(), "選取失敗", Toast.LENGTH_SHORT).show()
            }
        }
    }
}