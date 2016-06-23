/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.Pruebas;

import java.io.File;

/**
 *
 * @author javi_
 */
public class mainPruebas {
    public static void main(String args[]){
        
        mainPruebas obj = new mainPruebas();
	System.out.println(obj.getFile("Abuela/Frame1.jpeg"));	
    }
    
    private String getFile(String fileName) {


	//Get file from resources folder
	ClassLoader classLoader = getClass().getClassLoader();
	String res = classLoader.getResource(fileName).getPath();

	
		
	return res;

  }

}
