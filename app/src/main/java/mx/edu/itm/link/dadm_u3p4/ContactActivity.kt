package mx.edu.itm.link.dadm_u3p4

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.ToggleButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.itm.link.dadm_u3p4.models.Contact
import mx.edu.itm.link.dadm_u3p4.models.DBManager
import mx.edu.itm.link.dadm_u3p4.utils.MyUtils.Companion.toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class ContactActivity : AppCompatActivity() {

    lateinit var imgContact : ImageView
    lateinit var fabEditPhoto : FloatingActionButton
    lateinit var editName : EditText
    lateinit var editCel : EditText
    lateinit var tgBtnFav : ToggleButton
    lateinit var btnCancelar : ExtendedFloatingActionButton
    lateinit var btnGuardar : ExtendedFloatingActionButton

    var byteFoto : ByteArray? = null

    lateinit var manager : DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        imgContact = findViewById(R.id.imgContact )
        fabEditPhoto = findViewById( R.id.fabEditPhoto )
        editName = findViewById( R.id.editName )
        editCel = findViewById( R.id.editCel )
        tgBtnFav = findViewById( R.id.tgBtnFav )
        btnCancelar = findViewById( R.id.btnCancelar )
        btnGuardar = findViewById( R.id.btnGuardar )

        lateinit var manager : DBManager

        //var edit: Boolean = false

        manager = DBManager(
            this,
            resources.getString(R.string.db_name),
            null,
            resources.getInteger(R.integer.db_version)
        )

        /*var contacto: Contact? = null
        if (intent.extras != null){
            contacto = intent.getParcelableExtra("contacto")
            edit = true
        }
        if (edit == true){
            editName.setText(contacto?.name)
            editCel.setText(contacto?.celphone)
            contacto?.photo?.let {
                imgContact.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
        }*/


        fabEditPhoto.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, 1)
        }

        tgBtnFav.setOnClickListener {
            if(tgBtnFav.isSelected) {
                tgBtnFav.textOn = "Es favorito"
                tgBtnFav.setCompoundDrawablesWithIntrinsicBounds(
                    null, resources.getDrawable(android.R.drawable.star_big_on), null, null
                )
            } else {
                tgBtnFav.textOff = "No es favorito"
                tgBtnFav.setCompoundDrawablesWithIntrinsicBounds(
                    null, resources.getDrawable(android.R.drawable.star_big_off), null, null
                )
            }
        }

        btnCancelar.setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            var validated = true
            if(editName.text.isEmpty()) {
                validated = false
                editName.setError("El nombre es requerido")
            }
            if(editCel.text.isEmpty()) {
                validated = false
                editCel.setError("El celular es requerido")
            }
            // Si todo es correcto
            if(validated) {
                val contacto = Contact(
                    0,
                    editName.text.toString(),
                    editCel.text.toString(),
                    if(tgBtnFav.isSelected) 1 else 0,
                    byteFoto
                )
                // Se guarda en la BD
                try {
                    manager.create(contacto)

                    "Se agrego el contacto ${editName.text}".toast(this)
                } catch (e: Exception) {
                    e.printStackTrace()

                    "Error al crear el contacto".toast(this)
                }
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            data?.let {
                val uri = it.data
                val baos = ByteArrayOutputStream()
                try {
                    it.data?.let {
                        val fis = contentResolver.openInputStream(it)

                        fis?.let {
                            val buf = ByteArray(1024)
                            do {
                                val n = it.read(buf)
                                if(n != -1) {
                                    baos.write(buf, 0, n)
                                } else break
                            } while (true)

                            byteFoto = baos.toByteArray()
                        }
                    }

                    // Actualizar imageView
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    imgContact.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

}