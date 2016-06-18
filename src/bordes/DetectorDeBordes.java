package bordes;

import enums.Canal;
import java.awt.image.BufferedImage;
import modelo.Imagen;
import utiles.FiltroGaussiano;
import utiles.FiltroNuevo;
import utiles.MatricesManager;



public class DetectorDeBordes {

    private static float[][] calcularMascaraDePrewittEnY() {

        float[][] mascaraDePrewittEnY = new float[3][3];

        mascaraDePrewittEnY[0][0] = -1;
        mascaraDePrewittEnY[0][1] = 0;
        mascaraDePrewittEnY[0][2] = 1;
        mascaraDePrewittEnY[1][0] = -1;
        mascaraDePrewittEnY[1][1] = 0;
        mascaraDePrewittEnY[1][2] = 1;
        mascaraDePrewittEnY[2][0] = -1;
        mascaraDePrewittEnY[2][1] = 0;
        mascaraDePrewittEnY[2][2] = 1;

        return mascaraDePrewittEnY;
    }

    private static float[][] calcularMascaraDePrewittEnX() {

        float[][] mascaraDePrewittEnX = new float[3][3];

        mascaraDePrewittEnX[0][0] = -1;
        mascaraDePrewittEnX[0][1] = -1;
        mascaraDePrewittEnX[0][2] = -1;
        mascaraDePrewittEnX[1][0] = 0;
        mascaraDePrewittEnX[1][1] = 0;
        mascaraDePrewittEnX[1][2] = 0;
        mascaraDePrewittEnX[2][0] = 1;
        mascaraDePrewittEnX[2][1] = 1;
        mascaraDePrewittEnX[2][2] = 1;

        return mascaraDePrewittEnX;
    }

