package com.arkanoid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static int puntuacion;
	private static SharedPreferences preferencias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//deja la aplicacion a toda la pantalla y en vertical
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	
		setContentView(R.layout.activity_main);
		
		preferencias=getSharedPreferences("arkanoid", Context.MODE_PRIVATE);
		puntuacion=preferencias.getInt("puntuacion", 0);
	}
	
	public void jugar(View v){
		Intent i =new Intent(this, ArkanoidJuego.class);
		this.startActivity(i);
	}
	
	public void mostrarPuntuacion(View v){
		puntuacion=preferencias.getInt("puntuacion", 0);
		Toast.makeText(getApplicationContext(), puntuacion+"", Toast.LENGTH_SHORT).show();
	}
	
	public static void guardarPuntuacion(int p){
		Editor editor=preferencias.edit();
		editor.putInt("puntuacion", p);
		editor.commit();
	}
	
	public void salir(View v){
		System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
