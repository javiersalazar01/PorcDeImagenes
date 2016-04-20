/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author karlagutz
 */
public class Filtros {
   // private short[][] matrizGris = null;
   // private short[][] matrizGrisOriginal=null;
    
   
    
    public BufferedImage mediana (BufferedImage image, int size){
        //mascara seleccionada para realizar el filtro
            short[][] mascara = new short[size][size];
            //arreglo con los pixeles tomados de la mascara ordenados
            short[] mascaraOrdena = new short[size * size];
            
            int nrows, ncols;
		BufferedImage imageFinal;
		nrows = image.getWidth();
		ncols = image.getHeight();
		imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
            
                //inicializacion de los datos de la matriz gris
                /*for(int i=0; i<this.matrizGris.length;i++)
                    System.arraycopy(this.matrizGrisOriginal[i], 0, this.matrizGris[i], 0, this.matrizGris[0].length);
               */
            
                int tope = size/2; //variable que sirve de control para evitar que se desborde la mascara de la matriz
                
                for (int i = tope; i < nrows-tope; i++) {
                    for (int j = tope; j < ncols-tope; j++) {
                        //llenado de la mascara
                        for(int y = 0; y < mascara.length; y++){
                            for(int x = 0; x < mascara[0].length; x++){
                                Color c1 = new Color(image.getRGB(i-tope+y, j-tope+x));
                                mascara[y][x] = (short) c1.getRed();
                                      //  this.imagen.getMatrizGris()[i-tope+y][j-tope+x];
                            }
                        }
                        
                        //llenado del arreglo mascaraOrdenada apartir de la mascara
                        int posicion=0;
                        for(int y = 0; y < mascara.length; y++){
                            for(int x = 0; x < mascara[0].length; x++){
//                                System.out.print(mascara[y][x]+" ");
                                mascaraOrdena[posicion] = mascara[y][x];
                                posicion++;
                            }
  //                          
                        Arrays.sort(mascaraOrdena);
                        //System.out.println("mascara orden");
//                        for(int k=0; k<mascaraOrdena.length;k++){
//                            System.out.print(mascaraOrdena[k]+" ");
//                        }
//                        System.out.println();
                        
                        //***********************************************
                        //Se le asigna a la posicion i,j la mediana de la mascara
                        int mediana = mascaraOrdena[(int)Math.ceil(mascaraOrdena.length/2)];   
                        imageFinal.setRGB(i, j, new Color(mediana, mediana, mediana).getRGB());
                     //  this.matrizGris[i][j] = 
                    }
                }  
                }
        return imageFinal;
    }
    
    
    
    public BufferedImage media (BufferedImage image, int size){
        //mascara seleccionada para realizar el filtro
            short[][] mascara = new short[size][size];
            //arreglo con los pixeles tomados de la mascara ordenados
            short[] mascaraOrdena = new short[size * size];
            
            int nrows, ncols;
		BufferedImage imageFinal;
		nrows = image.getWidth();
		ncols = image.getHeight();
		imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
            
                //inicializacion de los datos de la matriz gris
                /*for(int i=0; i<this.matrizGris.length;i++)
                    System.arraycopy(this.matrizGrisOriginal[i], 0, this.matrizGris[i], 0, this.matrizGris[0].length);
               */
            
                int tope = size/2; //variable que sirve de control para evitar que se desborde la mascara de la matriz
                int acumulado;
                for (int i = tope; i < nrows-tope; i++) {
                    for (int j = tope; j < ncols-tope; j++) {
                        //llenado de la mascara
                        acumulado = 0;
                        for(int y = 0; y < mascara.length; y++){
                            for(int x = 0; x < mascara[0].length; x++){
                                
                                Color c1 = new Color(image.getRGB(i-tope+y, j-tope+x));
                                mascara[y][x] = (short) c1.getRed();
                                acumulado = acumulado + c1.getRed();
                                      //  this.imagen.getMatrizGris()[i-tope+y][j-tope+x];
                            }
                        }
                        
                        
                        //llenado del arreglo mascaraOrdenada apartir de la mascara
                        int posicion=0;
                        for(int y = 0; y < mascara.length; y++){
                            for(int x = 0; x < mascara[0].length; x++){
//                              System.out.print(mascara[y][x]+" ");
                                mascaraOrdena[posicion] = mascara[y][x];
                                posicion++;
                            }
  //                          
                        int promedio = (int) Math.ceil(acumulado / mascaraOrdena.length);
                        //System.out.println("mascara orden");
//                        for(int k=0; k<mascaraOrdena.length;k++){
//                            System.out.print(mascaraOrdena[k]+" ");
//                        }
//                        System.out.println();
                        
                        //***********************************************
                        //Se le asigna a la posicion i,j la mediana de la mascara
                        imageFinal.setRGB(i, j, new Color(promedio, promedio, promedio).getRGB());
                     //  this.matrizGris[i][j] = 
                    }
                }  
                }
        return imageFinal;
    }
    
