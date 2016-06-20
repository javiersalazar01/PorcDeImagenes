package com.untref.dialogs;

import com.untref.enums.NivelMensaje;
import java.awt.Component;

import javax.swing.JOptionPane;




public class DialogsHelper {
	
	public static void mostarMensaje(Component ventana, String mensaje, NivelMensaje nivel){
		
		JOptionPane.showMessageDialog(ventana,
			    mensaje,
			    nivel.getTitulo(),
			    nivel.getNivelDeError());
	}
	
	public static void mostarMensaje(Component ventana, String mensaje){
		
		JOptionPane.showMessageDialog(ventana, mensaje);
	}

}
