package com.dutycam.app.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.dutycam.app.databinding.GallerySlideBinding

class GallerySlide(val binding: GallerySlideBinding) : RecyclerView.ViewHolder(binding.root) {
    @Volatile var currentPostion = 0
}
