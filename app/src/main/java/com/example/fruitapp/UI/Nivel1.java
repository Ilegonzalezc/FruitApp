package com.example.fruitapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fruitapp.AdminSQLiteOpenHelper;
import com.example.fruitapp.AumentarScore;
import com.example.fruitapp.DisminuirVidas;
import com.example.fruitapp.R;
import com.example.fruitapp.ViewModel.ScoreViewModel;
import com.example.fruitapp.ViewModel.VidasViewModel;
import com.example.fruitapp.utilidades.utilidades;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class Nivel1 extends AppCompatActivity {

    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_vidas;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    private ScoreViewModel scoreViewModel;
    private VidasViewModel vidasViewModel;

    int numAleatorio_uno, numAleatorio_dos, resultado, i;
    int vida;

    String nombre_jugador, string_score, string_vidas;

    String numero [] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel1);

        GeneralConfig();
        NombreyScore();
        IconActionBar();
        MusicaDeFondo();
        floatingactionbutton();
        NumAleatorio();
    }

    private void GeneralConfig() {
        Toast.makeText(this, R.string.toast_nivel1, Toast.LENGTH_SHORT).show();

        //Inicializando nuestras clases ViewModel
        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        vidasViewModel = ViewModelProviders.of(this).get(VidasViewModel.class);

        iv_vidas = findViewById(R.id.imageView_vidas);
        iv_Auno = findViewById(R.id.imageView_NumUno);
        iv_Ados = findViewById(R.id.imageView_NumDos);
        et_respuesta = findViewById(R.id.editText_resultado);
    }

    private void NombreyScore() {
        tv_nombre = findViewById(R.id.textView_nombre);
        tv_score = findViewById(R.id.textView_score);
        //recuperando elnombre del jugador y guardandolo en la variable designada
        nombre_jugador = getIntent().getStringExtra("Jugador");
        //Indicandole donde va a mostar el nombre del jugador
        tv_nombre.setText("Jugador: " + nombre_jugador);
        //Iniciando score con cero
        tv_score.setText("Score: " + scoreViewModel.getResultado());
    }

    private void IconActionBar() {
        //agregandole icono al action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void MusicaDeFondo() {
        //Poniendo el fondo musical del activity
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true); //Para ciclar el audio
    }

    private void floatingactionbutton() {

        //Audio cuando la respuesta fue correcta
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        //Audio cuando la respuesta fue incorrecta
        mp_bad = MediaPlayer.create(this, R.raw.bad);

       //final FloatingActionButton MenuBotnes = findViewById(R.id.grupoFab);

        final FloatingActionButton fab1 =findViewById(R.id.fab_music);
        final FloatingActionButton fab2 =findViewById(R.id.fab_sound);
        final FloatingActionButton fab3 =findViewById(R.id.fab_power);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mp.isPlaying()) {
                    mp.pause();
                    //btn_music.setBackgroundResource(R.drawable.sinmusic);
                    Toast.makeText(getApplicationContext(), R.string.toast_btn_music_off, Toast.LENGTH_SHORT).show();
                } else {
                    mp.start();
                    mp.setLooping(true);
                    // btn_music.setBackgroundResource(R.drawable.music);
                     Toast.makeText(getApplicationContext(), R.string.toast_btn_music_on, Toast.LENGTH_SHORT).show();
                }

            }

        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i++;
                //Single click
                if (i == 1) {
                    Toast.makeText(getApplicationContext(), R.string.toast_btn_sound_off, Toast.LENGTH_SHORT).show();
                    //Doouble click
                } else if (i == 2) {
                    Toast.makeText(getApplicationContext(), R.string.toast_btn_sound_on, Toast.LENGTH_SHORT).show();
                } else  {
                    i = 1;
                    Toast.makeText(getApplicationContext(), R.string.toast_btn_sound_off, Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent); //Abre la otra activity
                finish(); //Finaliza esta activity
                mp.stop(); //Finaliza el audio
                mp.release(); //Libera recursos
            }
        });

    }

    public void comparar (View view) {
        //Recuperando la respuesta del usuario
        String respuesta = et_respuesta.getText().toString();

        //Validando que haya una respuesta (Si la respuesta es diferente a un espacio en blanco, fue que se introdujo info
        if(!respuesta.equals("")){

           //Guardando respuesta en una variable tipo int
            int respuesta_jugador = Integer.parseInt(respuesta);

            if (resultado == respuesta_jugador & i == 1){ //Single click , no sound

                //aumentando el score por respuesta correcta
                scoreViewModel.setResultado(AumentarScore.aumentar(scoreViewModel.getResultado()));

                //Mostrando el valor del score actualizado en la pantalla Nivel 1
                tv_score.setText("Score: " + scoreViewModel.getResultado());

                //Limpiando el  editText_resultado
                et_respuesta.setText("");

                BaseDeDatos();

            } else if (resultado == respuesta_jugador & (i == 0 || i == 2) ){  //No click or double click, sound

                //ejecutando audio de respuesta correcta
                mp_great.start();

                //aumentando el score por respuesta correcta
                scoreViewModel.setResultado(AumentarScore.aumentar(scoreViewModel.getResultado()));

                //Mostrando el valor del score actualizado en la pantalla Nivel 1
                tv_score.setText("Score: " + scoreViewModel.getResultado());

                //Limpiando el  editText_resultado
                et_respuesta.setText("");

                BaseDeDatos();

            } else if (resultado != respuesta_jugador & i == 1) { //Single click , no sound

                //Las vidas se tiene que decrementar
               // vidasViewModel.setResultado(DisminuirVidas.disminuir(vidasViewModel.getResultado()));
               // vidas--;
                // scoreViewModel.setResultado(AumentarScore.aumentar(scoreViewModel.getResultado()));
                vidasViewModel.setResultado( DisminuirVidas.disminuir(vidasViewModel.getResultado()));

                et_respuesta.setText(""); //Limpiando el  editText_resultado

                BaseDeDatos();

                //Programar que va a pasar dependiendo de la cantidad de vidas que tenga el jugador
                switch (vidasViewModel.getResultado()){
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
            }

            else if (resultado != respuesta_jugador & (i == 0 || i == 2)) { //No click or double click, sound

                //Ejecutando el audio de respuesta incorrecta
                mp_bad.start();

                //Las vidas se tiene que decrementar
                //vidasViewModel.setResultado(DisminuirVidas.disminuir(vidasViewModel.getResultado()));
                vidasViewModel.setResultado( DisminuirVidas.disminuir(vidasViewModel.getResultado()));

                et_respuesta.setText(""); //Limpiando el  editText_resultado

                BaseDeDatos();

               //Programar que va a pasar dependiendo de la cantidad de vidas que tenga el jugador
                switch (vidasViewModel.getResultado()){
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
            }
            //Actualizando los numeros aleatorios
            NumAleatorio();

        } else {
            Toast.makeText(this, R.string.toast_null_respuesta, Toast.LENGTH_SHORT).show();
        }
    }

    public  void NumAleatorio (){

        //Con esta estructura condicional se pasara de niveles
        if ( scoreViewModel.getResultado() <= 9 ){

            //La clase Math solo retorna resultados de tipo double por lo que hay q realizar un casting y convertirlo en int
            numAleatorio_uno = (int) (Math.random() * 10);
            numAleatorio_dos = (int) (Math.random() * 10);

            resultado = numAleatorio_uno + numAleatorio_dos ;

           //El resultado no puede ser mayor de 10
            if (resultado <= 10) {

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

            // Creando un metodo recursivo para que si "resultado" no es menor o igual a 10 se busquen otros numeros aleatorios
            } else {
                NumAleatorio(); //Recursividad
            }

         //Cuando el score es mayor de 10 se debe de cambiar de nivel y pasar a la siguinete activity
        } else {

            Intent intent = new Intent (this, Nivel2.class);

            //Pasando el dato del score y de vidas y del nombre del jugador al activity Nivel2

            //string_score es una variable de tipo string y score es de tipo entero, por lo que se usa String.valueOf
            string_score = String.valueOf(scoreViewModel.getResultado());
            //Lo mismo pasa con la variable vidas
            string_vidas = String.valueOf(vidasViewModel.getResultado());

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

        String insert = "INSERT INTO " + utilidades.TABLA_PUNTAJE + "(" + utilidades.CAMPO_NOMBRE + " , " + utilidades.CAMPO_SCORE + ")" + " VALUES ( ' " + nombre_jugador + " ' ,  " + scoreViewModel.getResultado() + " )";
        BD.execSQL(insert);

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
            if (scoreViewModel.getResultado() > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put(utilidades.CAMPO_NOMBRE, nombre_jugador);
                modificacion.put(utilidades.CAMPO_SCORE, scoreViewModel.getResultado());

                //Modificando los valores dentro de la base de datos
                BD.update("puntaje", modificacion, "score" + bestScore, null);
                BD.close();

            } else {

                ContentValues insertar = new ContentValues();

                insertar.put ("nombre", nombre_jugador);
                insertar.put("score", scoreViewModel.getResultado());

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
}