package com.arkanoid.pintado;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.arkanoid.MainActivity;
import com.arkanoid.models.Barra;
import com.arkanoid.models.Bloque;
import com.arkanoid.models.BloqueEspecial;
import com.arkanoid.models.Bola;

@SuppressLint("WrongCall")
public class HiloPantalla extends Thread {
	
	private GameView view;
	private boolean running=false;
	private boolean finJuego, inicio;
	private int FPS;
	private Bola bola, bola2;
	private Barra barra;
	private Paint pBlanco, pVerde, pAzul, pRojo, pAmarillo, pGris, pCyan, pMagenta, pGrisClaro, pGrisOscuro;
	private Bloque[][] arrayBloques;
	private boolean alargarBarra, acortarBarra, bolaPequenna, dobleBola;
	private BloqueEspecial bloqueAlargarBarra, bloqueAcortarBarra, bloqueBolaPequenna, bloqueDobleBola;
	
	public HiloPantalla(GameView view, Bola b, Bola b2, Barra ba, Bloque[][] array, BloqueEspecial bloqueAlargarBarra, 
			BloqueEspecial bloqueAcortarBarra, BloqueEspecial bloqueBolaPequenna, BloqueEspecial bloqueDobleBola){
		this.view=view;
		finJuego=true;
		inicio=true;
		FPS=40;
		bola=b;
		bola2=b2;
		barra=ba;
		arrayBloques=array;
		pBlanco=new Paint();
		pBlanco.setColor(Color.WHITE);
		pVerde=new Paint();
		pVerde.setColor(Color.GREEN);
		pRojo=new Paint();
		pRojo.setColor(Color.RED);
		pAmarillo=new Paint();
		pAmarillo.setColor(Color.YELLOW);
		pAzul=new Paint();
		pAzul.setColor(Color.BLUE);
		pGris=new Paint();
		pGris.setColor(Color.GRAY);
		pCyan=new Paint();
		pCyan.setColor(Color.CYAN);
		pMagenta=new Paint();
		pMagenta.setColor(Color.MAGENTA);
		pGrisClaro=new Paint();
		pGrisClaro.setColor(Color.LTGRAY);
		pGrisOscuro=new Paint();
		pGrisOscuro.setColor(Color.DKGRAY);
		alargarBarra=false;
		acortarBarra=false;
		bolaPequenna=false;
		dobleBola=false;
		this.bloqueAlargarBarra=bloqueAlargarBarra;
		this.bloqueAcortarBarra=bloqueAcortarBarra;
		this.bloqueBolaPequenna=bloqueBolaPequenna;
		this.bloqueDobleBola=bloqueDobleBola;
	}


