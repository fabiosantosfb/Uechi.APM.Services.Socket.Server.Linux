/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.uechi.services.socket.server.apm;

import br.com.uechi.util.constantes;

/**
 *
 * @author paulo.uechi
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        constantes.strPROPERTIES_FILE = "uechi.properties";
        load objLod = new load();
        objLod.start();
    }    
}
