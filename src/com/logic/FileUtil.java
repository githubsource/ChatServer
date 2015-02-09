/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author tranngocdien
 */
public class FileUtil {
    
    public static void writeFile(String file, String txt) {
        try {
             FileWriter fileWriter = new FileWriter(file);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(txt); 
            bufferedWriter.close();
        } catch(Exception ex) {

        }
    }
    
    public static String readFile(File file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder ret = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
             // do something with line.
            ret.append(line);
        }
        
        return ret.toString();
    }
}
