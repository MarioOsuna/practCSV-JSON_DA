/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pact_csv_json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jdk.nashorn.internal.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AccesoBD {

    String consultarPorCSV() {
        String csv = "";
        String script = "http://www.donantescordoba.org/online/crtsCordoba-colectas.csv";

        URLConnection conexion = null;
        try {
            conexion = new URL(script).openConnection();

            InputStream inputStream = conexion.getInputStream();

            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String linea = "";

            while ((linea = bf.readLine()) != null) {
                csv += linea + "\n";
            }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return csv;

    }

    void consultaPorJSON(DefaultTableModel modelo) {
        String contenido ="";

        String consulta = "http://www.donantescordoba.org/online/crtsCordoba-colectas.json";
        try {

            URLConnection conexion = null;

            conexion = new URL(consulta).openConnection();
            conexion.connect();
            InputStream inputStream = conexion.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String linea = "";

            while ((linea = br.readLine()) != null) {
                contenido += linea;

            }
            br.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(contenido).getAsJsonArray();

        boolean primero = true;
        String[] fila = new String[9];

        for (JsonElement elemento : jsonArray) {
            JsonObject objeto = elemento.getAsJsonObject();

            if (primero) {
                fila = new String[objeto.size() / 2];
            }
            Set<Map.Entry<String, JsonElement>> entrySet = objeto.entrySet();

            int contador = 0;
            int columna = 0;

            for (Map.Entry<String, JsonElement> entry : entrySet) {
                if (contador % 2 != 0) {
                    if (primero) {
                        modelo.addColumn(entry.getKey());
                    }
                    fila[columna] = entry.getValue().getAsString();
                    columna++;
                    entry.getValue();
                }
                contador++;
            }
            modelo.addRow(fila);
            primero = false;
        }

        /* modelo.addColumn("ID");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("HABITANTES");
        modelo.addColumn("PAIS");*/
 /*for (JsonElement elemento : jsonArray) {
            JsonObject objeto = elemento.getAsJsonObject();

            fila[0] = objeto.get("id").getAsString();
            fila[1] = objeto.get("nombre").getAsString();
            fila[2] = objeto.get("habitantes").getAsString();
            fila[3] = objeto.get("pais").getAsString();

            modelo.addRow(fila);
        }*/
    }

}
