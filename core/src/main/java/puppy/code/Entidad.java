package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


public abstract class Entidad {
	   private Rectangle areaEntidad;
	   private Texture textura;
	   private Texture texturaHurt;
	   private Sound sonidoHerido;
	   private int vida;
	   private int velocidad;
	   private boolean herido = false;
	   private int tiempoHeridoMax = 50;
	   private int tiempoHerido;
	   private Ataque ataqueActual;
	   private AtaqueStrategy ataqueStrategy;
	   
	   public AtaqueStrategy getAtaqueStrategy() {
		    return ataqueStrategy;
		}

		public void setAtaqueStrategy(AtaqueStrategy ataqueStrategy) {
		    this.ataqueStrategy = ataqueStrategy;
		}
	   
	   public Entidad(Texture image, Texture imageAlt, Sound ss, int vida, int velocidad, int coordenadasX, int coordenadasY, int ancho, int alto) {
		   this.textura = image;
		   this.texturaHurt = imageAlt;
		   this.sonidoHerido = ss;
		   this.vida = vida;
		   this.velocidad = velocidad;
		   crearHitbox(coordenadasX, coordenadasY, ancho, alto);
	   }

	   
	   public Ataque getAtaqueActual() {
		    return ataqueActual;
		}

		public void setAtaqueActual(Ataque ataqueActual) {
		    this.ataqueActual = ataqueActual;
		}

	   public Rectangle getAreaEntidad() {
		    return areaEntidad;
		}

		public void setAreaEntidad(Rectangle areaEntidad) {
		    this.areaEntidad = areaEntidad;
		}

		public Texture getTextura() {
		    return textura;
		}

		public void setTextura(Texture textura) {
		    this.textura = textura;
		}

		public Texture getTexturaHurt() {
		    return texturaHurt;
		}

		public void setTexturaHurt(Texture texturaHurt) {
		    this.texturaHurt = texturaHurt;
		}

		public Sound getSonidoHerido() {
		    return sonidoHerido;
		}

		public void setSonidoHerido(Sound sonidoHerido) {
		    this.sonidoHerido = sonidoHerido;
		}

		public int getVida() {
		    return vida;
		}

		public void setVida(int vida) {
		    this.vida = vida;
		}

		public int getVelocidad() {
		    return velocidad;
		}

		public void setVelocidad(int velocidad) {
		    this.velocidad = velocidad;
		}

		public boolean isHerido() {
		    return herido;
		}

		public void setHerido(boolean herido) {
		    this.herido = herido;
		}

		public int getTiempoHeridoMax() {
		    return tiempoHeridoMax;
		}

		public void setTiempoHeridoMax(int tiempoHeridoMax) {
		    this.tiempoHeridoMax = tiempoHeridoMax;
		}

		public int getTiempoHerido() {
		    return tiempoHerido;
		}

		public void setTiempoHerido(int tiempoHerido) {
		    this.tiempoHerido = tiempoHerido;
		}

		
	   public void crearHitbox(int coordenadasX, int coordenadasY, int ancho, int alto) {
		      areaEntidad = new Rectangle();
		      areaEntidad.x = coordenadasX;
		      areaEntidad.y = coordenadasY;
		      areaEntidad.width = ancho;
		      areaEntidad.height = alto;
	   }
	   
	   public void dañar() {
		  vida--;
		  herido = true;
		  tiempoHerido=tiempoHeridoMax;
		  sonidoHerido.play();
	   }
	   
	   public void dibujar(SpriteBatch batch) {
		    if (herido) {  
		        batch.draw(texturaHurt, areaEntidad.x, areaEntidad.y + MathUtils.random(-5, 5));
		        tiempoHerido--;
		        if (tiempoHerido <= 0) herido = false;
		    } else {
		        batch.draw(textura, areaEntidad.x, areaEntidad.y);
		    }

		    if (ataqueActual != null) {
		        ataqueActual.dibujar(batch);
		    }
		}
	    
	   public void actualizar() {
	}	
	   
	public void destruir() {
		    textura.dispose();
	   }
	
   public boolean estaHerido() {
	   return herido;
   }
   
   public void ataque() {
	    if (ataqueStrategy != null) {
	        setAtaqueActual(ataqueStrategy.crearAtaque(this));
	    }
	}

   protected abstract void chequearAtaque(float delta, Rectangle areaEntidad2);
	   
}
