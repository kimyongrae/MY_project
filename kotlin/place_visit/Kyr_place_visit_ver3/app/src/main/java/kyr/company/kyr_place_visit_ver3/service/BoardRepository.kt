package kyr.company.kyr_place_visit_ver3.service

import kyr.company.kyr_place_visit_ver3.common.AppContants
import kyr.company.kyr_place_visit_ver3.dao.BoardDao
import kyr.company.kyr_place_visit_ver3.model.FileVo
import kyr.company.kyr_place_visit_ver3.model.PlaceVo

class BoardRepository (private val boardDao: BoardDao){

    suspend fun dataInsert(placeVo: PlaceVo,flievos:MutableList<FileVo>){
        boardDao.dataInsert(placeVo,flievos)
    }

    suspend fun placelist():MutableList<PlaceVo>{

        val placelist= boardDao.placeList()
        val filelist:MutableList<FileVo> = boardDao.fileList()

/*        for (i in placelist){
            placeVos.add(PlaceVo())
        }*/

        for (i in placelist.indices){
            val fileVos= mutableListOf<FileVo>()
            for (j in filelist.indices){
                if(placelist[i].bno==filelist[j].parent_bno){
                    fileVos.add(filelist[j])
                }
            }
            placelist[i].fileVolist=fileVos
        }


        return placelist
    }

}