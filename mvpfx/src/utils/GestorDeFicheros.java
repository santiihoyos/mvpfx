/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Santi Hoyos
 */
public class GestorDeFicheros {

    /**
     * Constructor ocultado, apra singleton
     */
    public GestorDeFicheros() {
    }

    /**
     * Dada una ruta y el texto como String guarda a fichero el contendo, Se
     * marca como sincronizado puesto que podria estarse dando el caso que se
     * solape la escritura en concurrencia, si el problema persiste y se nota
     * una caida de rendimiento se quitara el patron singleton a esta clase.
     *
     * @param ruta
     * @param texto
     * @throws java.io.IOException
     */
    public void guardarToFichero(String ruta, String texto) throws IOException {

        File f = FileUtils.getFile(ruta);

        if (!f.exists()) {
            FileUtils.touch(f);
        }

        FileUtils.writeStringToFile(f, texto, "UTF-8");
    }

    /**
     * Cara un fichero a string en codificación UTF-8
     *
     * @param ruta
     * @return
     * @throws IOException
     */
    public synchronized String cargarFicheroToString(String ruta) throws IOException {

        File file = FileUtils.getFile(ruta);

        if (file != null) {
            return FileUtils.readFileToString(file, "UTF-8");
        } else {
            FileUtils.touch(new File(ruta));
            return "";
        }
    }

    /**
     * Dado un resulset lo transforma a un fichero con formato formato CSV.
     *
     * @param rs resulset que se desea transformar
     * @param rutaCsv ruta donde se desea dejar el fichero.
     * @throws SQLException
     * @throws FileNotFoundException fichero no encontrado
     */
    public void resulsetToCsv(ResultSet rs, String rutaCsv) throws SQLException, FileNotFoundException, IOException {

        /* Separador de linea de Windows, puesto qeu en base de datos alguien
         * se le olvido no meter saltos de linea en los registros de la abse de datos */
        String separadorDeLinea = new StringBuilder().append((char) 13).append((char) 10).toString();

        ResultSetMetaData meta = rs.getMetaData();
        int numberOfColumns = meta.getColumnCount();
        String dataHeaders = "\"" + meta.getColumnName(1) + "\"";
        StringBuilder csvWriter = new StringBuilder();

        for (int i = 2; i < numberOfColumns + 1; i++) {
            dataHeaders += ";\"" + meta.getColumnName(i) + "\"";
        }

        csvWriter.append(dataHeaders).append("\n");

        while (rs.next()) {
            String value1 = rs.getString(1);
            String row = "\"" + ((value1 != null) ? value1.replace(";", "").replace(separadorDeLinea, " ") : value1) + "\"";
            for (int i = 2; i < numberOfColumns + 1; i++) {
                String value2 = rs.getString(i);
                row += ";\"" + ((value2 != null) ? value2.replace(";", "").replace(separadorDeLinea, " ") : value2) + "\"";
            }
            csvWriter.append(row).append("\n");
        }

        guardarToFichero(rutaCsv, csvWriter.toString());
    }

    /**
     * Carga desde una ruta con el fichero CSV a un array de Strings de 2
     * dimensiones el CSV dado.
     *
     * @param rutaCsv ruta con el fichero CSV
     * @return Array de 2 dimensiones String[filas][columnas]
     * @throws java.io.FileNotFoundException fichero no encontrado.
     */
    public String[][] csvToString(String rutaCsv) throws FileNotFoundException, IOException {

        String[][] respu;
        ArrayList<String[]> filas = new ArrayList<>();
        FileReader fileReader = new FileReader(rutaCsv);
        CsvReader lector = new CsvReader(fileReader, ';');

        lector.readHeaders();
        filas.add(lector.getHeaders());

        while (lector.readRecord()) {
            filas.add(lector.getValues());
        }

        respu = new String[filas.size()][lector.getHeaderCount()];

        for (int i = 0; i < respu.length; i++) {

            String[] fila = respu[i];

            for (int j = 0; j < fila.length; j++) {
                fila[j] = filas.get(i)[j];
            }
        }

        return respu;
    }
}
