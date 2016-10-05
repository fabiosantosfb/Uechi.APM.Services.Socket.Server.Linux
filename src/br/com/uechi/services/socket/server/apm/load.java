/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.uechi.services.socket.server.apm;

import br.com.uechi.util.arquivos;
import br.com.uechi.util.propriedades;
import br.com.uechi.util.tempo;
import br.com.uechi.util.tratar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author paulo.uechi
 */
public class load {
    private Socket objSocket;
    private String strParameter;
    private propriedades objPrp = new propriedades();

    public boolean start() {
        boolean booRet = true;
        try {
            String strReturn = null;
            int intOption = 0;
            int port = tratar.ToIntDBNull(objPrp.configuracao(0, "Porta"));
            ServerSocket objServerSocket = new ServerSocket(port);
            System.out.println("[" + tempo.pegaDataHora24H() + "] - Uechi.Server.Monitor inicializado na porta: " + String.valueOf(port));
            while (true) {
                objSocket = objServerSocket.accept();
                InputStream objStm = objSocket.getInputStream();
                InputStreamReader objStr = new InputStreamReader(objStm);
                BufferedReader objBfr = new BufferedReader(objStr);
                String strParameter = objBfr.readLine();
                try {
                    intOption = options(strParameter);
                    System.out.println("[" + tempo.pegaDataHora24H() + "] - Uechi.Server.Monitor Parametro recebido: " + strParameter);
                    strReturn = parameters(intOption, strParameter) + "\n";
                } catch (NumberFormatException e) {
                    strReturn = "Error parameters.\n";
                    System.out.println("[" + tempo.pegaDataHora24H() + "] - Uechi.Server.Monitor Erro Parametro: " + e.getMessage());
                }
                OutputStream objOts = objSocket.getOutputStream();
                OutputStreamWriter objOtw = new OutputStreamWriter(objOts);
                BufferedWriter objBfw = new BufferedWriter(objOtw);
                objBfw.write(strReturn);
                System.out.println("[" + tempo.pegaDataHora24H() + "] - Uechi.Server.Monitor Retorno enviado: " + strReturn);
                objBfw.flush();
            }
        } catch (Exception e) {
            System.out.println("[" + tempo.pegaDataHora24H() + "] - Uechi.Server.Monitor Erro: " + e.getMessage());
        } finally {
            try {
                objSocket.close();
            } catch (Exception e) {
                System.out.println("[" + tempo.pegaDataHora24H() + "] - Uechi.Server.Monitor Socket Erro: " + e.getMessage());
            }
        }

        return booRet;
    }

    private String parameters(int intOpt, String strParameter) {
        String strRet = null;
        try {
            if (strParameter != null && strParameter.length() > 0) {
                if (validate(strParameter)) {
                    strRet = command(intOpt);
                }
            }
        } catch (Exception e) {
            strRet = null;
        }
        return strRet;
    }

    private boolean validate(String strParameter) {
        boolean booVal = false;
        String strChave;
        String strValida;
        try {
            if (strParameter != null && strParameter.length() > 0) {
                strChave = strParameter.substring(0, 32);
                strValida = objPrp.configuracao(0, "Chave");
                if (strValida.length() > 0) {
                    if (strChave.toLowerCase().equals(strValida.toLowerCase())) {
                        booVal = true;
                    }
                }
            }
        } catch (Exception e) {
            booVal = false;
        }
        return booVal;
    }

    private int options(String strParameter) {
        int intRet = 0;
        String strSubParameter;
        try {
            if (strParameter != null && strParameter.length() > 0) {
                strSubParameter = strParameter.substring(32, 33);
                intRet = tratar.ToIntDBNull(strSubParameter);
            }
        } catch (Exception e) {
            intRet = 0;
        }
        return intRet;
    }

    private String command(int intOpt) {
        /*
        Importants
        command sar: www.thegeekstuff.com/2011/03/sar-examples/?utm_source=feedburner
        command sar: linux.die.net/man/1/sar
        command sar: www.computerhope.com/unix/usar.htm
        command awk: www.dltec.com.br/blog/linux/uma-introducao-ao-uso-do-awk-no-linux/
        command awk: www.vivaolinux.com.br/dica/Awk-Uma-poderosa-ferramenta-de-analise
        script: www.tecmint.com/linux-server-health-monitoring-script/
        */
       
        String strOut = "";
        String strCmd = null;
        String strLine = null;
        boolean boocmd = true;
        try {
            if (intOpt == 1) {
                // CPU
                // strCmd = "iostat";
                // strCmd = "sar -f 1 1";
                strCmd = arquivos.diretorioRaiz("uechi.command.sh") + " " + intOpt;
            } else if (intOpt == 2) {
                // Memory
                //strCmd = "free -m | awk 'NR==2{printf \"Memory Usage: %s/%sMB (%.2f%%)\\n\", $3,$2,$3*100/$2 }'";
                strCmd = arquivos.diretorioRaiz("uechi.command.sh") + " " + intOpt;
            } else if (intOpt == 3) {
                // Disk
                //strCmd = "df -h | awk '$NF==\"/\"{printf \"Disk Usage: %d/%dGB (%s)\\n\", $3,$2,$5}'";
                strCmd = arquivos.diretorioRaiz("uechi.command.sh") + " " + intOpt;
            } else if (intOpt == 4) {
                // Network
                //strCmd = "sar -f -n DEV 1 1";
                strCmd = arquivos.diretorioRaiz("uechi.command.sh") + " " + intOpt;
            } else if (intOpt == 9) {
                // All
                strCmd = arquivos.diretorioRaiz("uechi.monitor.sh");
            } else {
                boocmd = false;
            }
            if (boocmd) {
                Process objProcess = Runtime.getRuntime().exec(strCmd);
                BufferedReader objBfr = new BufferedReader(new InputStreamReader(objProcess.getInputStream()));
                while ((strLine = objBfr.readLine()) != null) {
                    strOut += strLine + ";";
                }
                objProcess.destroy();
            }
        } catch (Exception e) {
            strOut = e.getMessage();
        }
        return strOut;
    }

    
}
