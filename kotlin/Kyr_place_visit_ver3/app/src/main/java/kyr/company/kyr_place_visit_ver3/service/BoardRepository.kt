package kyr.company.kyr_place_visit_ver3.service

import kyr.company.kyr_place_visit_ver3.dao.BoardDao
import kyr.company.kyr_place_visit_ver3.model.FileVo
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

class BoardRepository (private val boardDao: BoardDao){

    suspend fun DataInsert(placeVo: PlaceVo,flievo:MutableList<FileVo>){

    }

}