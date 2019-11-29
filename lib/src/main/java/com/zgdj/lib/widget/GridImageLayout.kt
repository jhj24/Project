package com.zgdj.lib.widget

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
    val mContext: Context

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (context is ContextWrapper) {
            this.mContext = context.baseContext
        } else {
            this.mContext = context
        }
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
        recyclerView.preventStuck()
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
            deleteBody: (FileBean) -> Unit = {},
            body: (List<FileBean>) -> Unit
    ) {
        setGridImage(key, dataList, model, use, true, isDelete, authorityDelete, authorityAdd, displayBody, deleteBody, body)
    }

    fun setDisplayImage(
            dataList: List<FileBean>,
            isDelete: Boolean,
            authorityDelete: String? = null,
            displayBody: ((List<String>, Int) -> Unit?)? = null,
            deleteBody: (FileBean) -> Unit = {}
    ) {
        setGridImage(
                key = 0,
                model = "",
                use = "",
                dataList = dataList,
                isDelete = isDelete,
                authorityDelete = authorityDelete,
                displayBody = displayBody,
                deleteBody = deleteBody,
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
                    injector.getView<ImageView>(R.id.iv_image_selector_picture).glide(bean.formatPath)
                    injector
                            .clicked(R.id.iv_image_selector_picture) {
                                val list = getDataList<FileBean>()
                                val pos = list.map { it.formatPath }.indexOf(bean.formatPath)

                                if (pos != -1) {
                                    if (displayBody == null) {
                                        (mContext as Activity).imageDisplay(list.map { it.formatPath }, pos)
                                    } else {
                                        displayBody(list.map { it.formatPath }, pos)
                                    }
                                }
                            }
                            .with<ImageView>(R.id.iv_image_selector_state) { imageView ->
                                if (isDelete) {
                                    imageView.glide(R.mipmap.ic_work_delete)
                                    imageView.visibility = View.VISIBLE
                                    //authorityDelete?.let { authority -> it.authorityOnTouch(authority) }
                                    imageView.onClick { _ ->
                                        val list = getDataList<FileBean>()
                                        val pos = list.map { it.formatPath }.indexOf(bean.formatPath)
                                        (mContext as Activity).messageDialog(msg = "是否删除改照片") { alertFragment, view ->
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
                            ImageSelector.multiSelected(mContext as Activity) { imageList ->
                                this@register.addDataList(imageList.map {
                                    FileBean(filename = it.path.fileName, src = it.path)
                                })
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
            deleteBody: (FileBean) -> Unit = {},
            body: (List<FileBean>) -> Unit
    ) {
        val mList = arrayListOf<Any>()
        mList.addAll(dataList)
        if (isAdd) mList.add(Camera())
        SlimAdapter.creator()
                .register<FileBean>(R.layout.layout_image_selector_grid) { injector, bean, position ->
                    injector.getView<ImageView>(R.id.iv_image_selector_picture).glide(bean.formatPath)
                    injector
                            .clicked(R.id.iv_image_selector_picture) {
                                val list = getDataList<FileBean>()
                                val pos = list.map { it.formatPath }.indexOf(bean.formatPath)

                                if (pos != -1) {
                                    if (displayBody == null) {
                                        (mContext as Activity).imageDisplay(list.map { it.formatPath }, pos)
                                    } else {
                                        displayBody(list.map { it.formatPath }, pos)
                                    }
                                }
                            }
                            .with<ImageView>(R.id.iv_image_selector_state) {
                                if (isDelete) {
                                    it.glide(R.mipmap.ic_work_delete)
                                    it.visibility = View.VISIBLE
                                    authorityDelete?.let { authority -> it.authorityOnTouch(authority) }
                                    it.onClick {
                                        val list = getDataList<FileBean>()
                                        val pos = list.map { it.formatPath }.indexOf(bean.formatPath)
                                        (mContext as Activity).delete(UrlConfig.MEDIA_DELETE, "是否删除该图片？", "key" to bean.key.toString()) {
                                            if (pos != -1) {
                                                deleteBody(list[pos])
                                                remove(pos)
                                                recyclerView.removeViewAt(pos)
                                            }
                                        }
                                    }
                                }
                            }
                }
                .register<Camera>(R.layout.layout_image_selector_grid_camera) { injector, bean, positon ->
                    injector.with {
                        authorityAdd?.let { authority -> it.authorityOnTouch(authority) }
                        it.onClick {
                            ImageSelector.multiSelected(mContext as Activity) { imageList ->
                                (mContext as Activity).uploadMedia(
                                        model,
                                        use,
                                        *imageList.map { localMedia -> localMedia.path }.toTypedArray()
                                ) { isTrue, list ->
                                    if (isTrue) {
                                        body(list)
                                    } else {
                                        context.toast("文件上传失败")
                                    }
                                    this@register.addDataList(0, list)
                                }
                            }
                        }
                    }
                }
                .attachTo(recyclerView)
                .setDataList(mList)
    }
}