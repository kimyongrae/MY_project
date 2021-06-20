package kyr.company.kyr_place_visit_ver2.service

import kyr.company.kyr_place_visit_ver2.dao.BoardDao
import kyr.company.kyr_place_visit_ver2.model.FileVo
import kyr.company.kyr_place_visit_ver2.model.PlaceVo

class BoardRepository (private val boardDao: BoardDao){

    suspend fun DataInsert(placeVo: PlaceVo,flievo:MutableList<FileVo>){

    }

}