package com.arkanoid.models;

import android.graphics.Rect;

public class Barra {
	
	private int x, y, anchoPan, altoPan, longiBarra, altoBarra, velocidad, aumentoVel;

	public Barra(int an, int al){
		anchoPan=an;
		altoPan=al;
		longiBarra=anchoPan*20/100;
		altoBarra=altoPan*3/100;
		velocidad=anchoPan*2/100;
		aumentoVel=velocidad/3/4;
	}

	public int getAumentoVel() {
		return aumentoVel;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public int getLongiBarra() {
		return longiBarra;
	}

	public void setLongiBarra(int longiBarra) {
		this.longiBarra = longiBarra;
	}

	public int getAltoBarra() {
		return altoBarra;
	}

	public void colocarInicio(){
		x=(anchoPan/2)-(longiBarra/2);
		y=altoPan*90/100;
		setLongiBarra(anchoPan*20/100);
	}
	
	public Rect getRect(){
		return new Rect(x, y, x+longiBarra, y+altoBarra);
	}
}
