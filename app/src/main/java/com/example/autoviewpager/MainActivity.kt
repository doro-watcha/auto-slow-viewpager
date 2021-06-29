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

    var events : List<Event> = listOf(Event(0, R.drawable.sample1),Event(1,R.drawable.sample2), Event(2, R.drawable.sample3),
        Event(3, R.drawable.sample4),Event(4,R.drawable.sample5), Event(5, R.drawable.sample6),Event(6,R.drawable.sample7)
    )
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
            this.setPadding(0, offsetPx, 0, 0)


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

                    Log.d(TAG, "position " + (position % events.size).toString())
                    Log.d(TAG, "blur = " + mBinding.mViewPagerBlurred.currentItem.toString())




                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)

                    when ( state ) {
                        ViewPager2.SCROLL_STATE_IDLE -> {

                            scrollToNext()

                            val position = mBinding.mViewPager2.currentItem
                            if (position % events.size != (mBinding.mViewPagerBlurred.currentItem ) ) {

                                mBinding.mViewPagerBlurred.setCurrentItem(
                                    position % events.size,
                                    false
                                )
                                mBinding.pageIndicator.refresh(position % events.size)
                            }


                        }
                        ViewPager2.SCROLL_STATE_DRAGGING -> {
                            autoScrollDisposable.clear()
                        }
                    }
                }
            })


            offscreenPageLimit = 10
        }


    }

    private fun scrollToNext () {

        Log.d(TAG, "Scroll To next")


        autoScrollDisposable.clear()
        rxSingleTimer(2000) {

            val position = mBinding.mViewPager2.currentItem + 1

            mBinding.pageIndicator.refresh( position % events.size )
            mBinding.mViewPagerBlurred.setCurrentItem(position % events.size, false)
            mBinding.mViewPager2.setCurrentItem(position, 600)

        }.disposedBy(autoScrollDisposable)


    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume")

        scrollToNext()

    }


    override fun onPause() {
        super.onPause()

        autoScrollDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        autoScrollDisposable.dispose()

    }


}
