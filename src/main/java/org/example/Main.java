package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

//Задание из тестовго задания:
//На вход в качестве аргументов программы поступают два файла: tests.json и values.json
//• values.json содержит результаты прохождения тестов с уникальными id
//• tests.json содержит структуру для построения отчёта на основе прошедших тестов
//Напишите программу, которая формирует файл report.json с заполненными полями value для структуры tests.json на основании values.json.

public class Main {
    private static final String ID = "id";
    private static final String VALUES = "values";
    private static final String VALUE = "value";
    private static final String TESTS = "tests";

    public static void main(String[] args) {
        String testPath = "tests.json";
        String valuesPath = "values.json";
        //
        try (FileReader testsReader = new FileReader(testPath);
             FileReader valuesReader = new FileReader(valuesPath)){
            JSONParser testsParser = new JSONParser();
            JSONParser valuesParser = new JSONParser();
            JSONObject testsJSONObj = (JSONObject) testsParser.parse(testsReader);
            JSONObject valuesJSON = (JSONObject) valuesParser.parse(valuesReader);
            //парсим данные c tests.json и values.json
            //парсим с tests.json
            JSONArray valuesAr = (JSONArray) valuesJSON.get(VALUES);
            JSONArray testAr = (JSONArray) testsJSONObj.get(TESTS);
            for (Object objValues : valuesAr) {
                JSONObject valuesObj = (JSONObject) objValues;
                long idValues = (long) valuesObj.get(ID);
                String valueValues = (String) valuesObj.get(VALUE);
                //парсим с values.json
                for (Object objTests: testAr) {
                    JSONObject testObjJSON = (JSONObject) objTests;
                    if (idValues==(long) testObjJSON.get(ID)) {
                        testObjJSON.put(VALUE, valueValues);
                        break;
                    }
                    if(testObjJSON.containsKey(VALUES)) {
                        JSONArray testAr2 = (JSONArray) testObjJSON.get(VALUES);
                        for (Object objTests2: testAr2) {
                            JSONObject jsonObj2 = (JSONObject) objTests2;
                            if (idValues==(long) jsonObj2.get(ID)) {
                                jsonObj2.put(VALUE, valueValues);
                                break;
                            }
                            if(jsonObj2.containsKey(VALUES)) {                             // ищем ключ "values"
                                JSONArray testAr3 = (JSONArray) jsonObj2.get(VALUES);
                                for (Object objTests3: testAr3) {
                                    JSONObject jsonObj3 = (JSONObject) objTests3;
                                    if (idValues == (long) jsonObj3.get(ID)) {             //ищем совпадение id
                                        jsonObj3.put(VALUE, valueValues);
                                    }
                                    if(jsonObj3.containsKey(VALUES)) {                     // ищем ключ "values"
                                        JSONArray testAr4 = (JSONArray) jsonObj3.get(VALUES);
                                        for (Object objTests4: testAr4) {
                                            JSONObject jsonObj4 = (JSONObject) objTests4;
                                            if(idValues == (long) jsonObj4.get(ID)) {       //ищем совпадение id
                                                jsonObj4.put(VALUE, valueValues);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            try (FileWriter out = new FileWriter("report.json")){
                out.write(testsJSONObj.toJSONString());
                System.out.println("Содержание файла report: ");
                System.out.println(testsJSONObj);
            }
            } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}