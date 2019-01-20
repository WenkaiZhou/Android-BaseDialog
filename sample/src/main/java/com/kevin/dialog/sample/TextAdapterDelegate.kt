package com.kevin.dialog.sample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kevin.delegationadapter.extras.ClickableAdapterDelegate
import com.kevin.dialog.sample.TextAdapterDelegate.ViewHolder

/**
 * TextAdapterDelegate
 *
 * @author zwenkai@foxmail.com, Created on 2019-01-20 18:09:29
 *         Major Function：<b></b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */
class TextAdapterDelegate(val activity: MainActivity) : ClickableAdapterDelegate<String, ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: String) {
        super.onBindViewHolder(holder, position, item)
        holder.tvName.text = item
    }

    override fun onItemClick(view: View, item: String, position: Int) {
        activity.onItemClick(view, item, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(android.R.id.text1)
    }
}