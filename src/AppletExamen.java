/**
 * AppletJuego
 *
 * Personaje para juego previo Examen
 *
 * @author Ricardo Perez A01035082
 * @version 1.00 2008/6/13
 */
 


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppletExamen extends JFrame implements Runnable, KeyListener {
    
    public AppletExamen() {
        init();
        start();
    }
    

    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private int intVidas;  //Vidas del juego
    private int intDireccion;       //La direccion que tiene la changa
    private int intScore = 0;         //El puntaje del juego
    private int intCont = 0;          //contar el numero de colisiones que choca corredor con changa
    private boolean bolPause = false;
    private Personaje perNena;      //Se crea la chanquita
    private Personaje perAlien1;        //Se crea alien que camina
    private Personaje perAlien2;        //Se crea alien que corre
    private LinkedList lnkAlien1;       //donde se guardaran los caminadores
    private LinkedList lnkAlien2;       //donde se guardaran lso corredores
    private SoundClip aucAlien1;        //sonido del alien caminante
    private SoundClip aucAlien2;        //soido del alien corriente
    
    public void leeArchivo() throws IOException{
        // defino el objeto de Entrada para tomar datos
    	BufferedReader brwEntrada;
    	try{
                // creo el objeto de entrada a partir de un archivo de texto
    		brwEntrada = new BufferedReader(new FileReader("datos.txt"));
    	} catch (FileNotFoundException e){
                // si marca error es que el archivo no existia entonces lo creo
    		File filPuntos = new File("datos.txt");
    		PrintWriter prwSalida = new PrintWriter(filPuntos);
                // le pongo datos ficticios o de default
                prwSalida.println("200");
                prwSalida.println("3");
                prwSalida.println("0");
                prwSalida.println("0");
                prwSalida.println("0");
                prwSalida.println(getWidth() / 2 - perNena.getAncho() / 2);
                prwSalida.println(getHeight()/2-(perNena.getAlto()/2));
                prwSalida.println("5");
                for (int i = 0; i < 5; i ++) {
                    prwSalida.println((-1)*(int) (perAlien1.getAncho() + Math.random() * (getWidth())));
                    prwSalida.println((int) (Math.random() * (getHeight() - perAlien1.getAlto())));
                    prwSalida.println((int) (3+(Math.random()*3)));
                }
                prwSalida.println("5");
                for (int i = 0; i < 5; i ++) {
                    prwSalida.println((int) (Math.random() * (getWidth() - perAlien2.getAncho())));
                    prwSalida.println((-1)*(int) (perAlien2.getAlto() + Math.random() * (getHeight())));
                }
                // lo cierro para que se grabe lo que meti al archivo
    		prwSalida.close();
                // lo vuelvo a abrir porque el objetivo es leer datos
    		brwEntrada = new BufferedReader(new FileReader("datos.txt"));
    	}
        // con el archivo abierto leo los datos que estan guardados
        // primero saco el score que esta en la linea 1
    	intScore = Integer.parseInt(brwEntrada.readLine());
        // ahora leo las vidas que esta en la linea 2
    	intVidas = Integer.parseInt(brwEntrada.readLine());
        intDireccion = Integer.parseInt(brwEntrada.readLine());
        int intPausa = Integer.parseInt(brwEntrada.readLine());
        if(intPausa == 0) {
            bolPause = false;
        }
        else {
            bolPause = true;
        }
        intCont = Integer.parseInt(brwEntrada.readLine());
        perNena.setX(Integer.parseInt(brwEntrada.readLine()));
        perNena.setY(Integer.parseInt(brwEntrada.readLine()));
        lnkAlien1.clear();
        lnkAlien2.clear();
        int lisAlien1 = Integer.parseInt(brwEntrada.readLine());
        URL urlImagenAlien1 = this.getClass().getResource("alien1Camina.gif");
        for (int iI=0; iI<lisAlien1; iI++){
            perAlien1 = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenAlien1));
            perAlien1.setX(Integer.parseInt(brwEntrada.readLine()));
            perAlien1.setY(Integer.parseInt(brwEntrada.readLine()));
            perAlien1.setVelocidad(Integer.parseInt(brwEntrada.readLine()));
            lnkAlien1.add(perAlien1);
        }
        int lisAlien2 = Integer.parseInt(brwEntrada.readLine());
        URL urlImagenAlien2 = this.getClass().getResource("alien2Corre.gif");
        for (int iI=0; iI<lisAlien2; iI++){
            perAlien2 = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenAlien2));
            perAlien2.setX(Integer.parseInt(brwEntrada.readLine()));
            perAlien2.setY(Integer.parseInt(brwEntrada.readLine()));
            lnkAlien2.add(perAlien2);
        }
    	brwEntrada.close();
    }
    
    public void grabaArchivo() throws IOException{
        // creo el objeto de salida para grabar en un archivo de texto
    	PrintWriter prwSalida = new PrintWriter(new FileWriter("datos.txt"));
        // guardo en  linea 1 el score
    	prwSalida.println(intScore);
        // guardo en  linea 2 las vidas
        prwSalida.println(intVidas);
        prwSalida.println(intDireccion);
        if (bolPause) {
            prwSalida.println("1");
        }
        else {
            prwSalida.println("0");
        }
        prwSalida.println(intCont);
        prwSalida.println(perNena.getX());
        prwSalida.println(perNena.getY());
        prwSalida.println(lnkAlien1.size());
        for(Object lnkAlien1s:lnkAlien1) {
            perAlien1=(Personaje) lnkAlien1s;
            prwSalida.println(perAlien1.getX());
            prwSalida.println(perAlien1.getY()); 
            prwSalida.println(perAlien1.getVelocidad());
        }
        prwSalida.println(lnkAlien2.size());
        
        for(Object lnkAlien2s:lnkAlien2) {
            perAlien2=(Personaje) lnkAlien2s;
            prwSalida.println(perAlien2.getX());
            prwSalida.println(perAlien2.getY());
                
        }
        
        // cierro el archivo
    	prwSalida.close();	
    }
    
    
    
    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {

    }

    @Override
    //Se define la direccion al oprimir las teclas
    public void keyReleased(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_C) {
            try {
                leeArchivo();
            } catch (IOException ex) {
                Logger.getLogger(AppletExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(ke.getKeyCode() == KeyEvent.VK_G) {
            try {
                grabaArchivo();
            } catch (IOException ex) {
                Logger.getLogger(AppletExamen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(ke.getKeyCode() == KeyEvent.VK_P) {
            bolPause = !bolPause;
        }
        if(!bolPause) {
            if(ke.getKeyCode() == KeyEvent.VK_W) {
                intDireccion=1;
            }
            else if(ke.getKeyCode() == KeyEvent.VK_S) {
                intDireccion=2;
            }
            else if(ke.getKeyCode() == KeyEvent.VK_A) {
                intDireccion=3;
            }
            else if(ke.getKeyCode() == KeyEvent.VK_D) {
                intDireccion=4;
            }
        }
    }

	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el applet de un tamaño 800,600
        setSize(800, 600);
        addKeyListener(this);
        //las vidas son random
        intVidas = ((int)(3 + Math.random() * 3));
        
        //agrego los sonidos de los lines al chocar con nena
        
        aucAlien1 = new SoundClip("shing.wav");
        
        aucAlien2 = new SoundClip("shot2.wav");
        
        //creo a nena
        URL urlImagenNena = this.getClass().getResource("nena.gif");
        perNena = new Personaje(getWidth() / 4, getHeight()  / 4,
                Toolkit.getDefaultToolkit().getImage(urlImagenNena));
        perNena.setX(getWidth()/2-(perNena.getAncho()/2));
        perNena.setY(getHeight()/2-(perNena.getAlto()/2));
        
        //creo las listas donde se guardaran los dos tipso de alines
        lnkAlien1=new LinkedList();
        lnkAlien2=new LinkedList();
        
        //importo la imagen de ambos aliens
        URL urlImagenAlien1 = this.getClass().getResource("alien1Camina.gif");
        URL urlImagenAlien2 = this.getClass().getResource("alien2Corre.gif");
        
        //creo al alien 1
        int intA1=(int) (8+(Math.random()*3));
        for (int iI=0; iI<intA1; iI++){
            perAlien1 = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenAlien1));
            perAlien1.setX((-1)*(int) (perAlien1.getAncho() + Math.random() * (getWidth())));
            perAlien1.setY((int) (Math.random() * (getHeight() - perAlien1.getAlto())));
            perAlien1.setVelocidad((int) (3+(Math.random()*3)));
            lnkAlien1.add(perAlien1);
        }
        
        //creo al alien 2
        int intA2=(int) (10+(Math.random()*6));
        for (int iI=0; iI<intA2; iI++){
            perAlien2 = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenAlien2));
            perAlien2.setX((int) (Math.random() * (getWidth() - perAlien2.getAncho())));
            perAlien2.setY((-1)*(int) (perAlien2.getAlto() + Math.random() * (getHeight())));
            lnkAlien2.add(perAlien2);
        }
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run() {
        // se realiza el ciclo del juego en este caso nunca termina

        while (true) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
               se checa si hubo colisiones para desaparecer jugadores o corregir
               movimientos y se vuelve a pintar todo
            */ 
            if (!bolPause) {
            actualiza();
            checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError)	{
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion del objeto elefante 
     * 
     */
    public void actualiza(){
        // instrucciones para actualizar personajes
        if(intVidas>0) {
            switch(intDireccion){
                case 1: {
                perNena.setY(perNena.getY()-3);
                break;
                }
                case 2: {
                perNena.setY(perNena.getY()+3);
                break;
                }
                case 3: {
                perNena.setX(perNena.getX()-3);
                break;
                }
                case 4: {
                perNena.setX(perNena.getX()+3);
                break;
                }
            }
            for(Object lnkAlien1s:lnkAlien1) {
                perAlien1=(Personaje) lnkAlien1s;
                perAlien1.setX(perAlien1.getX()+perAlien1.getVelocidad());
            }
        
            for(Object lnkAlien2s:lnkAlien2) {
                perAlien2=(Personaje) lnkAlien2s;
                perAlien2.setY(perAlien2.getY()+ (6-intVidas));
            }
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision del objeto elefante
     * con las orillas del <code>Applet</code>.
     * 
     */
    public void checaColision(){
        // instrucciones para checar colision y reacomodar personajes si 
        // es necesario
        if(perNena.getX()<0) {
            intDireccion=0;
            perNena.setX(1);
        }
        if(perNena.getX()+perNena.getAncho()>getWidth()) {
            intDireccion=0;
            perNena.setX(getWidth()-perNena.getAncho()-1);
        }
        if(perNena.getY()<0) {
            intDireccion=0;
            perNena.setY(1);
        }
        if(perNena.getY()+perNena.getAlto()>getHeight()) {
            intDireccion=0;
            perNena.setY(getHeight()-perNena.getAlto()-1);
        }
        //lo que ocurre con alien 1 si choca con nena o la pared
        for(Object lnkAlien1s:lnkAlien1) {
            perAlien1=(Personaje)lnkAlien1s;
            if(perNena.colisiona(perAlien1)) {
                intScore=intScore+1;
                perAlien1.setX((-1)*(int) (perAlien1.getAncho() + Math.random() * (getWidth() + 500)));
                perAlien1.setY((int) (Math.random() * (getHeight() - perAlien1.getAlto())));
                aucAlien1.play();
            }
            if(perAlien1.getX()>getWidth()-perAlien1.getAncho()) {
                perAlien1.setX((-1)*(int) (perAlien1.getAncho() + (Math.random() * (getWidth()))));
                perAlien1.setY((int) (Math.random() * (getHeight() - perAlien1.getAlto())));
            }
        }
        //lo que ocurre con alien 2 si choca con nena o la pared
        for(Object lnkAlien2s:lnkAlien2) {
            perAlien2=(Personaje)lnkAlien2s;
            if(perNena.colisiona(perAlien2)) {
                intCont=intCont+1;
                perAlien2.setX((int) (Math.random() * (getWidth() - perAlien2.getAncho())));
                perAlien2.setY((-1)*(int) (perAlien2.getAlto() + (Math.random() * (getHeight()))));
                aucAlien2.play();
                if(intCont==5) {
                    intVidas=intVidas-1;
                    intCont=0;
                }
            }
            if(perAlien2.getY()>getHeight()-perAlien2.getAlto()) {
                perAlien2.setX((int) (Math.random() * (getWidth() - perAlien2.getAncho())));
                perAlien2.setY((-1)*(int) (perAlien2.getAlto() + Math.random() * (getHeight())));
            }
        }
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint1
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }
        //mientras haya vidas se dibuja el espacio
        if(intVidas>0) {
            // Se pone la imgen de espacio como background
            URL urlImagenEspacio = this.getClass().getResource("espacio.jpg");
            Image imaImagenEspacio = Toolkit.getDefaultToolkit().getImage(urlImagenEspacio);
        
            graGraficaApplet.drawImage(imaImagenEspacio, 0, 0, 
                getWidth(), getHeight(), this);  
            
            if(bolPause) {
                URL urlImagenPausa = this.getClass().getResource("pause.jpg");
            Image imaImagenPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
            
            graGraficaApplet.drawImage(imaImagenPausa, getWidth()/2-perNena.getAncho(), getHeight()/2-perNena.getAlto(), 
                perNena.getAncho(), perNena.getAlto(), this); 
            }
        }
        //si no hay vidas se dibuja el gameover
        else {
            URL urlImagenOver = this.getClass().getResource("gameoverex.jpg");
            Image imaImagenOver = Toolkit.getDefaultToolkit().getImage(urlImagenOver);
        
            graGraficaApplet.drawImage(imaImagenOver, 0, 0, 
                getWidth(), getHeight(), this); 
        }

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint1
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint1(Graphics g) {
        //mientras haya vidas se pinta todo
        if(intVidas>0) {
        g.setFont(new Font("TimedRoman",Font.BOLD,16));
        g.setColor(Color.RED);
        g.drawString("Vidas:",60, 60);
        g.drawString(Integer.toString(intVidas), 160, 60);
        g.drawString("Puntaje:", getWidth()-180, 60);
        g.drawString(Integer.toString(intScore), getWidth()-60, 60);
        g.drawImage(perNena.getImagen(), perNena.getX(),
              perNena.getY(), this);
        for(Object lnkAlien1s:lnkAlien1) {
            perAlien1=(Personaje)lnkAlien1s;
            g.drawImage(perAlien1.getImagen(), perAlien1.getX(),
            perAlien1.getY(), this);
        }
        for(Object lnkAlien2s:lnkAlien2) {
            perAlien2=(Personaje)lnkAlien2s;
            g.drawImage(perAlien2.getImagen(), perAlien2.getX(),
            perAlien2.getY(), this);
        }
        }

    }
    
}