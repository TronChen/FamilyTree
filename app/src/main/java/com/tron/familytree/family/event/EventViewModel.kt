package com.tron.familytree.family.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.data.Event

class EventViewModel(
    private val repository: FamilyTreeRepository
) : ViewModel() {


    fun createMock(): List<Event> {
        val event1 = Event(
            publisher = "茶川川",
            title = "陳氏宗親水上活動",
            time = "2020/11/11 16:00",
            location = "中正紀念堂",
            content = "團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！"
        )
        val event2 = Event(
            publisher = "阿瓜",
            title = "陳氏宗親械鬥檢討",
            time = "2022/12/12 12:12",
            location = "市政府站",
            content = "這次因為工作上有使用 GPS 的需求，所以我打算做一個簡單的 GPS 的 DEMO 程式，由於 Android Developers Location strategies 的文件中大部分在探討使用 GPS 的策略，所以我打算一開始先簡單的介紹 Android 是如何取得座標的資訊。"
        )
        val event3 = Event(
            publisher = "咪咪",
            title = "陳氏宗親大會師",
            time = "2302/03/21 0:00",
            location = "茶川川餐廳",
            content = "有時候需要擷取藍芽的HCI log來查看傳送資訊，可以先在開發模式打開這個功能，但後來發現，網路上說的路徑怎麼都找不到HCI log 於是乎找了很久才找到一個比較專業的回答對了要先到Android開發模式把HCI收集打開有時候需要擷取藍芽的HCI log來查看傳送資訊，可以先在開發模式打開這個功能，但後來發現，網路上說的路徑怎麼都找不到HCI log 於是乎找了很久才找到一個比較專業的回答對了要先到Android開發模式把HCI收集打開"
        )
        val event4 = Event(
            publisher = "小阿姨",
            title = "陳氏宗親水上活動",
            time = "2020/12/12 12:12",
            location = "八仙樂園",
            content = "A TextureView can be used to display a content stream. Such a content stream can for instance be a video or an OpenGL scene. The content stream can come from the application's process as well as a remote process.TextureView can only be used in a hardware accelerated window. When rendered in software, TextureView will draw nothing. Unlike SurfaceView, TextureView does not create a separate window but behaves as a regular View. This key difference allows a TextureView to be moved, transformed, animated, etc. For instance, you can make a TextureView semi-translucent by calling myView.setAlpha(0.5f).Using a TextureView is simple: all you need to do is get its SurfaceTexture. The SurfaceTexture can then be used to render content. The following example demonstrates how to render the camera preview into a TextureView:"
        )
        val list = mutableListOf<Event>()
        list.add(event1)
        list.add(event2)
        list.add(event3)
        list.add(event4)
        list.add(event2)
        list.add(event3)
        list.add(event1)
        list.add(event2)
        list.add(event3)
        list.add(event4)

        return list
    }



}