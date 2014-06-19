package com.arkanoid.pintado;

import com.arkanoid.models.Barra;
import com.arkanoid.models.Bloque;
import com.arkanoid.models.BloqueEspecial;
import com.arkanoid.models.Bola;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	
	private HiloPantalla hiloPantalla;
	private int puntos;
	private Bola bola, bola2;
	private Paint pBlanco, pVerde, pRojo, pAzul, pAmarillo, pGris;
	private Barra barra;
	private Bloque[][] arrayBloques;
	private boolean moverBarraDer, moverBarraIzq; 
	private BloqueEspecial bloqueAlargarBarra, bloqueAcortarBarra, bloqueBolaPequenna, bloqueDobleBola;

	public GameView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		moverBarraDer=false;
		moverBarraIzq=false;
		pRojo=new Paint();
		pRojo.setColor(Color.RED);
		pAmarillo=new Paint();
		pAmarillo.setColor(Color.YELLOW);
		pAzul=new Paint();
		pAzul.setColor(Color.BLUE);
		pGris=new Paint();
		pGris.setColor(Color.GRAY);
		bloqueAlargarBarra=new BloqueEspecial(0, 0, getWidth(), pRojo);
		bloqueAcortarBarra=new BloqueEspecial(0, 0, getWidth(), pAmarillo);
		bloqueBolaPequenna=new BloqueEspecial(0, 0, getWidth(), pAzul);
		bloqueDobleBola=new BloqueEspecial(0, 0, getWidth(), pGris);
		bola=new Bola(getWidth(), getHeight());
		bola.colocarInicio();
		bola2=new Bola(getWidth(), getHeight());
		bola2.setX(-100);
		bola2.setY(-100);
		pBlanco=new Paint();
		pBlanco.setColor(Color.WHITE);
		pBlanco.setTextSize(30);
		
		barra=new Barra(getWidth(), getHeight());
		barra.colocarInicio();
		pVerde=new Paint();
		pVerde.setColor(Color.GREEN);
		
		arrayBloques=new Bloque[8][8];
		hiloPantalla=new HiloPantalla(this, bola, bola2, barra, arrayBloques, bloqueAlargarBarra, bloqueAcortarBarra, bloqueBolaPequenna, bloqueDobleBola);
		hiloPantalla.setRunning(true);
		hiloPantalla.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry=true;
		while(retry){
			try{
				hiloPantalla.setRunning(false);
				hiloPantalla.join();
				retry=false;
			}catch(InterruptedException e){
			}
		}
	}
	
	public void onDraw(Canvas canvas){
		if(hiloPantalla.isRunning()){
			if(moverBarraDer){
				moverDer();
			}else if(moverBarraIzq){
				moverIzq();
			}
			canvas.drawColor(Color.BLACK);
			canvas.drawText(puntos+"", 20, 35, pBlanco);
			if(bola.getX()!=0 && bola.getY()!=0){
				canvas.drawCircle(bola.getX(), bola.getY(), bola.getRadioBola(), pBlanco);
			}
			if(bola2.getX()!=0 && bola2.getY()!=0){
				canvas.drawCircle(bola2.getX(), bola2.getY(), bola2.getRadioBola(), pBlanco);
			}
			canvas.drawRect(barra.getX(), barra.getY(), barra.getX()+barra.getLongiBarra(), barra.getY()+barra.getAltoBarra(), pVerde);
			for(int f=0; f<8; f++){
				for(int c=0; c<8; c++){
					if(arrayBloques[f][c]!=null){
						canvas.drawRect(arrayBloques[f][c].getX(), arrayBloques[f][c].getY(), arrayBloques[f][c].getX()+arrayBloques[f][c].getAnchoBloque(), arrayBloques[f][c].getY()+arrayBloques[f][c].getAltoBloque(), arrayBloques[f][c].getPincel());
					}
				}
			}
			if(bloqueAlargarBarra.getX()!=0){
				canvas.drawRect(bloqueAlargarBarra.getX(), bloqueAlargarBarra.getY(), bloqueAlargarBarra.getX()+bloqueAlargarBarra.getLadoBloque(), 
						bloqueAlargarBarra.getY()+bloqueAlargarBarra.getLadoBloque(), bloqueAlargarBarra.getColor());
			}
			if(bloqueAcortarBarra.getX()!=0){
				canvas.drawRect(bloqueAcortarBarra.getX(), bloqueAcortarBarra.getY(), bloqueAcortarBarra.getX()+bloqueAcortarBarra.getLadoBloque(), 
						bloqueAcortarBarra.getY()+bloqueAcortarBarra.getLadoBloque(), bloqueAcortarBarra.getColor());
			}
			if(bloqueBolaPequenna.getX()!=0){
				canvas.drawRect(bloqueBolaPequenna.getX(), bloqueBolaPequenna.getY(), bloqueBolaPequenna.getX()+bloqueBolaPequenna.getLadoBloque(), 
						bloqueBolaPequenna.getY()+bloqueBolaPequenna.getLadoBloque(), bloqueBolaPequenna.getColor());
			}
			if(bloqueDobleBola.getX()!=0){
				canvas.drawRect(bloqueDobleBola.getX(), bloqueDobleBola.getY(), bloqueDobleBola.getX()+bloqueDobleBola.getLadoBloque(), 
						bloqueDobleBola.getY()+bloqueDobleBola.getLadoBloque(), bloqueDobleBola.getColor());
			}
		}
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if (event.getAction()==MotionEvent.ACTION_DOWN){
			if(event.getX()<getWidth()/2){
				moverBarraDer=false;
				moverBarraIzq=true;
			}else{
				moverBarraDer=true;
				moverBarraIzq=false;
			}
		}else if(event.getAction()==MotionEvent.ACTION_UP){
			moverBarraDer=false;
			moverBarraIzq=false;
		}
		return true;
	}
	
	private void moverIzq() {
		if(barra.getX()>=0){
			if(barra.getX()-(getWidth()*2/100)<0){
				barra.setX(0);
			}else{
				barra.setX(barra.getX()-barra.getVelocidad());
			}
		}
	}

	private void moverDer() {
		if(barra.getX()+barra.getLongiBarra()<=getWidth()){
			if(barra.getX()+barra.getLongiBarra()+(getWidth()*2/100)>getWidth()){
				barra.setX(getWidth()-barra.getLongiBarra());
			}else{
				barra.setX(barra.getX()+barra.getVelocidad());
			}
		}
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getPuntos(){
		return puntos;
	}
}
