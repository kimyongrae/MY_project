package kyr.company.kyr_place_visit_ver3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.common.MyApplication
import kyr.company.kyr_place_visit_ver3.databinding.CalendarDiaryNoteBinding
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

class PlaceAdapter :RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private var placeItems : MutableList<PlaceVo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(MyApplication.applicationContext())
        val placeItemBinding : CalendarDiaryNoteBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_diary_note,parent,false)
        return ViewHolder(placeItemBinding)
    }

    override fun getItemCount()=placeItems.size

    fun setItems(items : MutableList<PlaceVo>){
        this.placeItems=items
    }

    fun addItem(item: PlaceVo)= placeItems.add(item)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : PlaceVo =placeItems[position]
        holder.setItem(item)

    }


    inner class ViewHolder(private val binding: CalendarDiaryNoteBinding) : RecyclerView.ViewHolder(binding.root){


        fun setItem(item: PlaceVo){
            binding.title.text=("${item.title}")
            binding.Contents.text=("${item.content}")
            binding.address.text=("${item.address}")
        }

    }



}


