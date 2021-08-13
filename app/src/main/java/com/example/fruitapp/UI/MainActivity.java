package com.example.fruitapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitapp.AdminSQLiteOpenHelper;
import com.example.fruitapp.R;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    public TextView tv_bestScore;
    private MediaPlayer mp;

    //Generando números aleatorios del 0 al 10
    int num_aleatorio = (int) (Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeneralConfig();
        AnimatePersonaje();
        IconoApp();
        MusicaDeFondo();
        BaseDeDatos();
    }

    private void GeneralConfig() {

        et_nombre = findViewById(R.id.txt_nombre);
        //Para que la primera letra salga en mayuscula
        et_nombre.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //iv_personaje = findViewById(R.id.imageView_personaje);
        tv_bestScore = findViewById(R.id.textView_bestScore);
    }

    private void AnimatePersonaje() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shakeanimation);
        iv_personaje = findViewById(R.id.imageView_personaje);

        //Cambio de personaje de bienvenida
        int id;
        if (num_aleatorio == 0 || num_aleatorio == 10) {
            //Obtengo ruta e id de la imagen
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            //Guardandolo dentro del imageView
            iv_personaje.setImageResource(id);

        } else if (num_aleatorio == 1 || num_aleatorio == 9) {
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            iv_personaje.setImageResource(id);

        } else if (num_aleatorio == 2 || num_aleatorio == 8) {
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            iv_personaje.setImageResource(id);

        } else if (num_aleatorio == 3 || num_aleatorio == 7) {
            //Obtengo ruta e id de la imagen
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            //Guardandolo dentro del imageView
            iv_personaje.setImageResource(id);

        } else if (num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6) {
            //Obtengo ruta e id de la imagen
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            //Guardandolo dentro del imageView
            iv_personaje.setImageResource(id);
        }
        iv_personaje.setAnimation(shake);
    }

    private void IconoApp() {
        //Para poner el icono de la app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void MusicaDeFondo() {
        //Música de fondo
        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
    }

    private void BaseDeDatos() {
        //* LA BASE DE DATOS SE LLAMA "BD" Y LA TABLA SE LLAMA "puntaje" *
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        //  try {
        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max (score) from puntaje)", null);

        //Nos aseguramos de que existe al menos un registro
        if (consulta.moveToFirst()) {
            //Recuperando el nombre que está en la columna 1, sería posición cero
            String temp_nombre = consulta.getString(0);
            //Recuperando el score q está en la columna 2, seria posición uno
            String temp_score = consulta.getString(1);
            tv_bestScore.setText("Record: " + temp_nombre + "con Score de " +  temp_score );
            BD.close();

        } else {
            BD.close();
        }
    }

    //Método Boton Jugar
    public void Jugar (View view){
        //Ncesitamos asegurarnos que se escriba un nombre
        //Por lo q creamos una variable donde guardamos la info de ese editText
        //Y lo convertimos a string
        String nombre  = et_nombre.getText().toString();

        //Si nombre es diferente a un espacio vacio
        if (!nombre.equals("")){
            mp.stop();
            mp.release();
            mp = null;

            //Inicializando Activity Nivel 1
            Intent intent = new Intent(this, Nivel1.class);

            //Pasando los datos al Activity Nivel 1
            intent.putExtra("Jugador", nombre);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, R.string.toast_faltaNombre, Toast.LENGTH_SHORT).show();

            //Para que el teclado se abra cuando se posicione en el EditText nombre
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    //Método para el botón Back
    // Override se utiliza para sobrescribir un método
    @Override
    public void onBackPressed(){
    }

    //Con estos métodos se deja de reprpoducir la pista de mp una vez que minimizas la aplicación
    @Override
    protected void onResume() {
        super.onResume();
        if(mp != null) {
            mp.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp != null){
            mp.pause();
        }
    }
}