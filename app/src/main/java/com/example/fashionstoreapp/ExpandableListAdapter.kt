package com.example.fashionstoreapp

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import com.example.fashionstoreapp.databinding.ListParentItemBinding
import com.example.fashionstoreapp.databinding.ListSubItemBinding

class ExpandableListAdapter(
    private val context: Context,
    private val titleList: List<ExpendedMenuModel>,
    private val dataList: HashMap<ExpendedMenuModel, List<ExpendedMenuModel>>
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return dataList[titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return dataList[titleList[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return titleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val binding: ListParentItemBinding
        if (convertView == null) {
            binding = ListParentItemBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            binding = ListParentItemBinding.bind(convertView)
        }
        val item = getGroup(listPosition) as ExpendedMenuModel
        binding.parentItem.text = item.title
        binding.iconImage.setImageResource(item.iconImg)

        if (getChildrenCount(listPosition) == 0) {
            binding.expandIcon.visibility = View.GONE
        } else {
            binding.expandIcon.visibility = View.VISIBLE
            if (isExpanded && binding.expandIcon.rotation == 0f) {
                val rotateAnimation =
                    ObjectAnimator.ofFloat(binding.expandIcon, "rotation", 0f, 180f)
                rotateAnimation.duration = 300
                rotateAnimation.start()
                binding.iconImage.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.primary_color
                    ), PorterDuff.Mode.SRC_IN
                )
                binding.parentItem.setTextColor(context.resources.getColor(R.color.primary_color))
            } else if (!isExpanded && binding.expandIcon.rotation == 180f) {
                val rotateAnimation =
                    ObjectAnimator.ofFloat(binding.expandIcon, "rotation", 180f, 0f)
                rotateAnimation.duration = 300
                rotateAnimation.start()
                binding.iconImage.setColorFilter(
                    ContextCompat.getColor(context, R.color.black),
                    PorterDuff.Mode.SRC_IN
                )
                binding.parentItem.setTextColor(context.resources.getColor(R.color.black))
            }
        }
        return binding.root
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val binding: ListSubItemBinding
        if (convertView == null) {
            binding = ListSubItemBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            binding = ListSubItemBinding.bind(convertView)
        }
        val item = getChild(listPosition, expandedListPosition) as ExpendedMenuModel
        binding.subItem.text = item.title
        return binding.root
    }
}