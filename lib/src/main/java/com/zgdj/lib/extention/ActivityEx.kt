package com.zgdj.lib.extention

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.os.Looper
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.model.HttpHeaders
import com.jhj.httplibrary.request.DownloadRequest
import com.jhj.httplibrary.request.PostRequest
import com.jhj.imageselector.ImageSelector
import com.jhj.imageselector.bean.LocalMedia
import com.jhj.prompt.fragment.AlertFragment
import com.jhj.prompt.fragment.LoadingFragment
import com.jhj.prompt.fragment.PercentFragment
import com.zgdj.lib.BaseApplication
import com.zgdj.lib.R
import com.zgdj.lib.bean.FileBean
import com.zgdj.lib.bean.UserBean
import com.zgdj.lib.config.Config
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.ui.FileDisplayActivity
import com.zgdj.lib.ui.X5WebViewActivity
import com.zgdj.lib.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


val Context.density: Float
    get() = this.resources.displayMetrics.density

val Context.scaleDensity: Float
    get() = this.resources.displayMetrics.scaledDensity

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

val Context.itemClickBackground: StateListDrawable
    get() = BackGroundUtils.pressed(getResColor(R.color.bg_item_pressed), getResColor(R.color.bg_item_normal))

val Context.buttonTextColor: Int
    get() = getResColor(R.color.bg_bottom_color)

fun Context.selected(press: Int, normal: Int): StateListDrawable {
    return selected(getResDrawable(press), getResDrawable(normal))
}

fun Context.getResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getResDrawable(@DrawableRes id: Int): Drawable {
    return ContextCompat.getDrawable(this, id) ?: resources.getDrawable(id)
}

fun selected(selected: Drawable, normal: Drawable): StateListDrawable {
    return BackGroundUtils.selected(selected, normal)
}

fun pressed(press: Drawable, normal: Drawable): StateListDrawable {
    return BackGroundUtils.pressed(press, normal)
}

fun <T> Context.assets(assetsFileName: String): T {
    val inputStream = resources.assets.open(assetsFileName)
    val text = inputStream.use {
        val buf = BufferedReader(InputStreamReader(inputStream, "utf-8"))
        return@use buf.readText()
    }
    return Gson().fromJson(text, object : TypeToken<T>() {}.type)
}


//===========================keyboard=============================
fun Context.openKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
    imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
    );
}

fun Context.closeKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


//============================image==============================
fun Context.avatar(imagePath: Any, imageView: ImageView) {
    ImageUtil.show(this, imagePath, imageView, ImageUtil.avatarOptions)
}

fun Context.image(imagePath: Any, imageView: ImageView) {
    ImageUtil.show(this, imagePath, imageView, ImageUtil.options)
}

fun Context.roundImage(imagePath: Any, imageView: ImageView) {
    ImageUtil.show(this, imagePath, imageView, ImageUtil.roundOptions)
}

fun Context.circleImage(imagePath: Any, imageView: ImageView) {
    ImageUtil.show(this, imagePath, imageView, ImageUtil.circleOptions)
}

fun Context.imageNoCache(imagePath: Any, imageView: ImageView) {
    ImageUtil.show(this, imagePath, imageView, ImageUtil.noCacheOptions)
}


//=============================net===============================
fun Context.isNetworkConnected(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo
    if (networkInfo != null) {
        return networkInfo.isConnected
    }
    return false
}

val Context.loginUserInfo: UserBean?
    get() {
        val user: UserBean? = SystemEnv.getLogin(this)
        if (user == null) {
            loginExpired()
            return null
        }
        return user
    }


fun Context.download(url: String): DownloadRequest {
    return HttpCall.download(url)
}

fun Context.httpPost(url: String): PostRequest {
    val login = loginUserInfo ?: return HttpCall.post(UrlConfig.BASE_URL)
    val header = HttpHeaders()
    header.put("key", login.key.toString())
    header.put("token", login.token)
    return HttpCall.post(CommonNetUtils.baseUrl + url).addHeaders(header)
}

fun Context.pyHttpPost(url: String): PostRequest {
    val login = loginUserInfo ?: return HttpCall.post(UrlConfig.BASE_URL)
    val header = HttpHeaders()
    header.put("key", login.key.toString())
    header.put("token", login.token)
    return HttpCall.post(UrlConfig.PYTHON_URL + url).addHeaders(header)
}

fun Context.uploadMedia(module: String, use: String, vararg path: String, body: (Boolean, List<FileBean>) -> Unit) {
    CommonNetUtils.uploadMedia(this, module, use, *path, body = body)
}

