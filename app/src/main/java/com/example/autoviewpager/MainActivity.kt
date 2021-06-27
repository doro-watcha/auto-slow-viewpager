package com.example.autoviewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.example.autoviewpager.adapter.BlurredAdapter
import com.example.autoviewpager.adapter.MainImageAdapter
import com.example.autoviewpager.databinding.ActivityMainBinding
import com.example.autoviewpager.model.Event
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mBinding : ActivityMainBinding

    private val autoScrollDisposable = CompositeDisposable()

    var events : List<Event> = listOf(Event(0, R.drawable.sample_1),Event(1,R.drawable.sample_2), Event(2, R.drawable.sample_3))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        mBinding.lifecycleOwner = this
        mBinding.activity = this
        setContentView(mBinding.root)


        setupViewPager()
        setupBlurredImage()
    }

    private fun setupBlurredImage() {

        mBinding.mViewPagerBlurred.apply {

            adapter = BlurredAdapter()
            isUserInputEnabled = false
            offscreenPageLimit = 10

            this.registerOnPageChangeCallback( object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                 Log.d(TAG, "VIEWPAGER" + mBinding.mViewPager2.currentItem )

                    mBinding.mViewPager2.setCurrentItem(mBinding.mViewPager2.currentItem + 1, 800)
                }

            })

        }
    }

    private fun setupViewPager() {

        mBinding.mViewPager2.apply {


            adapter = MainImageAdapter().apply {


            }

            var centerValue =  Integer.MAX_VALUE / 2

            val findFirstPosition = centerValue % ( events.size)

            centerValue -= findFirstPosition


            setCurrentItem( centerValue , false )

            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
            this.setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
            this.setPadding(0, offsetPx, 0, offsetPx)


            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)


                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })

            offscreenPageLimit = 10
        }


    }

    private fun startAutoScroll () {


        autoScrollDisposable.clear()
        rxRepeatTimer(2000, {

            //sleep(5000)

            val position = mBinding.mViewPager2.currentItem % events.size

            Log.d(TAG, "rxRepestTImeer " + position.toString())

            mBinding.mViewPagerBlurred.setCurrentItem(
                mBinding.mViewPager2.currentItem % (events.size) + 1, false
            )




        }, 2000).disposedBy(autoScrollDisposable)


    }





    override fun onResume() {
        super.onResume()

        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()

        autoScrollDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

    }


}
