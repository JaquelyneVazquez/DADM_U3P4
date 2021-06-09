package mx.edu.itm.link.dadm_u3p4.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.itm.link.dadm_u3p4.R
import mx.edu.itm.link.dadm_u3p4.models.Contact

class ContactsAdapter(val context: Context, val res: Int, val contacts:ArrayList<Contact>)
    : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(context).inflate(res, null)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(contact: Contact) {
            val imgPhoto = itemView.findViewById<ImageView>(R.id.imgRowPhoto)
            val textName = itemView.findViewById<TextView>(R.id.textRowName)
            val textCel = itemView.findViewById<TextView>(R.id.textRowCel)
            val imgFav = itemView.findViewById<ImageView>(R.id.imgRowFav)
            val fabOptions = itemView.findViewById<FloatingActionButton>(R.id.fabRowOptions)
            val fabEdit = itemView.findViewById<FloatingActionButton>(R.id.fabRowEdit)
            val fabDelete = itemView.findViewById<FloatingActionButton>(R.id.fabRowDelete)
            val fabCall = itemView.findViewById<FloatingActionButton>(R.id.fabRowCall)
            val fabMsg = itemView.findViewById<FloatingActionButton>(R.id.fabRowMsg)

            textName.text = contact.name
            textCel.text = contact.celphone

            contact.favorite?.let {
                imgFav.setImageResource(
                    if(it == 1) android.R.drawable.star_big_on
                    else android.R.drawable.star_big_off
                )
            }

            contact.photo?.let {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                imgPhoto.setImageBitmap(
                    Bitmap.createScaledBitmap(bmp, bmp.width, bmp.height, false)
                )
            }

            fabOptions.setOnClickListener {
                fabEdit.visibility = if(fabEdit.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
                fabDelete.visibility = if(fabDelete.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
                fabCall.visibility = if(fabCall.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
                fabMsg.visibility = if(fabMsg.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }
        }

    }

}