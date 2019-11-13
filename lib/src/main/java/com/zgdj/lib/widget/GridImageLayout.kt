package com.zgdj.lib.widget

import android.app.Activity
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jhj.imageselector.ImageSelector
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.itemdecoration.GridItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.bean.Camera
import com.zgdj.lib.bean.FileBean
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.extention.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class GridImageLayout : LinearLayout {

    var recyclerView: RecyclerView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = VERTICAL
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridImageLayout)
        val title = typedArray.getString(R.styleable.GridImageLayout_image_title)

        typedArray.recycle()
        //标题
        val textView = TextView(context)
        textView.height = dimen(R.dimen.bill_height_item)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.leftPadding = dimen(R.dimen.bill_padding_left)
        textView.textSize = 16f
        textView.textColor = color(R.color.text_title)
        textView.text = title
        addView(textView)

        //分割线
        val line = TextView(context)
        line.backgroundColor = color(R.color.line_color)
        line.height = 1
        addView(line)

        //图片列表
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.setPadding(dip(20), dip(5), dip(20), dip(5))
        recyclerView.addItemDecoration(GridItemDecoration(3, dip(5), false))
        this.recyclerView = recyclerView
        addView(recyclerView)
    }

    fun setEditGridImage(
        key: Int,
        dataList: List<FileBean>,
        model: String,
        use: String,
        isDelete: Boolean,
        authorityDelete: String? = null,
        authorityAdd: String? = null,
        displayBody: ((List<String>, Int) -> Unit?)? = null,
        body: (List<FileBean>) -> Unit
    ) {
        setGridImage(key, dataList, model, use, true, isDelete, authorityDelete, authorityAdd, displayBody, body)
    }

    fun setDisplayImage(
        dataList: List<FileBean>,
        isDelete: Boolean,
        authorityDelete: String? = null,
        displayBody: ((List<String>, Int) -> Unit?)? = null
    ) {
        setGridImage(
            key = 0,
            model = "",
            use = "",
            dataList = dataList,
            isDelete = isDelete,
            authorityDelete = authorityDelete,
            displayBody = displayBody,
            body = {})
    }


    fun setGridImage(
        dataList: List<FileBean>,
        isAdd: Boolean = true,
        isDelete: Boolean = true,
        displayBody: ((List<String>, Int) -> Unit?)? = null,
        body: (List<FileBean>) -> Unit
    ) {
        val mList = arrayListOf<Any>()
        if (isAdd) mList.add(Camera())
        mList.addAll(dataList)
        SlimAdapter.creator()
            .register<FileBean>(R.layout.layout_image_selector_grid) { injector, bean, position ->
                injector.getView<ImageView>(R.id.iv_image_selector_picture).glide(bean.src)
                injector
                    .clicked(R.id.iv_image_selector_picture) {
                        val list = getDataList().filterIsInstance<FileBean>()
                        val pos = list.map { it.src }.indexOf(bean.src)

                        if (pos != -1) {
                            if (displayBody == null) {
                                (context as Activity).imageDisplay(list.map { it.src }, pos)
                            } else {
                                displayBody(list.map { it.src }, pos)
                            }
                        }
                    }
                    .with<ImageView>(R.id.iv_image_selector_state) {
                        if (isDelete) {
                            it.glide(R.mipmap.ic_work_delete)
                            it.visibility = View.VISIBLE
                            //authorityDelete?.let { authority -> it.authorityOnTouch(authority) }
                            it.onClick {
                                val list = getDataList().filterIsInstance<FileBean>()
                                val pos = list.map { l -> l.src }.indexOf(bean.src)
                                (context as Activity).messageDialog(msg = "是否删除改照片") { alertFragment, view ->
                                    remove(pos + 1)
                                    recyclerView.removeViewAt(pos + 1)
                                }
                            }
                        }
                    }
            }
            .register<Camera>(R.layout.layout_image_selector_grid_camera) { injector, bean, positon ->
                injector.with {
                    it.onClick {
                        ImageSelector.multiSelected(context as Activity) { imageList ->
                            this@register.addDataList(imageList.map { FileBean(it.path.fileName, it.path) })
                        }
                    }
                }
            }
            .attachTo(recyclerView)
            .setDataList(mList)
    }


    fun setGridImage(
        key: Int,
        dataList: List<FileBean>,
        model: String,
        use: String,
        isAdd: Boolean = true,
        isDelete: Boolean = true,
        authorityDelete: String? = null,
        authorityAdd: String? = null,
        displayBody: ((List<String>, Int) -> Unit?)? = null,
        body: (List<FileBean>) -> Unit
    ) {
        val mList = arrayListOf<Any>()
        if (isAdd) mList.add(Camera())
        mList.addAll(dataList)
        SlimAdapter.creator()
            .register<FileBean>(R.layout.layout_image_selector_grid) { injector, bean, position ->
                injector.getView<ImageView>(R.id.iv_image_selector_picture).glide(bean.src)
                injector
                    .clicked(R.id.iv_image_selector_picture) {
                        val list = getDataList().filterIsInstance<FileBean>()
                        val pos = list.map { it.src }.indexOf(bean.src)

                        if (pos != -1) {
                            if (displayBody == null) {
                                (context as Activity).imageDisplay(list.map { it.src }, pos)
                            } else {
                                displayBody(list.map { it.src }, pos)
                            }
                        }
                    }
                    .with<ImageView>(R.id.iv_image_selector_state) {
                        if (isDelete) {
                            it.glide(R.mipmap.ic_work_delete)
                            it.visibility = View.VISIBLE
                            //authorityDelete?.let { authority -> it.authorityOnTouch(authority) }
                            it.onClick {
                                val list = getDataList().filterIsInstance<FileBean>()
                                val pos = list.map { l -> l.src }.indexOf(bean.src)
                                (context as Activity).delete(
                                    UrlConfig.MEDIA_DELETE,
                                    "是否删除该图片？",
                                    "key" to bean.key.toString()
                                ) {
                                    if (pos != -1) {
                                        remove(pos + 1)
                                        recyclerView.removeViewAt(pos + 1)
                                    }
                                }
                            }
                        }
                    }
            }
            .register<Camera>(R.layout.layout_image_selector_grid_camera) { injector, bean, positon ->
                injector.with {
                    //authorityAdd?.let { authority -> it.authorityOnTouch(authority) }
                    it.onClick {
                        ImageSelector.multiSelected(context as Activity) { imageList ->
                            (context as Activity).uploadMedia(
                                key.toString(),
                                model,
                                use,
                                *imageList.map { localMedia -> localMedia.path }.toTypedArray()
                            ) { isTrue, list ->
                                if (isTrue) {
                                    body(list)
                                } else {
                                    context.toast("文件上传失败")
                                }
                                this@register.addDataList(list)
                            }
                        }
                    }
                }
            }
            .attachTo(recyclerView)
            .setDataList(mList)
    }
}