    public static BufferedImage aplicarDetectorDePrewitt(Imagen imagenOriginal) {

        float[][] mascaraDePrewittEnX = calcularMascaraDePrewittEnX();
        float[][] mascaraDePrewittEnY = calcularMascaraDePrewittEnY();

        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDePrewittEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDePrewittEnY);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);

        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);

        //Sintetizamos usando la raiz de los cuadrados
        int[][] matrizRojosSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizRojoEnX, matrizRojoEnY));
        int[][] matrizVerdesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizVerdeEnX, matrizVerdeEnY));
        int[][] matrizAzulesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizAzulEnX, matrizAzulEnY));

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojosSintetizadosYTransformados, matrizVerdesSintetizadosYTransformados, matrizAzulesSintetizadosYTransformados));

        return imagenResultante.getBufferedImage();
    }

    public static BufferedImage mostrarMascaraDePrewittEnX(Imagen imagenOriginal) {

        float[][] mascaraDePrewittEnX = calcularMascaraDePrewittEnX();

        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDePrewittEnX);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);

        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

        return imagenResultante.getBufferedImage();

    }

    public static BufferedImage mostrarMascaraDePrewittEnY(Imagen imagenOriginal) {

        float[][] mascaraDePrewittEnY = calcularMascaraDePrewittEnY();

        Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDePrewittEnY);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);

        int[][] matrizRojoEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnY);
        int[][] matrizVerdeEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnY);
        int[][] matrizAzulEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnY);

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnYTransformada, matrizVerdeEnYTransformada, matrizAzulEnYTransformada));

        return imagenResultante.getBufferedImage();

    }

    public static float[][] calcularMascaraDeSobelEnY() {

        float[][] mascaraDeSobelEnY = new float[3][3];

        mascaraDeSobelEnY[0][0] = -1;
        mascaraDeSobelEnY[0][1] = 0;
        mascaraDeSobelEnY[0][2] = 1;
        mascaraDeSobelEnY[1][0] = -2;
        mascaraDeSobelEnY[1][1] = 0;
        mascaraDeSobelEnY[1][2] = 2;
        mascaraDeSobelEnY[2][0] = -1;
        mascaraDeSobelEnY[2][1] = 0;
        mascaraDeSobelEnY[2][2] = 1;
        
        return mascaraDeSobelEnY;
    }

    public static float[][] calcularMascaraDeSobelEnX() {

        float[][] mascaraDeSobelEnX = new float[3][3];

        mascaraDeSobelEnX[0][0] = -1;
        mascaraDeSobelEnX[0][1] = -2;
        mascaraDeSobelEnX[0][2] = -1;
        mascaraDeSobelEnX[1][0] = 0;
        mascaraDeSobelEnX[1][1] = 0;
        mascaraDeSobelEnX[1][2] = 0;
        mascaraDeSobelEnX[2][0] = 1;
        mascaraDeSobelEnX[2][1] = 2;
        mascaraDeSobelEnX[2][2] = 1;

        return mascaraDeSobelEnX;
    }

    public static BufferedImage aplicarDetectorDeSobel(Imagen imagenOriginal) {

        float[][] mascaraDeSobelEnX = calcularMascaraDeSobelEnX();
        float[][] mascaraDeSobelEnY = calcularMascaraDeSobelEnY();

        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);

        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);

        //Sintetizamos usando la raiz de los cuadrados
        int[][] matrizRojosSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizRojoEnX, matrizRojoEnY));
        int[][] matrizVerdesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizVerdeEnX, matrizVerdeEnY));
        int[][] matrizAzulesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizAzulEnX, matrizAzulEnY));

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojosSintetizadosYTransformados, matrizVerdesSintetizadosYTransformados, matrizAzulesSintetizadosYTransformados));

        return imagenResultante.getBufferedImage();
    }

    public static BufferedImage mostrarMascaraDeSobelEnX(Imagen imagenOriginal) {

        float[][] mascaraDeSobelEnX = calcularMascaraDeSobelEnX();

        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);

        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

        return imagenResultante.getBufferedImage();

    }

    public static BufferedImage mostrarMascaraDeSobelEnY(Imagen imagenOriginal) {

        float[][] mascaraDeSobelEnY = calcularMascaraDeSobelEnY();

        Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);

        int[][] matrizRojoEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnY);
        int[][] matrizVerdeEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnY);
        int[][] matrizAzulEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnY);

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnYTransformada, matrizVerdeEnYTransformada, matrizAzulEnYTransformada));

        return imagenResultante.getBufferedImage();

    }

    private static float[][] calcularMascaraDeLaplaciano() {

        float[][] mascaraDeLaplaciano = new float[3][3];

        mascaraDeLaplaciano[0][0] = 0;
        mascaraDeLaplaciano[0][1] = -1;
        mascaraDeLaplaciano[0][2] = 0;
        mascaraDeLaplaciano[1][0] = -1;
        mascaraDeLaplaciano[1][1] = 4;
        mascaraDeLaplaciano[1][2] = -1;
        mascaraDeLaplaciano[2][0] = 0;
        mascaraDeLaplaciano[2][1] = -1;
        mascaraDeLaplaciano[2][2] = 0;

        return mascaraDeLaplaciano;
    }

    public static BufferedImage aplicarDetectorLaplaciano(Imagen imagenOriginal) {

        float[][] mascaraDeLaplaciano = calcularMascaraDeLaplaciano();
        int[][] matrizCrucesPorCerosRojo = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
        int[][] matrizCrucesPorCerosVerde = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
        int[][] matrizCrucesPorCerosAzul = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];

        int[][] matrizRojoTranspuesta = new int[imagenOriginal.getBufferedImage().getHeight()][imagenOriginal.getBufferedImage().getWidth()];
        int[][] matrizVerdeTranspuesta = new int[imagenOriginal.getBufferedImage().getHeight()][imagenOriginal.getBufferedImage().getWidth()];
        int[][] matrizAzulTranspuesta = new int[imagenOriginal.getBufferedImage().getHeight()][imagenOriginal.getBufferedImage().getWidth()];

        int umbral = 30;

        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeLaplaciano);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojo = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerde = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzul = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);

        for (int j = 0; j < matrizCrucesPorCerosRojo.length; j++) {
            for (int i = 0; i < matrizCrucesPorCerosRojo[0].length; i++) {
                matrizRojoTranspuesta[i][j] = matrizRojo[j][i];
                matrizVerdeTranspuesta[i][j] = matrizVerde[j][i];
                matrizAzulTranspuesta[i][j] = matrizAzul[j][i];
            }
        }

        calcularCrucesPorCero(imagenOriginal, matrizCrucesPorCerosRojo,
                matrizCrucesPorCerosVerde, matrizCrucesPorCerosAzul,
                matrizRojoTranspuesta, matrizVerdeTranspuesta,
                matrizAzulTranspuesta, umbral, imagenResultante, matrizRojo,
                matrizVerde, matrizAzul);

        return imagenResultante.getBufferedImage();
    }

    private static void calcularCrucesPorCero(Imagen imagenOriginal,
            int[][] matrizCrucesPorCerosRojo,
            int[][] matrizCrucesPorCerosVerde,
            int[][] matrizCrucesPorCerosAzul, int[][] matrizRojoTranspuesta,
            int[][] matrizVerdeTranspuesta, int[][] matrizAzulTranspuesta,
            int umbral, Imagen imagenResultante, int[][] matrizRojo,
            int[][] matrizVerde, int[][] matrizAzul) {

        if (imagenOriginal.getBufferedImage().getWidth() == imagenOriginal.getBufferedImage().getHeight()) {
            for (int i = 0; i < matrizRojo[0].length; i++) {
                for (int j = 0; j < matrizRojo.length; j++) {

                    if (hayCambioDeSignoPorFilaYUmbral(matrizRojo, i, j, umbral)) {
                        matrizCrucesPorCerosRojo[i][j] = 255;
                    } else {
                        matrizCrucesPorCerosRojo[i][j] = 0;
                    }

                    if (hayCambioDeSignoPorFilaYUmbral(matrizVerde, i, j, umbral)) {
                        matrizCrucesPorCerosVerde[i][j] = 255;
                    } else {
                        matrizCrucesPorCerosVerde[i][j] = 0;
                    }

                    if (hayCambioDeSignoPorFilaYUmbral(matrizAzul, i, j, umbral)) {
                        matrizCrucesPorCerosAzul[i][j] = 255;
                    } else {
                        matrizCrucesPorCerosAzul[i][j] = 0;
                    }

                }
            }
            imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizCrucesPorCerosRojo, matrizCrucesPorCerosVerde, matrizCrucesPorCerosAzul));
        } else {
            for (int i = 0; i < matrizRojo[0].length; i++) {
                for (int j = 0; j < matrizRojo.length; j++) {

                    if (hayCambioDeSignoPorFilaYUmbral(matrizRojoTranspuesta, i, j, umbral)) {
                        matrizCrucesPorCerosRojo[j][i] = 255;
                    } else {
                        matrizCrucesPorCerosRojo[j][i] = 0;
                    }

                    if (hayCambioDeSignoPorFilaYUmbral(matrizVerdeTranspuesta, i, j, umbral)) {
                        matrizCrucesPorCerosVerde[j][i] = 255;
                    } else {
                        matrizCrucesPorCerosVerde[j][i] = 0;
                    }

                    if (hayCambioDeSignoPorFilaYUmbral(matrizAzulTranspuesta, i, j, umbral)) {
                        matrizCrucesPorCerosAzul[j][i] = 255;
                    } else {
                        matrizCrucesPorCerosAzul[j][i] = 0;
                    }

                }

            }
            imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizCrucesPorCerosRojo, matrizCrucesPorCerosVerde, matrizCrucesPorCerosAzul));
        }
    }

    public static BufferedImage mostrarMascaraCrucesPorCeros(Imagen imagenOriginal) {

        float[][] mascaraDeLaplaciano = calcularMascaraDeLaplaciano();
        int[][] matrizCrucesPorCerosRojo = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
        int[][] matrizCrucesPorCerosVerde = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
        int[][] matrizCrucesPorCerosAzul = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];

        int[][] matrizRojoTranspuesta = new int[imagenOriginal.getBufferedImage().getHeight()][imagenOriginal.getBufferedImage().getWidth()];
        int[][] matrizVerdeTranspuesta = new int[imagenOriginal.getBufferedImage().getHeight()][imagenOriginal.getBufferedImage().getWidth()];
        int[][] matrizAzulTranspuesta = new int[imagenOriginal.getBufferedImage().getHeight()][imagenOriginal.getBufferedImage().getWidth()];

        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeLaplaciano);
        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        //Aplicamos filtros en X y en Y
        int[][] matrizRojo = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerde = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzul = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);

        for (int j = 0; j < matrizCrucesPorCerosRojo.length; j++) {
            for (int i = 0; i < matrizCrucesPorCerosRojo[0].length; i++) {
                matrizRojoTranspuesta[i][j] = matrizRojo[j][i];
                matrizVerdeTranspuesta[i][j] = matrizVerde[j][i];
                matrizAzulTranspuesta[i][j] = matrizAzul[j][i];
            }
        }

        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        if (imagenOriginal.getBufferedImage().getWidth() == imagenOriginal.getBufferedImage().getHeight()) {

            for (int i = 0; i < matrizRojo[0].length; i++) {
                for (int j = 0; j < matrizRojo.length; j++) {

                    if (hayCambioDeSignoPorFila(matrizRojo, i, j)) {
                        matrizCrucesPorCerosRojo[i][j] = 255;
                    } else {
                        matrizCrucesPorCerosRojo[i][j] = 0;
                    }

                    if (hayCambioDeSignoPorFila(matrizVerde, i, j)) {
                        matrizCrucesPorCerosVerde[i][j] = 255;
                    } else {
                        matrizCrucesPorCerosVerde[i][j] = 0;
                    }

                    if (hayCambioDeSignoPorFila(matrizAzul, i, j)) {
                        matrizCrucesPorCerosAzul[i][j] = 255;
                    } else {
                        matrizCrucesPorCerosAzul[i][j] = 0;
                    }

                }

            }
            imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizCrucesPorCerosRojo, matrizCrucesPorCerosVerde, matrizCrucesPorCerosAzul));
        } else {
            for (int i = 0; i < matrizRojo[0].length; i++) {
                for (int j = 0; j < matrizRojo.length; j++) {

                    if (hayCambioDeSignoPorFila(matrizRojoTranspuesta, i, j)) {
                        matrizCrucesPorCerosRojo[j][i] = 255;
                    } else {
                        matrizCrucesPorCerosRojo[j][i] = 0;
                    }

                    if (hayCambioDeSignoPorFila(matrizVerdeTranspuesta, i, j)) {
                        matrizCrucesPorCerosVerde[j][i] = 255;
                    } else {
                        matrizCrucesPorCerosVerde[j][i] = 0;
                    }

                    if (hayCambioDeSignoPorFila(matrizAzulTranspuesta, i, j)) {
                        matrizCrucesPorCerosAzul[j][i] = 255;
                    } else {
                        matrizCrucesPorCerosAzul[j][i] = 0;
                    }

                }

            }

            imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizCrucesPorCerosRojo, matrizCrucesPorCerosVerde, matrizCrucesPorCerosAzul));
        }

        return imagenResultante.getBufferedImage();
    }

    public static BufferedImage mostrarMascaraDeLaplaciano(Imagen imagenOriginal) {

        float[][] mascaraDeLaplaciano = calcularMascaraDeLaplaciano();

        Imagen imagenFiltrada = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
        Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        FiltroNuevo filtro = new FiltroNuevo(mascaraDeLaplaciano);

        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtro.filtrar(imagenFiltrada, Canal.ROJO);
        int[][] matrizVerdeEnX = filtro.filtrar(imagenFiltrada, Canal.VERDE);
        int[][] matrizAzulEnX = filtro.filtrar(imagenFiltrada, Canal.AZUL);

        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

        return imagenResultante.getBufferedImage();

    }
    
    private static float[][] generarMascaraLaplacianoDelGaussiano(double sigma) {

		int posibleTamanio = (int) ((3 * sigma) % 2 == 0 ? 3 * sigma : (3 * sigma) + 1);
		int tamanioMascara = sigma < 1 ? 5 : posibleTamanio + 1;

		float[][] matrizDeLaplacianoDelGaussiano = new float[tamanioMascara][tamanioMascara];

		double primerTermino = -1.0 * (Math.sqrt(2 * Math.PI) * Math.pow(sigma, 3.0));
		double segundoTermino = 0;

		for (int i = 0; i < tamanioMascara; i++) {
			for (int j = 0; j < tamanioMascara; j++) {

				segundoTermino = (Math.pow(i, 2.0) + Math.pow(j, 2.0)) / Math.pow(sigma, 2.0);
				matrizDeLaplacianoDelGaussiano[i][j] = (float) (primerTermino * (2.0 - segundoTermino)
                                        * Math.exp((-0.5) * segundoTermino));
			}
		}

		return matrizDeLaplacianoDelGaussiano;
	}

    private static float[][] calcularMascaraDeLaplacianoDelGaussiano(int longitudMascara, double sigma) {

        float[][] mascaraDeLaplacianoDeGaussiano = new float[longitudMascara][longitudMascara];

        for (int j = 0; j < longitudMascara; j++) {
            for (int i = 0; i < longitudMascara; i++) {

                mascaraDeLaplacianoDeGaussiano[i][j] = (float) calcularValorMascaraLaplacianoDelGaussiano(i - (longitudMascara / 2), j
                        - (longitudMascara / 2), sigma);
            }
        }
        //System.out.println(Arrays.toString(mascaraDeLaplacianoDeGaussiano));
        return mascaraDeLaplacianoDeGaussiano;
    }

    
    private static float calcularValorMascaraLaplacianoDelGaussiano(int indiceI, int indiceJ, double sigma) {

       /* float termino1 =  (float) (-1 / ((Math.PI) * Math.pow(sigma, 4)));
        float termino2 = (float) (1 - (((Math.pow(indiceI, 2))+(Math.pow(indiceJ, 2))) / (2 * Math.pow(sigma, 2))));
        float termino3 = (float) Math.exp(- (((Math.pow(indiceI, 2))+(Math.pow(indiceJ, 2))) / (2 * Math.pow(sigma, 2)))); 
       // float termino3 = (float) Math.pow(Math.E, (-1 * ((float) (Math.pow(indiceI, 2) + Math.pow(indiceJ, 2)) / (2 * Math.pow(sigma, 2)))));
       */
        float termino1 = (float) (((indiceI*indiceI)+(indiceJ*indiceJ)-(2*Math.pow(sigma, 2)))/(Math.pow(sigma, 4)));
        float termino2 = (float) Math.exp(-1*(((indiceI*indiceI)+(indiceJ*indiceJ))/(2*Math.pow(sigma, 2))));
        //float termino3 
        
        float t1 = (float) (-1.0 * (Math.sqrt(2 * Math.PI) * Math.pow(sigma, 3.0)));
        float t2 = (float) ((Math.pow(indiceI, 2.0) + Math.pow(indiceJ, 2.0)) / Math.pow(sigma, 2.0));
	
        
        float valor = termino1 * termino2 *10;
        float vF = (float) (t1 * (2.0 - t2) * Math.exp((-0.5) * t2));
        System.out.println(valor);
        return valor;
    }

    public static BufferedImage aplicarDetectorLaplacianoDelGaussiano(Imagen imagenOriginal, double sigma, int umbral) {

       int posibleTamanio = (int) ((3 * sigma) % 2 == 0 ? 3 * sigma : (3 * sigma) + 1);
		int longitudMascara = sigma < 1 ? 5 : posibleTamanio + 1;


       // float[][] mascaraLaplacianoDelGaussiano = generarMascaraLaplacianoDelGaussiano(sigma);
        float[][] mascaraLaplacianoDelGaussiano = calcularMascaraDeLaplacianoDelGaussiano(longitudMascara, sigma);

        FiltroNuevo filtroLaplacianoDelGaussiano = new FiltroNuevo(mascaraLaplacianoDelGaussiano);

        Imagen imagenFiltrada = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre() + "_filtroLoG", imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

        //Aplicamos filtro
        int[][] matrizRojo = filtroLaplacianoDelGaussiano.filtrar(imagenFiltrada, Canal.ROJO);
        int[][] matrizVerde = filtroLaplacianoDelGaussiano.filtrar(imagenFiltrada, Canal.VERDE);
        int[][] matrizAzul = filtroLaplacianoDelGaussiano.filtrar(imagenFiltrada, Canal.AZUL);

        int[][] matrizCrucesPorCerosRojo = new int[imagenFiltrada.getBufferedImage().getWidth()][imagenFiltrada.getBufferedImage().getHeight()];
        int[][] matrizCrucesPorCerosVerde = new int[imagenFiltrada.getBufferedImage().getWidth()][imagenFiltrada.getBufferedImage().getHeight()];
        int[][] matrizCrucesPorCerosAzul = new int[imagenFiltrada.getBufferedImage().getWidth()][imagenFiltrada.getBufferedImage().getHeight()];

        int[][] matrizRojoTranspuesta = new int[imagenFiltrada.getBufferedImage().getHeight()][imagenFiltrada.getBufferedImage().getWidth()];
        int[][] matrizVerdeTranspuesta = new int[imagenFiltrada.getBufferedImage().getHeight()][imagenFiltrada.getBufferedImage().getWidth()];
        int[][] matrizAzulTranspuesta = new int[imagenFiltrada.getBufferedImage().getHeight()][imagenFiltrada.getBufferedImage().getWidth()];

        for (int j = 0; j < matrizCrucesPorCerosRojo.length; j++) {
            for (int i = 0; i < matrizCrucesPorCerosRojo[0].length; i++) {
                matrizRojoTranspuesta[i][j] = matrizRojo[j][i];
                matrizVerdeTranspuesta[i][j] = matrizVerde[j][i];
                matrizAzulTranspuesta[i][j] = matrizAzul[j][i];
            }
        }

        calcularCrucesPorCero(imagenOriginal, matrizCrucesPorCerosRojo,
                matrizCrucesPorCerosVerde, matrizCrucesPorCerosAzul,
                matrizRojoTranspuesta, matrizVerdeTranspuesta,
                matrizAzulTranspuesta, umbral, imagenFiltrada, matrizRojo,
                matrizVerde, matrizAzul);

        return imagenFiltrada.getBufferedImage();
    }

    	public static Imagen laplacianoGaussiano(Imagen imagen, double sigma) {

            int[][] matrizPixelesLoG = new int[imagen.getBufferedImage().getWidth()][imagen.getBufferedImage().getHeight()];

		float[][] mascaraLaplaciano = generarMascaraLaplacianoDelGaussiano(sigma);
		int posicionCentralMascara = mascaraLaplaciano.length / 2;

		 Imagen imagenFiltrada = new Imagen(imagen.getBufferedImage(), imagen.getFormato(), imagen.getNombre() + "_filtroLoG", imagen.getMatriz(Canal.ROJO), imagen.getMatriz(Canal.VERDE), imagen.getMatriz(Canal.AZUL));

		for (int i = posicionCentralMascara; i < imagen.getBufferedImage().getWidth() - posicionCentralMascara; i++) {
			for (int j = posicionCentralMascara; j < imagen.getBufferedImage().getHeight() - posicionCentralMascara; j++) {

				float valorResultado = 0f;

				for (int xMascaraEnImagen = i - posicionCentralMascara, xMascara = 0; xMascaraEnImagen <= i
						+ posicionCentralMascara; xMascaraEnImagen++, xMascara++) {
					for (int yMascaraEnImagen = j - posicionCentralMascara, yMascara = 0; yMascaraEnImagen <= j
							+ posicionCentralMascara; yMascaraEnImagen++, yMascara++) {

						double valorMascara = mascaraLaplaciano[xMascara][yMascara];
						int nivelGrisPixel = imagen.getBufferedImage().getRGB(xMascaraEnImagen, yMascaraEnImagen);

						double operacion = nivelGrisPixel * valorMascara;
						valorResultado += operacion;
					}
				}

				int valorEntero = (int) valorResultado;
				matrizPixelesLoG[i][j] = valorEntero;
			}
		}

		for (int i = 0; i < imagen.getBufferedImage().getWidth(); i++) {
			for (int j = 0; j < imagen.getBufferedImage().getHeight(); j++) {

				if (hayCambioDeSignoPorFila(matrizPixelesLoG, i, j)
						|| hayCambioDeSignoPorColumna(matrizPixelesLoG, i, j)) {
                                    imagenFiltrada.getBufferedImage().setRGB(i, j, 255);
                                    //imagePlus.getProcessor().putPixel(i, j, 255);
				} else {
                                    imagenFiltrada.getBufferedImage().setRGB(i, j, 0);
					//imagePlus.getProcessor().putPixel(i, j, 0);
				}
			}
		}
		return imagenFiltrada;
	}
    
    public static BufferedImage mostrarMascaraLaplacianoDelGaussiano(Imagen imagenOriginal, double sigma) {

        int longitudMascara = (int) (4+(sigma * 3));
        if (longitudMascara % 2 == 0) {

            longitudMascara = longitudMascara + 1;
        }

        float[][] mascaraDeLaplacianoDelGaussiano = calcularMascaraDeLaplacianoDelGaussiano(longitudMascara, sigma);

        Imagen imagenFiltrada = FiltroGaussiano.aplicarFiltroGaussiano(imagenOriginal, (int) sigma);

        Imagen imagenResultante = new Imagen(imagenFiltrada.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenFiltrada.getMatriz(Canal.ROJO), imagenFiltrada.getMatriz(Canal.VERDE), imagenFiltrada.getMatriz(Canal.AZUL));
        FiltroNuevo filtro = new FiltroNuevo(mascaraDeLaplacianoDelGaussiano);

        int[][] matrizRojo = filtro.filtrar(imagenFiltrada, Canal.ROJO);
        int[][] matrizVerde = filtro.filtrar(imagenFiltrada, Canal.VERDE);
        int[][] matrizAzul = filtro.filtrar(imagenFiltrada, Canal.AZUL);

        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojo);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerde);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzul);

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

        return imagenResultante.getBufferedImage();
    }

    private static boolean hayCambioDeSignoPorFila(int[][] matriz, int i, int j) {
        if (j - 1 >= 0) {

            int valorActual = matriz[i][j];

            int valorAnterior = matriz[i][j - 1];
            if (valorAnterior == 0 && j - 2 >= 0) {
                valorAnterior = matriz[i][j - 2];
            }

            if ((valorAnterior < 0 && valorActual > 0) || (valorAnterior > 0 && valorActual < 0)) {
                return true;
            }
        }
        return false;
    }
    
      private static boolean hayCambioDeSignoPorColumna(int[][] matriz, int x, int y) {

        boolean hayCambio = false;

        if (x - 1 >= 0) {

            int valorActual = matriz[x][y];
            int valorAnterior = matriz[x-1][y];

            if (valorAnterior == 0 && x - 2 >= 0) {
                valorAnterior = matriz[x - 2][y];
            }

            hayCambio = (valorAnterior < 0 && valorActual > 0)
                    || (valorAnterior > 0 && valorActual < 0);
        }
        return hayCambio;
    }

    private static boolean hayCambioDeSignoPorFilaYUmbral(int[][] matriz, int i, int j, int umbral) {

        if (j - 1 >= 0) {

            int valorActual = matriz[i][j];

            int valorAnterior = matriz[i][j - 1];
            if (valorAnterior == 0 && j - 2 >= 0) {
                valorAnterior = matriz[i][j - 2];
            }

            if ((valorAnterior < 0 && valorActual > 0) || (valorAnterior > 0 && valorActual < 0)) {
                if (Math.abs(valorActual - valorAnterior) > umbral) {
                    return true;
                }
            }

        }

        return false;
    }

    public static int[][] sintetizar(int[][] matrizEnX, int[][] matrizEnY) {

        int[][] matrizFinal = new int[matrizEnX.length][matrizEnX[0].length];

        for (int i = 0; i < matrizEnX.length; i++) {
            for (int j = 0; j < matrizEnX[0].length; j++) {
                
                matrizFinal[i][j] = (int) Math.hypot(matrizEnX[i][j], matrizEnY[i][j]);
            }
        }

        return matrizFinal;
    }

}
