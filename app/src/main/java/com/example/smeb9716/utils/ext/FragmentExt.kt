package com.example.smeb9716.utils.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.example.smeb9716.R
import com.example.smeb9716.foundation.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

fun Fragment.goBackUntilRootFragment() {
    activity?.supportFragmentManager?.apply {
        while (backStackEntryCount > 0) {
            popBackStackImmediate()
        }
    }
}

fun Fragment.goBackUtilHomeFragment() {
    (requireActivity() as BaseActivity<*>).showBottomNavigation()
    activity?.supportFragmentManager?.apply {
        while (backStackEntryCount > 0) {
            popBackStackImmediate()
        }
    }
}

fun BaseActivity<*>.shouldShowBottomNavigation() {
    val backstackCount = supportFragmentManager.backStackEntryCount
    if (backstackCount > 1) {
        hideBottomNavigation()
    } else {
        showBottomNavigation()
    }
}

fun BaseActivity<*>.hideBottomNavigation() {
    findViewById<BottomNavigationView>(R.id.bottom_navigation)?.gone()
}

fun BaseActivity<*>.showBottomNavigation() {
    findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visible()
}

fun Fragment.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = fragment::class.java.simpleName,
) {
    val backstackCount =
        (requireActivity() as BaseActivity<*>).supportFragmentManager.backStackEntryCount

    Timber.d("Backstack BEFORE ADD count: $backstackCount")

    if (backstackCount >= 0) {
        (requireActivity() as BaseActivity<*>).hideBottomNavigation()
    } else {
        (requireActivity() as BaseActivity<*>).showBottomNavigation()
    }

    activity?.supportFragmentManager?.apply {
        beginTransaction().apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
            add(containerId, fragment, tag)
        }.commit()
    }

}

fun Fragment.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean,
    tag: String? = fragment::class.java.simpleName,
) {
    activity?.supportFragmentManager?.apply {
        beginTransaction().apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
            replace(containerId, fragment, tag)
        }.commit()
    }
}

fun BaseActivity<*>.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean,
    tag: String? = fragment::class.java.simpleName,
) {
    supportFragmentManager.apply {
        beginTransaction().apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
            replace(containerId, fragment, tag)
        }.commit()
    }

    val backstackCount2 = supportFragmentManager.backStackEntryCount

    Timber.d("Backstack REPLACE count: $backstackCount2")
}

fun Fragment.goBackFragment(): Boolean {
    val backstackCount =
        (requireActivity() as BaseActivity<*>).supportFragmentManager.backStackEntryCount

    if (backstackCount > 1) {
        (requireActivity() as BaseActivity<*>).hideBottomNavigation()
    } else {
        (requireActivity() as BaseActivity<*>).showBottomNavigation()
    }

    activity?.supportFragmentManager?.notNull {
        val isShowPreviousPage = it.backStackEntryCount > 0
        if (isShowPreviousPage) {
            it.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
    return false
}
