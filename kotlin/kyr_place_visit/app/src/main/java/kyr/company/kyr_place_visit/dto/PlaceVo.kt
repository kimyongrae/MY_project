package kyr.company.kyr_place_visit.dto

class PlaceVo constructor() {

    //장소 번호
    public var bno :Int = 0
    //장소 제목
    public var title : String =""
    //장소 설명
    public var content : String =""
    //장소 작성 날짜
    public var createDate : String = ""
    //방문 시작일
    public var startDate : String =""
    //방문 종료일
    public var endDate : String = ""
    //방문한 주소
    public var address : String = ""
    //위도
    public var latitude : Double = 0.0
    //경도
    public var longitude : Double = 0.0

    constructor(title:String,content:String,address:String):this(){
        this.title=title
        this.content=content
        this.address=address
    }


}