package kyr.company.kyr_place_visit_ver3.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.common.AppContants
import kyr.company.kyr_place_visit_ver3.common.MyApplication
import kyr.company.kyr_place_visit_ver3.databinding.CalendarDiaryNoteBinding
import kyr.company.kyr_place_visit_ver3.databinding.ListFragmentBinding
import kyr.company.kyr_place_visit_ver3.databinding.ListItemBinding
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

class ListAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var placeItems : MutableList<PlaceVo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
            //캘린더 리스트 아이템
        val listItemBinding : ListItemBinding = DataBindingUtil.inflate(inflater, R.layout.list_item,parent,false)
        return ListViewHolder(listItemBinding)
    }

    override fun getItemCount()=placeItems.size

    fun setItems(items : MutableList<PlaceVo>){
        this.placeItems=items
    }

    fun addItem(item: PlaceVo)= placeItems.add(item)

    override fun getItemViewType(position: Int): Int {
        return placeItems[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is ListViewHolder){
            val listViewHolder = holder as ListViewHolder

            //listViewHolder..root.layoutParams.width = AppContants.displayPixelWidth/4

            listViewHolder.binding.root.layoutParams.height = AppContants.displayPixelHeight/10*4


            val item : PlaceVo =placeItems[position]
            listViewHolder.setItem(item)
        }
    }


    inner class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun setItem(item: PlaceVo){

            if(item.fileVolist.size<2){
                binding.pictureMore.visibility= View.GONE
            }

            //파일리스트에 데이터가 있는경우
            if(item.fileVolist.size!=0){
                if(item.fileVolist[0].file_path!=null || item.fileVolist[0].file_path ==""){
                    setPicture(item.fileVolist[0].file_path,1)
                }
            }


            binding.title.text=("${item.title}")
            binding.address.text=("${item.address}")
            binding.dateText.text=item.startDate+"~"+item.endDate
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
            binding.picture.setImageBitmap(resultPhotoBitmap)
        }

    }






}


