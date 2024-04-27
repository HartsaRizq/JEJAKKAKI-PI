package com.folu.jejakkaki.ui.detail.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.folu.jejakkaki.databinding.FragmentInfoBinding
import com.folu.jejakkaki.model.TamanData
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tamanList = TamanData.taman
        val id = arguments?.getInt("id", 0)
        val selectedTaman = tamanList.find { it.id == id }
        val carousel: ImageCarousel = binding.carousel
        if (selectedTaman != null) {
            selectedTaman.info?.let { infoResId ->
                binding.content.text = getString(infoResId)
            }
        }

        carousel.registerLifecycle(lifecycle)


        val carouselItems = mutableListOf<CarouselItem>()

        selectedTaman?.car1?.let { car1ImageResourceId ->
            carouselItems.add(CarouselItem(imageUrl = car1ImageResourceId))
        }

        selectedTaman?.car2?.let { car2ImageResourceId ->
            carouselItems.add(CarouselItem(imageUrl = car2ImageResourceId))
        }

        selectedTaman?.car3?.let { car3ImageResourceId ->
            carouselItems.add(CarouselItem(imageUrl = car3ImageResourceId))
        }
        carousel.setData(carouselItems)


        if (selectedTaman?.facebook.isNullOrBlank()) {
            binding.btnFb?.visibility = View.GONE
            binding.txtFB.visibility = View.GONE
        } else {
            binding.txtFB.text = selectedTaman?.facebook
        }

        if (selectedTaman?.twitter.isNullOrBlank()) {
            binding.btnTwitter.visibility = View.GONE
            binding.txtTW.visibility = View.GONE
        } else {
            binding.txtTW.text = selectedTaman?.twitter
        }

        if (selectedTaman?.youtube.isNullOrBlank()) {
            binding.btnYt.visibility = View.GONE
            binding.txtYT.visibility = View.GONE
        } else {
            binding.txtYT.text = selectedTaman?.youtube
        }

        if (selectedTaman?.instagram.isNullOrBlank()) {
            binding.btnIg.visibility = View.GONE
            binding.txtIG.visibility = View.GONE
        } else {
            binding.txtIG.text = selectedTaman?.instagram
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(id: Int): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putInt("id", id)
            fragment.arguments = args
            return fragment
        }
    }
}