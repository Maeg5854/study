package org.mandy.tobi.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
//    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader(filepath));
//            return callback.doSomethingWithReader(br);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw e;
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }
//    }
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

    public int calcSum(String path) throws IOException {
        return lineReadTemplate(path, new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value += Integer.valueOf(line);
            }
        }, 0);
    }

    public int calcProduct(String path) throws IOException {
        return lineReadTemplate(path, new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value *= Integer.valueOf(line);
            }
        }, 1);
    }
}
