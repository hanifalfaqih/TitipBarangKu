package id.allana.titipbarangku.ui.setting.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class LanguageAdapter(private val dataList: List<String>, context: Context, private val onItemClick: (String) -> Unit) : ArrayAdapter<String>(
    context,
    android.R.layout.simple_list_item_1,
    dataList
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = dataList[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item

        view.setOnClickListener {
            onItemClick(item)
        }
        return view
    }
}