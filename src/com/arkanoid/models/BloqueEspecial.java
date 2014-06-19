package com.arkanoid.models;

import android.graphics.Paint;

public class BloqueEspecial {

	private int x, y, ladoBloque;
	private Paint color;

	public BloqueEspecial(int x, int y, int anchoPan, Paint p) {
		this.x = x;
		this.y = y;
		ladoBloque=anchoPan*3/100;
		color=p;
	}

	public Paint getColor() {
		return color;
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

	public void setY(int y) {
		this.y = y;
	}

	public int getLadoBloque() {
		return ladoBloque;
	}
}
