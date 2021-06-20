package kyr.company.kyr_place_visit_ver3.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "PLACE_TABLE")
class PlaceVo constructor() {

    //장소 번호
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "BNO")
    var bno :Int = 0

    //방문한 주소
    @ColumnInfo(name = "ADDRESS")
    var address : String = ""
    //장소 제목
    @ColumnInfo(name = "TITLE")
    var title : String =""
    //장소 설명
    @ColumnInfo(name = "CONTENT")
    var content : String =""
    //방문 시작일
    @ColumnInfo(name = "S_DATE")
    var startDate : String =""
    //방문 종료일
    @ColumnInfo(name = "E_DATE")
    var endDate : String = ""
    //방문 시작시간
    @ColumnInfo(name = "S_TIME")
    var startTime : String =""
    //방문 종료시간
    @ColumnInfo(name = "E_TIME")
    var endTime : String =""

    //위도
    @ColumnInfo(name = "LATITUDE")
    var latitude : Double = 0.0
    //경도
    @ColumnInfo(name = "LONGITUDE")
    var longitude : Double = 0.0

    //장소 작성일
    @ColumnInfo(name = "CDATE",defaultValue = "(datetime('now','localtime'))")
    var createDate : String = ""

    constructor(title:String,content:String,address:String):this(){
        this.title=title
        this.content=content
        this.address=address
    }

    override fun toString(): String {
        return "address:$address\n" +
                "title:$title" +
                "content:$content" +
                "startdate:$startDate" +
                "enddate:$endDate" +
                "startTime:$startTime" +
                "endTime:$endTime" +
                "latitude:$latitude" +
                "longitude:$longitude"
    }

    
}