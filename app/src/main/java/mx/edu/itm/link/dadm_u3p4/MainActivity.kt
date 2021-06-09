package mx.edu.itm.link.dadm_u3p4


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.itm.link.dadm_u3p4.adapters.ContactsAdapter
import mx.edu.itm.link.dadm_u3p4.models.Contact
import mx.edu.itm.link.dadm_u3p4.models.DBManager


class MainActivity : AppCompatActivity() {

    lateinit var fabAgregar : FloatingActionButton
    lateinit var recyclerContactos : RecyclerView
    lateinit var editSearch : EditText

    lateinit var nTelefono : TextView
    lateinit var fabLlamada: FloatingActionButton

    lateinit var manager : DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAgregar = findViewById(R.id.fabAgregar)
        recyclerContactos = findViewById(R.id.recyclerContactos)
        editSearch = findViewById(R.id.editSearch)

        fabLlamada = findViewById(R.id.fabRowCall)
        nTelefono = findViewById(R.id.textRowCel)


        manager = DBManager(
            this,
            resources.getString(R.string.db_name),
            null,
            resources.getInteger(R.integer.db_version)
        )

        var contact: Contact? = intent.getParcelableExtra("contact")

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    refreshContacts()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        fabAgregar.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

        fabLlamada.setOnClickListener {
            val numero : String = String.format("Tel: ${contact?.celphone}")
            val llamada = Intent(Intent.ACTION_DIAL);
            llamada.setData(Uri.parse(numero))
            ContextCompat.startActivity(this, llamada, null)
        }


        refreshContacts()
    }

    private fun refreshContacts() {
        try {
            val contacts = manager.find(editSearch.text)

            recyclerContactos.adapter = ContactsAdapter(this,R.layout.recylcer_row_contacts, contacts)
            recyclerContactos.layoutManager = LinearLayoutManager(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}