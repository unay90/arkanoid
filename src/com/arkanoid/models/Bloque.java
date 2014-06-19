package com.arkanoid.models;

import android.graphics.Paint;
import android.graphics.Rect;

public class Bloque {

	private int x, y, anchoBloque, altoBloque, anchoPan, altoPan; 
	private Paint pincel;
	private int color, especial;
	
	public Bloque(int xx, int yy, int an, int al, Paint p, int c, int e){
		anchoPan=an;
		altoPan=al;
		anchoBloque=anchoPan*10/100;
		altoBloque=altoPan*5/100;
		pincel=p;
		x=((anchoBloque*xx)+anchoBloque);
		y=((altoBloque*yy)+(altoBloque*2));
		color=c;
		especial=e;
	}

	public int getEspecial() {
		return especial;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAnchoBloque() {
		return anchoBloque;
	}

	public int getAltoBloque() {
		return altoBloque;
	}

	public Paint getPincel() {
		return pincel;
	}
	
	public Rect getRect(){
		return new Rect(x, y, x+anchoBloque, y+altoBloque);
	}
}
