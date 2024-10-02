package org.mandy.tobi.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Concatenation {
    public String concatenate(String filepath) throws IOException {
        return lineReadTemplate(filepath, new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        }, "");
    }
    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = null;
            T result = initVal;
            while((line = br.readLine()) != null ){
                result = callback.doSomethingWithLine(line, result);
            }

            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
