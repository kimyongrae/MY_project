package kyr.company.kyr_place_visit_ver3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kyr.company.kyr_place_visit_ver3.R
import kyr.company.kyr_place_visit_ver3.common.MyApplication
import kyr.company.kyr_place_visit_ver3.databinding.CalendarDiaryNoteBinding
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

class CalenderAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var placeItems : MutableList<PlaceVo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater : LayoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){

            //캘린더 리스트 아이템
            0->{
                val CanlenderListItemBinding : CalendarDiaryNoteBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_diary_note,parent,false)
                CalnederListViewHolder(CanlenderListItemBinding)
            }

            else->{
                val placeItemBinding : CalendarDiaryNoteBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_diary_note,parent,false)
                CalnederListViewHolder(placeItemBinding)
            }
        }

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

        if(holder is CalnederListViewHolder){
            val calnederListViewHolder = holder as CalnederListViewHolder
            val item : PlaceVo =placeItems[position]
            calnederListViewHolder.setItem(item)
        }
    }


    inner class CalnederListViewHolder(private val binding: CalendarDiaryNoteBinding) : RecyclerView.ViewHolder(binding.root){

        fun setItem(item: PlaceVo){
            binding.title.text=("${item.title}")
            binding.Contents.text=("${item.content}")
            binding.address.text=("${item.address}")
        }

    }




}