     public BufferedImage gauss(BufferedImage image, int tamanoMascara){
        
         int nrows, ncols;
		BufferedImage imageFinal;
		nrows = image.getWidth();
		ncols = image.getHeight();
		imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
            
                        //this.matrizGris = new short[imagen.getMatrizGris().length][imagen.getMatrizGris()[0].length];

            
            Convolucion cv = new Convolucion();
            short[][] matrizGris = new short[nrows][ncols];
            //inicializacion de los datos de la matriz gris
            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    Color c1 = new Color(image.getRGB(i, j));
                    int ng = c1.getRed();
                    matrizGris[i][j] = (short) ng;
                }
            }
            
            //mascara seleccionada para realizar el filtro
            short[][] mascara = multiplicaVector(trianguloPascal(tamanoMascara));            
            
            int tope = tamanoMascara / 2; //variable que sirve de control para evitar que se desborde la mascara de la matriz
            //JOptionPane.showMessageDialog(null, "tope: "+tope);   

            for (int i = tope; i < nrows - tope; i++) {
                for (int j = tope; j < ncols - tope; j++) {
                   // this.matrizGris[i][j] = cv.convolucionar(this.matrizGrisOriginal, mascara, i, j);              
                    short intensidad = cv.convolucionar(matrizGris, mascara, i, j);  
                    if(255<intensidad)
                        imageFinal.setRGB(i, j, new Color(255,255,255).getRGB());
                        //matrizGris[i][j] = (short)this.imagen.getNivelIntensidad();
                    else if (intensidad<0)
                        imageFinal.setRGB(i, j, new Color(0,0,0).getRGB());
                        //matrizGris[i][j] = 0;
                    else
                        imageFinal.setRGB(i, j, new Color(intensidad,intensidad,intensidad).getRGB());
                      //  matrizGris[i][j] = intensidad;
                }
            }
        return imageFinal;
    }
     
     public BufferedImage bordes(BufferedImage image){
        
     int nrows, ncols;
		BufferedImage imageFinal;
		nrows = image.getWidth();
		ncols = image.getHeight();
		imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
            
            
            //Convolucion cv = new Convolucion();
            short[][] matrizGris = new short[nrows][ncols];
            //inicializacion de los datos de la matriz gris
            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    Color c1 = new Color(image.getRGB(i, j));
                    int ng = c1.getRed();
                    matrizGris[i][j] = (short) ng;
                }
            }
            
            //mascara seleccionada para realizar el filtro
            short[][] mascara = new short [3][3];            
            
            int tope = 1; //variable que sirve de control para evitar que se desborde la mascara de la matriz
            //JOptionPane.showMessageDialog(null, "tope: "+tope);   

            for (int i = tope; i < nrows-tope; i++) {
                    for (int j = tope; j < ncols-tope; j++) {
                        //llenado de la mascara
                        for(int y = 0; y < mascara.length; y++){
                            for(int x = 0; x < mascara[0].length; x++){
                                Color c1 = new Color(image.getRGB(i-tope+y, j-tope+x));
                                mascara[y][x] = (short) c1.getRed();
                                      //  this.imagen.getMatrizGris()[i-tope+y][j-tope+x];
                            }
                        }
                        
                        //llenado del arreglo mascaraOrdenada apartir de la mascara
                        int lap=0;
                        lap = mascara[0][0] - mascara[0][1] - mascara[0][2] - mascara[1][0] - mascara[1][2]
                                - mascara[2][0] - mascara[2][1] - mascara[2][2] + (8*mascara[1][1]);
                        
                        
                        matrizGris[i][j] = (short) (matrizGris[i][j]+lap);
                        int intensidad = matrizGris[i][j];
                        
                        //***********************************************
                        //Se le asigna a la posicion i,j laplace de la mascara
                        
                          if(255<intensidad)
                        imageFinal.setRGB(i, j, new Color(255,255,255).getRGB());
                        //matrizGris[i][j] = (short)this.imagen.getNivelIntensidad();
                    else if (intensidad<0)
                        imageFinal.setRGB(i, j, new Color(0,0,0).getRGB());
                        //matrizGris[i][j] = 0;
                    else
                        imageFinal.setRGB(i, j, new Color(intensidad,intensidad,intensidad).getRGB());
                      //  matrizGris[i][j] = intensidad;
   
                    }
                }  
        return imageFinal;

            }
 
     
     
     
     
     private short[][] multiplicaVector(short [] A){
        short [][] C = new short[A.length][A.length];
        
        for(int i=0; i<C.length; i++){
            for(int j=0; j<C[0].length;j++){
                C[i][j] = (short) (A[i] * A[j]);
                //System.out.print(C[i][j]+" ");
            }
            //System.out.println();
        }
        return C;
    }
    
    private short [] trianguloPascal(int nivel){
    
        short [] aux = new short[nivel];
        
        ArrayList resultado = new ArrayList();
        ArrayList base = new ArrayList();
        
        base.add(1);
        base.add(1);
        
        for (int i=2; i< nivel; i++){
            resultado.add(1);
            for(int j=1; j < base.size();j++){
                int dato = (Integer) base.get(j-1)+ (Integer)base.get(j);
                resultado.add(dato);
            }
            
            resultado.add(1);
            base.removeAll(base);
           
            for(int k=0; k<resultado.size(); k++){
                int dato = (Integer) resultado.get(k);
                base.add(dato);
            }
            //JOptionPane.showMessageDialog(null, "Tamano Base "+base.size());
            if(i < nivel-1)
                resultado.removeAll(resultado);
            //JOptionPane.showMessageDialog(null, "Tamano resultado "+resultado.size());
        } 
        
        //System.out.println("**********Pascal*********************");
        for(int k=0; k<resultado.size(); k++){
            int dato = (Integer)resultado.get(k);
            aux[k]= (short) dato;    
            //System.out.println(aux[k]); 
        } 
        return aux;
    }
    
    
   public BufferedImage negativo(BufferedImage image){
       int nrows, ncols;
		BufferedImage imageFinal;
		nrows = image.getWidth();
		ncols = image.getHeight();
		imageFinal = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
                int r=0, T=0;
                 for (int i = 0; i < nrows; i++) {
                    for (int j = 0; j < ncols; j++) {
                        Color c1 = new Color(image.getRGB(i, j));
                        r = c1.getRed();
                        T = -r+255;
                        imageFinal.setRGB(i, j, new Color(T,T,T).getRGB());
                        
                    }
                 }
                 
       return imageFinal;
   }
    }
    
 

