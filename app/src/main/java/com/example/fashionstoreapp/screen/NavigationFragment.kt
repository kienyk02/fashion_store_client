package com.example.fashionstoreapp.screen

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fashionstoreapp.databinding.FragmentNavigationBinding
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fashionstoreapp.R
import com.example.fashionstoreapp.data.model.ExpendedMenuModel
import com.example.fashionstoreapp.data.network.JwtManager
import com.example.fashionstoreapp.databinding.NavHeaderBinding
import com.example.fashionstoreapp.screen.adapter.ExpandableListAdapter
import com.example.fashionstoreapp.screen.adapter.MyPagerAdapter
import com.example.fashionstoreapp.screen.product.SearchFragment
import com.example.fashionstoreapp.screen.setting.ProfileFragment
import com.example.fashionstoreapp.screen.setting.SettingFragment
import com.example.fashionstoreapp.screen.viewmodel.ProductsViewModel
import com.example.fashionstoreapp.screen.viewmodel.SearchViewModel
import com.example.fashionstoreapp.screen.viewmodel.UserViewModel


class NavigationFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentNavigationBinding
    private lateinit var navHeaderBinding: NavHeaderBinding

    private val searchViewModel by lazy {
        ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(requireActivity().application)
        )[UserViewModel::class.java]
    }

    private val controller by lazy {
        findNavController()
    }

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var titleList: List<ExpendedMenuModel>
    private lateinit var dataList: HashMap<ExpendedMenuModel, List<ExpendedMenuModel>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNavigationBinding.inflate(inflater, container, false)
        val headerView = binding.navigationView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)

        initListData()
        getUserInfo()
        setUptViewPager()
        setUptNavigationMenu()
        setUpExpandableView()
        setUpBottomNavigation()
        handleSearch()
        binding.fabCart.setOnClickListener {
            controller.navigate(R.id.action_navigationFragment_to_cartFragment)
        }
        binding.btnLogout.setOnClickListener {
            signOut()
        }

        return binding.root
    }

    private fun initListData() {
        titleList = listOf(
            ExpendedMenuModel(1, "Áo nam", R.drawable.tshirt),
            ExpendedMenuModel(2, "Quần nam", R.drawable.trouser),
            ExpendedMenuModel(3, "Phụ kiện", R.drawable.accessory),
            ExpendedMenuModel(4, "Lịch sử đặt hàng", R.drawable.truck),
        )
        dataList = HashMap()

        val subItems = listOf(
            ExpendedMenuModel(1, "Áo nam xuân hè", R.drawable.ic_launcher_foreground),
            ExpendedMenuModel(2, "Áo thun nam", R.drawable.ic_launcher_foreground),
            ExpendedMenuModel(3, "Áo sơ mi nam", R.drawable.ic_launcher_foreground)
        )

        dataList[titleList[0]] = subItems
        dataList[titleList[1]] = subItems
        dataList[titleList[2]] = emptyList()
        dataList[titleList[3]] = emptyList()
    }

    private fun getUserInfo() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.id != 0){
                val imageBytes = Base64.decode(it.avatarImage, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                navHeaderBinding.userAvt.setImageBitmap(decodedImage)
                navHeaderBinding.userName.text = it.name
            }
        })
        viewModel.getUserInfo()
    }

    private fun setUptViewPager() {
        val fragments = listOf(
            HomeFragment(),
            ProfileFragment(),
            ProfileFragment(),
            ProfileFragment(),
            SettingFragment(),
            SearchFragment()
        )
        val pagerAdapter = MyPagerAdapter(requireActivity(), fragments)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = false
    }

    private fun setUptNavigationMenu() {
        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(
            requireActivity(),
            R.color.primary_color
        )
    }

    private fun setUpExpandableView() {
        expandableListAdapter = ExpandableListAdapter(requireContext(), titleList, dataList)
        binding.expandableListView.setAdapter(expandableListAdapter)

        binding.expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            Toast.makeText(
                requireContext(),
                "Clicked: " + expandableListAdapter.getChild(groupPosition, childPosition),
                Toast.LENGTH_SHORT
            ).show()

            false
        }

        binding.expandableListView.setOnGroupClickListener { _, _, groupPosition, _ ->
            if (expandableListAdapter.getChildrenCount(groupPosition) == 0) {
                val item = expandableListAdapter.getGroup(groupPosition) as ExpendedMenuModel
                Toast.makeText(
                    requireContext(),
                    "Clicked on group: $item", Toast.LENGTH_SHORT
                ).show()
            }
            if (groupPosition == titleList.size - 1)
                controller.navigate(R.id.action_navigationFragment_to_orderHistoryFragment)
            false
        }
    }

    private fun setUpBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    if (binding.viewPager.currentItem == 5) binding.viewPager.setCurrentItem(
                        0,
                        false
                    )
                    else binding.viewPager.currentItem = 0
                }

                R.id.nav_setting -> {
                    binding.viewPager.currentItem = 4
                }
            }
            true
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position < 5) binding.bottomNav.menu.getItem(position).isChecked = true
                if (position == 0 || position == 5) binding.header.visibility = View.VISIBLE
                else binding.header.visibility = View.GONE
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> binding.viewPager.currentItem = 0
        }
        binding.drawerLayout.closeDrawer(binding.navigationView)
        return true
    }

    private fun handleSearch() {
        binding.btnSearch.setOnClickListener {
            binding.searchHeader.visibility = View.VISIBLE
            binding.toolbar.visibility = View.GONE
        }
        binding.btnClose.setOnClickListener {
            binding.searchHeader.visibility = View.GONE
            binding.toolbar.visibility = View.VISIBLE
        }

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val key: String = binding.edtSearch.query.toString()

                if (binding.viewPager.currentItem != 5) binding.viewPager.setCurrentItem(5, false)
                binding.searchHeader.visibility = View.GONE
                binding.toolbar.visibility = View.VISIBLE
                searchViewModel.updateKey(key)
                binding.edtSearch.requestFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

    }

    private fun signOut() {
        JwtManager.removeJwtToken(requireContext())
        JwtManager.CURRENT_JWT = JwtManager.getJwtToken(requireActivity().application)
        controller.navigate(R.id.action_navigationFragment_to_loginFragment)
    }
}
