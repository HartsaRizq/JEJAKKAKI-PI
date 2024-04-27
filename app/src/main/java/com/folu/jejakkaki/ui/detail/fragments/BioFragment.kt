package com.folu.jejakkaki.ui.detail.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.folu.jejakkaki.databinding.FragmentBioBinding
import com.folu.jejakkaki.databinding.ItemCustomFixedSizeLayout3Binding
import com.folu.jejakkaki.model.TamanData
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.listener.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage

class BioFragment : Fragment() {
    private var _binding: FragmentBioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val carousel: ImageCarousel = binding.carousel
        val tamanList = TamanData.taman
        val id = arguments?.getInt("id", 0)
        val animalList = mutableListOf<CarouselItem>()
        val selectedTaman = tamanList.find { it.id == id }

        carousel.registerLifecycle(viewLifecycleOwner)
        carousel.carouselListener = object : CarouselListener {
            override fun onCreateViewHolder(
                layoutInflater: LayoutInflater,
                parent: ViewGroup
            ): ViewBinding {
                return ItemCustomFixedSizeLayout3Binding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }

            override fun onBindViewHolder(
                binding: ViewBinding,
                item: CarouselItem,
                position: Int
            ) {
                val currentBinding = binding as ItemCustomFixedSizeLayout3Binding

                currentBinding.imageView.apply {
                    setImage(item)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

        selectedTaman?.animals?.let { animals ->
            for (animal in animals) {
                animal?.desc?.let { descResourceId ->
                    val caption = requireContext().getString(descResourceId)
                    animal.pic.let {
                        animalList.add(
                            CarouselItem(
                                imageUrl = animal.pic,
                                caption = caption
                            )
                        )
                    }
                }
            }
        }
        carousel.setData(animalList)
        binding.caption.isSelected = true

        carousel.onScrollListener = object : CarouselOnScrollListener {

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    carouselItem?.apply {
                        binding.caption.text = caption
                    }
                }
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                // ...
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(id: Int): BioFragment {
            val fragment = BioFragment()
            val args = Bundle()
            args.putInt("id", id)
            fragment.arguments = args
            return fragment
        }
    }
}