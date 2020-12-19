package com.tron.familytree.family.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.data.Event
import com.tron.familytree.data.Photo
import com.tron.familytree.databinding.FragmentAlbumBinding
import com.tron.familytree.ext.getVmFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.times


class AlbumFragment(val position : Int) : Fragment() {

    private val viewModel by viewModels<AlbumViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter =  AlbumAdapter(AlbumAdapter.AlbumOnItemClickListener{
            findNavController().navigate(R.id.action_global_albumDetailFragment)
        })
        binding.recyclerAlbum.adapter = adapter


        adapter.submitList(createMock())

        viewModel.liveEvent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                it.forEach { event ->
                    event.eventTime
                    val aaa = Calendar.getInstance().timeInMillis
                    if ( aaa < event.eventTime!! ) {
                        //即將到來的活動  爲以結束的

                    }
                    if (aaa > event.eventTime!!){
                        //以結束的活動

                    }
                }
            }
        })

        val aaa = Calendar.getInstance().before(Calendar.getInstance())



        return binding.root
    }

    fun createMock() : List<Photo>{
        val event1 = Event(
            publisher = "茶川川",
            title = "陳氏宗親水上活動",
            time = "2020/11/11 16:00",
            location = "中正紀念堂",
            content = "團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！團結大家凝聚力，為下一場械鬥奮戰！"
        )
        val album1 = Photo(
            publisher = "茶川川",
            title = "陳氏宗親水上活動",
            event = event1
        )
        val event2 = Event(
            publisher = "阿瓜",
            title = "陳氏宗親械鬥檢討",
            time = "2022/12/12 12:12",
            location = "市政府站",
            content = "這次因為工作上有使用 GPS 的需求，所以我打算做一個簡單的 GPS 的 DEMO 程式，由於 Android Developers Location strategies 的文件中大部分在探討使用 GPS 的策略，所以我打算一開始先簡單的介紹 Android 是如何取得座標的資訊。"
        )
        val album2 = Photo(
            publisher = "阿瓜",
            title = "陳氏宗親械鬥檢討",
            event = event2
        )
        val event3 = Event(
            publisher = "咪咪",
            title = "陳氏宗親大會師",
            time = "2302/03/21 0:00",
            location = "茶川川餐廳",
            content = "有時候需要擷取藍芽的HCI log來查看傳送資訊，可以先在開發模式打開這個功能，但後來發現，網路上說的路徑怎麼都找不到HCI log 於是乎找了很久才找到一個比較專業的回答對了要先到Android開發模式把HCI收集打開有時候需要擷取藍芽的HCI log來查看傳送資訊，可以先在開發模式打開這個功能，但後來發現，網路上說的路徑怎麼都找不到HCI log 於是乎找了很久才找到一個比較專業的回答對了要先到Android開發模式把HCI收集打開"
        )
        val album3 = Photo(
            publisher = "咪咪",
            title = "陳氏宗親大會師",
            event = event3
        )
        val event4 = Event(
            publisher = "小阿姨",
            title = "陳氏宗親水上活動",
            time = "2020/12/12 12:12",
            location = "八仙樂園",
            content = "A TextureView can be used to display a content stream. Such a content stream can for instance be a video or an OpenGL scene. The content stream can come from the application's process as well as a remote process.TextureView can only be used in a hardware accelerated window. When rendered in software, TextureView will draw nothing. Unlike SurfaceView, TextureView does not create a separate window but behaves as a regular View. This key difference allows a TextureView to be moved, transformed, animated, etc. For instance, you can make a TextureView semi-translucent by calling myView.setAlpha(0.5f).Using a TextureView is simple: all you need to do is get its SurfaceTexture. The SurfaceTexture can then be used to render content. The following example demonstrates how to render the camera preview into a TextureView:"
        )
        val album4 = Photo(
            publisher = "小阿姨",
            title = "陳氏宗親水上活動",
            event = event4
        )

        val list = mutableListOf<Photo>()
        list.add(album1)
        list.add(album2)
        list.add(album3)
        list.add(album4)
        list.add(album1)
        list.add(album2)
        list.add(album3)
        list.add(album4)
        list.add(album1)
        list.add(album2)
        list.add(album3)

        return list
    }
}