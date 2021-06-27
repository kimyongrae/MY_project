package kyr.company.kyr_place_visit_ver3.dao

import androidx.room.*
import kyr.company.kyr_place_visit_ver3.model.FileVo
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

@Dao
interface BoardDao {

    //방문 데이터 넣기
    @Query("INSERT INTO PLACE_TABLE('TITLE','CONTENT','S_DATE','E_DATE','S_TIME','E_TIME','ADDRESS','LATITUDE','LONGITUDE') VALUES(:title,:content,:s_date,:e_date,:s_time,:e_time,:address,:latitude,:longitude)")
    suspend fun placeInsert(title:String,content:String,s_date:String,e_date:String,s_time:String,e_time:String,address:String,latitude:Double,longitude:Double)

    //파일 데이터 넣기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fileInsert(fileDataList: MutableList<FileVo>)

    //방문 리스트 찾기
    @Query("SELECT * FROM PLACE_TABLE ORDER BY BNO DESC")
    suspend fun placeList(): MutableList<PlaceVo>


    //파일 리스트 찾기
    @Query("SELECT * FROM FILE_TABLE ORDER BY FILE_BNO DESC")
    suspend fun fileList(): MutableList<FileVo>

    //방문 데이터 최대값 찾기
    @Query("SELECT IFNULL(MAX(BNO),1) FROM PLACE_TABLE")
    suspend fun placeMaxNumber(): Int

    //디비에 데이터 저장
    @Transaction
    suspend fun dataInsert(placeVo: PlaceVo,fileVo: MutableList<FileVo>){
//        var result:Int = 0

        //방문장소 기록 디비에 저장
        placeInsert(placeVo.title,placeVo.content,placeVo.startDate,placeVo.endDate,placeVo.startTime,placeVo.endTime,placeVo.address,placeVo.latitude,placeVo.longitude)

        if(fileVo.size>0){
            val parentNumber : Int = placeMaxNumber()
            val fileVos:MutableList<FileVo> = mutableListOf()
            for (i in fileVo.indices){
                fileVos.add(FileVo(parentNumber,fileVo[i].file_path))
            }
            //파일목록 디비에 저장
            fileInsert(fileVos)
//            result = parentNumber
        }

//        return result
    }





}