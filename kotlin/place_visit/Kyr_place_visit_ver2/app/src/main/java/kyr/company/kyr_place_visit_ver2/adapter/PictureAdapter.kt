package kyr.company.kyr_place_visit_ver2.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kyr.company.kyr_place_visit_ver2.R
import kyr.company.kyr_place_visit_ver2.common.AppContants
import kyr.company.kyr_place_visit_ver2.common.MyApplication
import kyr.company.kyr_place_visit_ver2.databinding.UploadPicutureLayoutBinding
import kyr.company.kyr_place_visit_ver2.model.FileVo

class PictureAdapter constructor():RecyclerView.Adapter<RecyclerView.ViewHolder>(),PictureItemListener {

    private var item: MutableList<FileVo> = mutableListOf()
    lateinit var listener: PictureItemListener

    //리스너 설정
    fun setItemListner(listener: PictureItemListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater : LayoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            0->{
                val uploadPicutureLayoutBinding:UploadPicutureLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.upload_picuture_layout,parent,false)
                UploadPictureViewHolder(uploadPicutureLayoutBinding)
            }

            else->{
                val uploadPicutureLayoutBinding:UploadPicutureLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.upload_picuture_layout,parent,false)
                UploadPictureViewHolder(uploadPicutureLayoutBinding)
            }
        }

    }

    fun setItems(vo : MutableList<FileVo>){
        this.item = vo
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is UploadPictureViewHolder){
            val uploadPictureViewHolder = holder as UploadPictureViewHolder

            uploadPictureViewHolder.binding.root.layoutParams.width = AppContants.displayPixelWidth/4

            val data : FileVo = item[position]
            uploadPictureViewHolder.bind(data)

        }


    }

    //아이템 삭제
    override fun onRemoveClick(position: Int) {
        if(listener!=null){
            listener.onRemoveClick(position)
        }
    }

    inner class UploadPictureViewHolder(val binding: UploadPicutureLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data : FileVo){

            if(data.file_path !=null && data.file_path != ""){
                setPicture(data.file_path,1)
            }else{

            }

        }

        fun setPicture(picturePath: String?, sampleSize: Int) {
            val options = BitmapFactory.Options()
            var resultPhotoBitmap: Bitmap? = null

            options.inSampleSize = sampleSize
            //        options.inJustDecodeBounds=true;
            resultPhotoBitmap = BitmapFactory.decodeFile(picturePath, options)
            var rotateMatrix = Matrix()
/*          *//*   rotateMatrix.setScale(-1f,1f)
            rotateMatrix.postRotate(90F)*//*
            rotateMatrix.postRotate(90F)
            var rotatedBitmap: Bitmap = Bitmap.createBitmap(resultPhotoBitmap, 0,0, resultPhotoBitmap.width, resultPhotoBitmap.height, rotateMatrix, false)*/
//          resultPhotoBitmap = decodeSampledBitmapFromResource(new File(picturePath),width,height);
            binding.image.setImageBitmap(resultPhotoBitmap)
        }


    }





}