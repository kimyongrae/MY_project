package kyr.company.kyr_place_visit_ver2.model

import android.graphics.Bitmap
import androidx.room.*


@Entity(tableName = "FILE_TABLE",foreignKeys = [ForeignKey(entity = PlaceVo::class, parentColumns = ["BNO"], childColumns = ["PARENT_BNO"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)])
class FileVo constructor() {

        //파일 번호
        @ColumnInfo(name = "FILE_BNO")
        @PrimaryKey(autoGenerate = true)
        var file_bno : Int = 0
        //파일 경로
        @ColumnInfo(name = "FILE_PATH")
        var file_path : String = ""
        //부모 번호
        @ColumnInfo(name = "PARENT_BNO")
        var parent_bno : Int = 0

        @Ignore
        var bitmap : Bitmap? = null
        @Ignore
        var viewType : Int = 0

        constructor(file_path:String):this(){
             this.file_path=file_path
        }

}