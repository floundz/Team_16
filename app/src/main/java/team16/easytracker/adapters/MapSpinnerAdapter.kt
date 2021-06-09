package team16.easytracker.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.setPadding


/**
 * Adapted from https://android-er.blogspot.com/2014/04/spinner-with-different-displat-text-and.html
 */
class MapSpinnerAdapter (
    context: Context, textViewResourceId: Int,
    private val map: MutableMap<String, String>
) : ArrayAdapter<String>(context, textViewResourceId, map.values.toMutableList()) {

    val keys: MutableList<String> = map.keys.toMutableList()

    override fun getCount(): Int {
        return map.keys.size
    }

    override fun getItem(position: Int): String {
        return keys[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        super.getView(position, convertView, parent)
        val label = TextView(context)
        label.text = map[keys[position]]!!
        label.setPadding(30)
        return label
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        super.getDropDownView(position, convertView, parent!!)
        val label = TextView(context)
        label.text = map[keys[position]]!!
        label.setPadding(50)
        return label
    }

    fun add(key: String, value: String) {
        if (map.containsKey(key)) {
            throw IllegalArgumentException("Key '${key}' already exists!")
        }
        keys.add(key)
        map[key] = value
        super.add(value)
    }

    fun remove(key: String) {
        keys.remove(key)
        map.remove(key)
        super.remove(key)
    }

    fun contains(key: String) : Boolean {
        return map.contains(key)
    }
}