package com.arkanoid.models;

public class Bola {

	private int x, y, anchoPan, altoPan, radioBola, direccion, velocidad, puntoInter;//diferencia entre la esqina y el circulo
	
	public Bola(int an, int al){
		anchoPan=an;
		altoPan=al;
		radioBola=(int) (anchoPan*2/100);
		puntoInter=radioBola*5/6;
		velocidad=radioBola/2;
	}
	
	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getPuntoInter() {
		return puntoInter;
	}

	public void setRadioBola(int radioBola) {
		this.radioBola = radioBola;
		puntoInter=radioBola*5/6;
	}

	public void colocarInicio(){
		x=radioBola;
		y=altoPan*60/100;
		direccion=1;
		setRadioBola(anchoPan*2/100);
	}

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public int getRadioBola() {
		return radioBola;
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
}
