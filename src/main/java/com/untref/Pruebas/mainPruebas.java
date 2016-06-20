/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.untref.Pruebas;

/**
 *
 * @author javi_
 */
public class mainPruebas {
    public static void main(String args[]){
        
        double sin = 64 * Math.sin(Math.toRadians(-60));
        System.out.println(sin);
        int ro = -200;
        double compi = (0 * Math.cos(Math.toRadians(-60)));
        double compj = (64 * Math.sin(Math.toRadians(-60)));
        double casires = ro -  compi - compj;
        double res = Math.abs(ro -  compi - compj);
        System.out.println(compi);
        System.out.println(compj);
        System.out.println(casires);
        System.out.println(res);
    }
}