	public void setRunning(boolean run){
		running=run;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void run(){
		long startTime;
		long sleepTime;
		while(running){
			long ticksPS=1000/FPS;
			Canvas c=null;
			startTime=System.currentTimeMillis();
			try{
				if(!inicio){
					if(nivelCompletado()){
						reInitJuego();
						if(FPS<60){
							FPS=FPS+5;
							barra.setVelocidad(barra.getVelocidad()-barra.getAumentoVel());
						}
						view.setPuntos(view.getPuntos()+1);
						comprobarPuntuacion();
						try{
							sleep(2000);
						}catch(InterruptedException e){}
					}
				}
				inicio=false;
				comprobarDireccion(bola);
				moverBola(bola);
				comprobarDireccion(bola2);
				moverBola(bola2);
				moverEspecial();
				c=view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.onDraw(c);
				}
			}finally{
				if(c!=null){
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			sleepTime=ticksPS-(System.currentTimeMillis()-startTime);
			try{
				if(sleepTime>0){
					sleep(sleepTime);
				}
			}catch(Exception e){}
			if(finJuego){
				try {
					sleep(2000);
					comprobarPuntuacion();
					reInitJuego();
					FPS=40;
					((GameView) view).setPuntos(0);
					finJuego=false;
					inicio=true;
				} catch (InterruptedException e) {}
			}
		}
	}
	
	private void moverEspecial() {
		if(bloqueAlargarBarra.getY()!=0){
			bloqueAlargarBarra.setY(bloqueAlargarBarra.getY()+(bloqueAlargarBarra.getLadoBloque()/2));
		}else if(bloqueAlargarBarra.getY()>=view.getHeight()){
			bloqueAlargarBarra.setX(0);
			bloqueAlargarBarra.setY(0);
		}
		if(barra.getRect().contains(bloqueAlargarBarra.getX(), bloqueAlargarBarra.getY()+bloqueAlargarBarra.getLadoBloque()) ||
				barra.getRect().contains(bloqueAlargarBarra.getX()+bloqueAlargarBarra.getLadoBloque(), bloqueAlargarBarra.getY()+bloqueAlargarBarra.getLadoBloque())){
			synchronized (barra) {
				barra.setLongiBarra(view.getWidth()*30/100);
			}
			bloqueAlargarBarra.setX(0);
			bloqueAlargarBarra.setY(0);
		}
		if(bloqueAcortarBarra.getY()!=0){
			bloqueAcortarBarra.setY(bloqueAcortarBarra.getY()+(bloqueAcortarBarra.getLadoBloque()/2));
		}else if(bloqueAcortarBarra.getY()>=view.getHeight()){
			bloqueAcortarBarra.setX(0);
			bloqueAcortarBarra.setY(0);
		}
		if(barra.getRect().contains(bloqueAcortarBarra.getX(), bloqueAcortarBarra.getY()+bloqueAcortarBarra.getLadoBloque()) ||
				barra.getRect().contains(bloqueAcortarBarra.getX()+bloqueAcortarBarra.getLadoBloque(), bloqueAcortarBarra.getY()+bloqueAcortarBarra.getLadoBloque())){
			synchronized (barra) {
				barra.setLongiBarra(view.getWidth()*10/100);
			}
			bloqueAcortarBarra.setX(0);
			bloqueAcortarBarra.setY(0);
		}
		if(bloqueBolaPequenna.getY()!=0){
			bloqueBolaPequenna.setY(bloqueBolaPequenna.getY()+(bloqueBolaPequenna.getLadoBloque()/2));
		}else if(bloqueBolaPequenna.getY()>=view.getHeight()){
			bloqueBolaPequenna.setX(0);
			bloqueBolaPequenna.setY(0);
		}
		if(barra.getRect().contains(bloqueBolaPequenna.getX(), bloqueBolaPequenna.getY()+bloqueBolaPequenna.getLadoBloque()) ||
				barra.getRect().contains(bloqueBolaPequenna.getX()+bloqueBolaPequenna.getLadoBloque(), bloqueBolaPequenna.getY()+bloqueBolaPequenna.getLadoBloque())){
			bola.setRadioBola(view.getWidth()*1/100);
			bola2.setRadioBola(view.getWidth()*1/100);
			bloqueBolaPequenna.setX(0);
			bloqueBolaPequenna.setY(0);
		}
		if(bloqueDobleBola.getY()!=0){
			bloqueDobleBola.setY(bloqueDobleBola.getY()+(bloqueDobleBola.getLadoBloque()/2));
		}else if(bloqueDobleBola.getY()>=view.getHeight()){
			bloqueDobleBola.setX(0);
			bloqueDobleBola.setY(0);
		}
		if(barra.getRect().contains(bloqueDobleBola.getX(), bloqueDobleBola.getY()+bloqueDobleBola.getLadoBloque()) ||
				barra.getRect().contains(bloqueDobleBola.getX()+bloqueDobleBola.getLadoBloque(), bloqueDobleBola.getY()+bloqueDobleBola.getLadoBloque())){
			bola2.setX(bola.getX());
			bola2.setY(bola.getY());
			bola2.setRadioBola(bola.getRadioBola());
			switch (bola.getDireccion()) {
			case 1:
				bola2.setDireccion(2);
				break;
			case 2:
				bola2.setDireccion(1);			
				break;
			case 3:
				bola2.setDireccion(4);
				break;
			case 4:
				bola2.setDireccion(3);
				break;
			}
			bloqueDobleBola.setX(0);
			bloqueDobleBola.setY(0);
		}
	}

	private void comprobarPuntuacion(){
		if(MainActivity.puntuacion<view.getPuntos()){
			MainActivity.guardarPuntuacion(view.getPuntos());
		}
	}
	
	private void comprobarDireccion(Bola b){
		if(b.getX()!=-100 && b.getY()!=-100){
			switch (b.getDireccion()) {
			case 1:
				if(b.getX()+b.getRadioBola()>=view.getWidth()){
					b.setDireccion(2);
					break;
				}
				if(barra.getRect().contains(b.getX(), b.getY()+b.getRadioBola()) || 
						barra.getRect().contains(b.getX()-b.getPuntoInter(), b.getY()+b.getPuntoInter())){
					b.setDireccion(4);
					break;
				}
				if(b.getY()+b.getRadioBola()>=view.getHeight()){
					b.setX(-100);
					b.setY(-100);
					if(bola.getX()==-100 && bola.getY()==-100 && bola2.getX()==-100 && bola2.getY()==-100){
						finJuego=true;
					}
					break;
				}
				for(int f=0; f<8; f++){
					for(int c=0; c<8; c++){
						if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX(), b.getY()+b.getRadioBola()) || 
								arrayBloques[f][c].getRect().contains(b.getX()-b.getPuntoInter(), b.getY()+b.getPuntoInter()))){
							b.setDireccion(4);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}else if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX()+b.getRadioBola(), b.getY()) || 
								arrayBloques[f][c].getRect().contains(b.getX()+b.getPuntoInter(), b.getY()-b.getPuntoInter()))){
							b.setDireccion(2);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}
					}
				}
				break;
			case 2:
				if(b.getX()-b.getRadioBola()<=0){
					b.setDireccion(1);
					break;
				}
				if(barra.getRect().contains(b.getX(), b.getY()+b.getRadioBola()) || 
						barra.getRect().contains(b.getX()+b.getPuntoInter(), b.getY()+b.getPuntoInter())){
					b.setDireccion(3);
					break;
				}
				if(b.getY()+b.getRadioBola()>=view.getHeight()){
					b.setX(-100);
					b.setY(-100);
					if(bola.getX()==-100 && bola.getY()==-100 && bola2.getX()==-100 && bola2.getY()==-100){
						finJuego=true;
					}
					break;
				}
				for(int f=0; f<8; f++){
					for(int c=0; c<8; c++){
						if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX(), b.getY()+b.getRadioBola()) || 
								arrayBloques[f][c].getRect().contains(b.getX()+b.getPuntoInter(), b.getY()+b.getPuntoInter()))){
							b.setDireccion(3);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}else if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX()-b.getRadioBola(), b.getY()) || 
								arrayBloques[f][c].getRect().contains(b.getX()-b.getPuntoInter(), b.getY()-b.getPuntoInter()))){
							b.setDireccion(1);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}
					}
				}
				break;
			case 3:
				if(b.getX()-b.getRadioBola()<=0){
					b.setDireccion(4);
					break;
				}
				if(b.getY()-b.getRadioBola()<=0){
					b.setDireccion(2);
					break;
				}
				for(int f=0; f<8; f++){
					for(int c=0; c<8; c++){
						if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX(), b.getY()-b.getRadioBola()) || 
								arrayBloques[f][c].getRect().contains(b.getX()+b.getPuntoInter(), b.getY()+b.getPuntoInter()))){
							b.setDireccion(2);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}else if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX()-b.getRadioBola(), b.getY()) || 
								arrayBloques[f][c].getRect().contains(b.getX()-b.getPuntoInter(), b.getY()+b.getPuntoInter()))){
							b.setDireccion(4);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}
					}
				}
				break;
			case 4:
				if(b.getX()+b.getRadioBola()>=view.getWidth()){
					b.setDireccion(3);
					break;
				}
				if(b.getY()-b.getRadioBola()<=0){
					b.setDireccion(1);
					break;
				}
				for(int f=0; f<8; f++){
					for(int c=0; c<8; c++){
						if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX(), b.getY()-b.getRadioBola()) || 
								arrayBloques[f][c].getRect().contains(b.getX()-b.getPuntoInter(), b.getY()-b.getPuntoInter()))){
							b.setDireccion(1);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}else if(arrayBloques[f][c]!=null && (arrayBloques[f][c].getRect().contains(b.getX()+b.getRadioBola(), b.getY()) || 
								arrayBloques[f][c].getRect().contains(b.getX()+b.getPuntoInter(), b.getY()+b.getPuntoInter()))){
							b.setDireccion(3);
							sacarEspecial(arrayBloques[f][c]);
							eliminarBloque(f, c);
							break;
						}
					}
				}
				break;
			}
		}
	}
	
	private void sacarEspecial(Bloque bloque) {
		switch (bloque.getEspecial()) {
		case 1:
			bloqueAlargarBarra.setX(bloque.getX()+(bloque.getAnchoBloque()/2));
			bloqueAlargarBarra.setY(bloque.getY());
			break;
		case 2:
			bloqueAcortarBarra.setX(bloque.getX()+(bloque.getAnchoBloque()/2));
			bloqueAcortarBarra.setY(bloque.getY());
			break;
		case 3:
			bloqueBolaPequenna.setX(bloque.getX()+(bloque.getAnchoBloque()/2));
			bloqueBolaPequenna.setY(bloque.getY());
			break;
		case 4:
			bloqueDobleBola.setX(bloque.getX()+(bloque.getAnchoBloque()/2));
			bloqueDobleBola.setY(bloque.getY());
			break;
		default:
			break;
		}
	}

	private void eliminarBloque(int f, int c) {
		switch (arrayBloques[f][c].getColor()) {
		case 0:
			arrayBloques[f][c]=null;
			break;
		case 1:
			arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pGrisClaro, 0, 0);
			break;
		case 2:
			arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pGris, 1, 0);
			break;
		}
	}

	private void moverBola(Bola b){
		if(b.getX()!=-100 && b.getY()!=-100){
			switch (b.getDireccion()) {
			case 1://abajo derecha
				b.setX(b.getX()+(b.getVelocidad()));
				b.setY(b.getY()+(b.getVelocidad()));
				break;
			case 2://abajo izquierda
				b.setX(b.getX()-(b.getVelocidad()));
				b.setY(b.getY()+(b.getVelocidad()));
				break;
			case 3://arriba izquierda
				b.setX(b.getX()-(b.getVelocidad()));
				b.setY(b.getY()-(b.getVelocidad()));
				break;
			case 4://arriba derecha
				b.setX(b.getX()+(b.getVelocidad()));
				b.setY(b.getY()-(b.getVelocidad()));
				break;
			}
		}
	}

	public void reInitJuego(){
		bola.colocarInicio();
		barra.colocarInicio();
		bola2.setX(-100);
		bola2.setY(-100);
		bola2.setVelocidad(bola.getVelocidad());
		borrarArray();
		llenarArray();
	}
	
	public void borrarArray(){
		alargarBarra=false;
		acortarBarra=false;
		bolaPequenna=false;
		dobleBola=false;
		for(int f=0; f<8; f++){
			for(int c=0; c<8; c++){
				arrayBloques[f][c]=null;
			}
		}
		bloqueAcortarBarra.setX(0);
		bloqueAcortarBarra.setY(0);
		bloqueAlargarBarra.setX(0);
		bloqueAlargarBarra.setY(0);
		bloqueBolaPequenna.setX(0);
		bloqueBolaPequenna.setY(0);
		bloqueDobleBola.setX(0);
		bloqueDobleBola.setY(0);
	}
	
	public void llenarArray(){
		int contBloques=0;
		while(contBloques<40){
			for(int f=0; f<8; f++){
				for(int c=0; c<8; c++){
					if(arrayBloques[f][c]==null){
						if((int)(Math.random()*10)<=4){
							switch ((int)(Math.random()*10)) {
							case 0:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAmarillo, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAmarillo, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAmarillo, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAmarillo, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAmarillo, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAmarillo, 0, 0);
								}
								break;
							case 1:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAzul, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAzul, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAzul, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAzul, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAzul, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pAzul, 0, 0);
								}					
								break;
							case 2:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pBlanco, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pBlanco, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pBlanco, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pBlanco, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pBlanco, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pBlanco, 0, 0);
								}
								break;
							case 3:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pCyan, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pCyan, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pCyan, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pCyan, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pCyan, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pCyan, 0, 0);
								}
								break;
							case 4:
								arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pGris, 1, 0);
								break;
							case 5:
								arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pGrisClaro, 0, 0);
								break;
							case 6:
								arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pGrisOscuro, 2, 0);
								break;
							case 7:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pMagenta, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pMagenta, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pMagenta, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pMagenta, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pMagenta, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pMagenta, 0, 0);
								}
								break;
							case 8:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pRojo, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pRojo, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pRojo, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pRojo, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pRojo, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pRojo, 0, 0);
								}
								break;
							case 9:
								if(!alargarBarra || !acortarBarra || !bolaPequenna || !dobleBola){
									switch ((int)(Math.random()*10)) {
									case 2:
										if(!alargarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pVerde, 0, 1);
											alargarBarra=true;
										}
										break;
									case 4:
										if(!acortarBarra){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pVerde, 0, 2);
											acortarBarra=true;
										}
										break;
									case 6:
										if(!bolaPequenna){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pVerde, 0, 3);
											bolaPequenna=true;
										}
										break;
									case 8:
										if(!dobleBola){
											arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pVerde, 0, 4);
											dobleBola=true;
										}
										break;
									default:
										arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pVerde, 0, 0);
										break;
									}
								}else{
									arrayBloques[f][c]=new Bloque(f, c, view.getWidth(), view.getHeight(), pVerde, 0, 0);
								}
								break;
							}
							contBloques++;
						}
					}
				}
			}
		}
	}
	
	private boolean nivelCompletado(){
		for(int f=0; f<8; f++){
			for(int c=0; c<8; c++){
				if(arrayBloques[f][c]!=null){
					return false;
				}
			}
		}
		return true;
	}
}
