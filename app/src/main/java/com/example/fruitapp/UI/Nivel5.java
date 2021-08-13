package com.example.fruitapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitapp.AdminSQLiteOpenHelper;
import com.example.fruitapp.R;
import com.example.fruitapp.utilidades.utilidades;

public class Nivel5 extends AppCompatActivity {

    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_vidas;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;
    Button btn_PlayPause;

    int score;
    int numAleatorio_uno, numAleatorio_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;

    String numero [] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel5);

        Toast.makeText(this, R.string.toast_nivel5, Toast.LENGTH_SHORT).show();

        tv_nombre = findViewById(R.id.textView_nombre);
        tv_score = findViewById(R.id.textView_score);
        iv_vidas = findViewById(R.id.imageView_vidas);
        iv_Auno = findViewById(R.id.imageView_NumUno);
        iv_Ados = findViewById(R.id.imageView_NumDos);
        et_respuesta = findViewById(R.id.editText_resultado);

        //recuperando elnombre del jugador y guardandolo en la variable designada
        nombre_jugador = getIntent().getStringExtra("Jugador");
        //Indicandole donde va a mostar el nombre del jugador
        tv_nombre.setText("Jugador: " + nombre_jugador);

        //Recuperando el score
        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("score: " + score);

        //Recuperando las vidas (manzanas)
        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);

        if (vidas == 3) {
            iv_vidas.setImageResource(R.drawable.tresvidas);
        } if (vidas == 2){
            iv_vidas.setImageResource(R.drawable.dosvidas);
        } if (vidas ==1){
            iv_vidas.setImageResource(R.drawable.unavida);
        }

        //agregandole icono al action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Poniendo el fondo musical del activity
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true); //Para ciclar el audio

        //Audio cuando la respuesta fue correcta
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        //Audio cuando la respuesta fue incorrecta
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        // Ejecutando el metodo Numero aleatorio
        NumAleatorio();
    }

    public void comparar (View view) {

        //Recuperando la respuesta del usuario
        String respuesta =  et_respuesta.getText().toString();

        //Validando que haya una respuesta (Si la respuesta es diferente a un espacio en blanco, fue que se introdujo info
        if(!respuesta.equals("")){

            //Guardando respuesta en una variable tipo int
            int respuesta_jugador = Integer.parseInt(respuesta);

            if (resultado == respuesta_jugador){
                //ejecutando audio de respuesta correcta
                mp_great.start();
                //aumentando el score por respuesta correcta
                score++;
                //Mostrando el valor del score actualizado en la pantalla Nivel 1
                tv_score.setText("Score: " + score);
                //Limpiando el  editText_resultado
                et_respuesta.setText("");
                BaseDeDatos();

            } else {

                //Ejecutando el audio de respuesta incorrecta
                mp_bad.start();
                //Las vidas se tiene que decrementar
                vidas--;
                BaseDeDatos();

                //Programar que va a pasar dependiendo de la cantidad de vidas que tenga el jugador
                switch (vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, R.string.toast_dos_manzanas, Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, R.string.toast_una_manzana, Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, R.string.toast_lost, Toast.LENGTH_SHORT).show();

                        //Regresando a la pantalla de bienvenida
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent); //Abre la otra activity
                        finish(); //Finaliza esta activity
                        mp.stop(); //Finaliza el audio
                        mp.release(); //Libera recursos
                        break;
                }
                et_respuesta.setText(""); //Limpiando el  editText_resultado

            }

            //Actualizando los numeros aleatorios
            NumAleatorio();

        } else {
            Toast.makeText(this, R.string.toast_null_respuesta, Toast.LENGTH_SHORT).show();
        }
    }

    public  void NumAleatorio (){

        //Con esta estructura condicional se pasara de niveles
        if ( score <= 49 ){

            //La clase Math solo retorna resultados de tipo double por lo que hay q realizar un casting y convertirlo en int
            numAleatorio_uno = (int) (Math.random() * 10);
            numAleatorio_dos = (int) (Math.random() * 10);

            resultado = numAleatorio_uno * numAleatorio_dos ;


            for (int i = 0; i < numero.length; i++) {

                //Para obtener el id de las imagenes que se van a sumar
                int id = getResources().getIdentifier(numero [i], "drawable", getPackageName());

                // Para indicar que imagen va a corresponder a cada imageView
                if (numAleatorio_uno == i){
                    iv_Auno.setImageResource(id);

                } if (numAleatorio_dos == i) {
                    iv_Ados.setImageResource(id);
                }
            }

            //Cuando el score es mayor de 10 se debe de cambiar de nivel y pasar a la siguinete activity
        } else {

            Intent intent = new Intent (this, Nivel6.class);

            //Pasando el dato del score y de vidas y del nombre del jugador al activity Nivel2

            //string_score es una variable de tipo string y score es de tipo entero, por lo que se usa String.valueOf
            string_score = String.valueOf(score);
            //Lo mismo pasa con la variable vidas
            string_vidas = String.valueOf(vidas);

            intent.putExtra("Jugador", nombre_jugador);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_vidas);

            startActivity(intent);
            finish(); //Finalizando el activity del Nivel 1
            mp.stop(); //deteniendo los audios
            mp.release(); //destruyendo el objeto de la clase mp y liberar recursos
            mp = null;
        }
    }

    //Metodo de base de datos
    public void BaseDeDatos (){

        //Registro de usuario
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        String insert = "INSERT INTO " + utilidades.TABLA_PUNTAJE + "(" + utilidades.CAMPO_NOMBRE + " , " + utilidades.CAMPO_SCORE + ")" + " VALUES ( ' " + nombre_jugador + " ' ,  " + score + " )";
        BD.execSQL(insert);

        // ContentValues values = new ContentValues();
        // values.put (utilidades.CAMPO_NOMBRE, nombre_jugador);
        // values.put (utilidades.CAMPO_SCORE, score);
        // Long idResultante = BD.insert(utilidades.TABLA_PUNTAJE, utilidades.CAMPO_SCORE, values);
        // BD.close();

        try {
            Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max (score) from puntaje)", null);

            //Comprueba si hay datos
            consulta.moveToFirst();

            //Devuelve los valores
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            //Llevando el temp-score de srting a entero para poder realizar comparaciones
            int bestScore = Integer.parseInt(temp_score);

            //Comprobando que el score atual sea mayor que el bestscore, si fuera asi, modifica el valor de nombre del jugador y el score
            if (score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put(utilidades.CAMPO_NOMBRE, nombre_jugador);
                modificacion.put(utilidades.CAMPO_SCORE, score);

                //Modificando los valores dentro de la base de datos
                BD.update("puntaje", modificacion, "score" + bestScore, null);
                BD.close();

            } else {

                ContentValues insertar = new ContentValues();

                insertar.put ("nombre", nombre_jugador);
                insertar.put("score", score);

                BD.insert("puntaje", null, insertar);
                BD.close();
            }

        } catch (Exception e) {
            BD.close();
        }
    }


    //Control del boton back (Para que no se pueda regresar a la pantalla d bienvenida)
    @Override
    public void onBackPressed () {
        //Como no tiene nada programado, no hara nada
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

    //Metodo boton pause
    public void PlayPause (View view) {

        if (mp.isPlaying()) {
            mp.pause();
            btn_PlayPause.setBackgroundResource(R.drawable.sinmusic);
        } else {
            mp.start();
            mp.setLooping(true);
            btn_PlayPause.setBackgroundResource(R.drawable.music);
        }
    }
}