fun Activity.delete(url: String, msg: String, vararg pairs: Pair<String, String>, body: () -> Unit = {}) {
    CommonNetUtils.delete(this, url, msg, *pairs, body = body)
}

//=============================dialog===============================

fun Context.downloadDialog(
        tag: Any,
        text: String = "正在下载...",
        body: (PercentFragment) -> Unit = {}
): PercentFragment.Builder {
    return DialogUtils.downloadDialog(this, tag, text, body)
}

fun Context.loadingDialog(text: String = "正在加载...", body: (LoadingFragment) -> Unit = {}): LoadingFragment.Builder {
    return DialogUtils.loadingDialog(this, text, body)
}

fun Context.uploadDialog(msg: String = "正在上传...", body: (LoadingFragment) -> Unit): LoadingFragment.Builder {
    return DialogUtils.uploadDialog(this, msg, body)
}

fun Context.percentUploadDialog(msg: String = "正在上传...", body: (PercentFragment) -> Unit): PercentFragment.Builder {
    return DialogUtils.uploadDialog(this, msg, body)
}

fun Context.messageDialog(title: String = "提示", msg: String, body: (AlertFragment, View?) -> Unit = { _, _ -> }) {
    return DialogUtils.messageDialog(this, title, msg, body)
}

fun Context.customDialog(layoutRes: Int, body: (View, AlertFragment) -> Unit) {
    return DialogUtils.customDialog(this, layoutRes, body)
}

fun Context.bottomDialog(layoutRes: Int, body: (View, AlertFragment) -> Unit): AlertFragment.Builder {
    return DialogUtils.bottomCustomDialog(this, layoutRes, body)
}

fun Context.bottomSingleDialog(title: String = "请选择", list: List<String>, body: (AlertFragment, String) -> Unit) {
    return DialogUtils.bottomSingleDialog(this, title, list, body)
}

fun Context.bottomMultiDialog(
        title: String = "请选择",
        list: List<String>,
        selectedList: List<Int>,
        body: (AlertFragment, List<Int>) -> Unit
) {
    return DialogUtils.bottomMultiDialog(this, title, list, selectedList, body)
}

fun Context.timePick(date: Date?, type: BooleanArray = TimePickerUtils.type2, body: (Date) -> Unit) {
    TimePickerUtils.timePicker(this, date, type, body)
}

fun Context.timePick(
        startData: Date?,
        endData: Date?,
        isStartTime: Boolean,
        type: BooleanArray = TimePickerUtils.type2,
        msg: String = "开始时间小于结束事件，请重新选择",
        body: (Date) -> Unit = {},
        calculate: (startDate: Date, endDate: Date) -> Unit = { _, _ -> }
) {
    TimePickerUtils.timePicker(this, startData, endData, isStartTime, type, msg, body, calculate)
}


//===================================file=========================
fun Context.fileTypeIcon(filePath: String, imageView: ImageView) {
    FileUtils.fileTypeIcon(this, filePath, imageView)
}

fun Context.readAssets(name: String): String {
    val inputStream = resources.assets.open(name)
    val text = inputStream.use {
        val buf = BufferedReader(InputStreamReader(inputStream, "utf-8"))
        return@use buf.readText()
    }
    return text
}

fun Activity.imageDisplay(list: List<String>, index: Int) {
    val localMediaList = list.map { LocalMedia(it) }
    ImageSelector.preview(this, localMediaList, index)
}

fun Activity.fileDisplay(path: String) {
    if (BaseApplication.isTBSLoadingSuccess) {
        val dialog = loadingDialog()
        GlobalScope.launch(Dispatchers.Main) {
            var fileSize = 0L
            try {
                if (isNetworkConnected()) {
                    fileSize = path.fileSize()
                }
                dialog.dismiss()
                startActivity<FileDisplayActivity>(Config.PATH to path, Config.SIZE to fileSize)
            } catch (e: Exception) {
                dialog.dismiss()
                startActivity<FileDisplayActivity>(Config.PATH to path, Config.SIZE to fileSize)
            }
        }
    } else {
        startActivity<X5WebViewActivity>()
    }
}

fun Context.loginExpired(msg: String? = Config.LOGIN_EXPIRED): Boolean {
    if (msg == Config.LOGIN_EXPIRED && ActivityManager.activityCount != 0) {
        if (Looper.myLooper() == Looper.getMainLooper()) toast(Config.LOGIN_EXPIRED)
        ActivityManager.finishAllActivity()
        //startActivity<LoginActivity>()
        SystemEnv.deleteLogin(this)
        //SystemEnv.deleteAuthority(this)
        val user = SystemEnv.getLogin(this) ?: return true
        //JPushInterface.deleteAlias(this, user.key)
        return true
    }
    return false
}
