package by.supruniuk.alisa.meditationapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter (fragmentActivity:FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

//    override fun getItem(position: Int): Fragment {
//
//        return mFragmentList[position]
//    }
//
//    override fun getCount(): Int {
//        return mFragmentList.size
//    }
//
//    override fun getPageTitle(position: Int): CharSequence = mFragmentTitleList[position]
//
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}