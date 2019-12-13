package com.zgdj.lib.extention

import android.app.Activity
import com.jhj.imageselector.ImageSelector
import com.jhj.imageselector.bean.LocalMedia


fun Activity.imageDisplay(list: List<String>, index: Int) {
    val localMediaList = list.map { LocalMedia(it) }
    ImageSelector.preview(this, localMediaList, index)
}


