package kyr.company.kyr_place_visit_ver2.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BoardDao {

    @Query("INSERT INTO PLACE_TABLE('TITLE','CONTENT','S_DATE','E_DATE','S_TIME','E_TIME','ADDRESS','LATITUDE','LONGITUDE') VALUES(:title,:content,:s_date,:e_date,:s_time,:e_time,:address,:latitude,:longitude)")
    suspend fun placeInsert(title:String,content:String,s_date:String,e_date:String,s_time:String,e_time:String,address:String,latitude:Double,longitude:Double)

}