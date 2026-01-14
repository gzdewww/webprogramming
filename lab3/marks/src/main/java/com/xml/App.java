package com.xml;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Scanner;

public class App {
    // Константа для пути к ресурсам
    private static final String RESOURCES_PATH = "src/main/resources/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("XML Average Grade Corrector");
        System.out.println("Лабораторная работа №3");
        System.out.println("=========================================\n");

        try {
            String inputFileName;

            // Если переданы аргументы командной строки
            if (args.length == 1) {
                inputFileName = args[0];
                System.out.println("Используется файл из аргументов: " + inputFileName);
            } else {
                // Интерактивное меню выбора
                System.out.println("Выберите вариант загрузки XML файла:");
                System.out.println("1. student1.xml (правильная средняя оценка)");
                System.out.println("2. student2.xml (неправильная средняя оценка)");
                System.out.println("3. Ввести имя своего XML файла из папки resources");
                System.out.print("\nВаш выбор (1-3): ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        inputFileName = "student1.xml";
                        break;
                    case "2":
                        inputFileName = "student2.xml";
                        break;
                    case "3":
                        System.out.print("Введите имя XML файла (например: myfile.xml): ");
                        inputFileName = scanner.nextLine().trim();
                        if (!inputFileName.toLowerCase().endsWith(".xml")) {
                            inputFileName += ".xml";
                        }
                        break;
                    default:
                        System.out.println("Неверный выбор. Используется student1.xml по умолчанию.");
                        inputFileName = "student1.xml";
                }
            }

            // Проверка существования файла
            File inputFile = new File(RESOURCES_PATH + inputFileName);
            if (!inputFile.exists()) {
                System.out.println("\n[ОШИБКА] Файл " + inputFileName + " не найден в папке " + RESOURCES_PATH);
                System.out.println("Доступные файлы в " + RESOURCES_PATH + ":");
                File resourcesDir = new File(RESOURCES_PATH);
                if (resourcesDir.exists()) {
                    String[] files = resourcesDir.list((dir, name) -> name.toLowerCase().endsWith(".xml"));
                    if (files != null && files.length > 0) {
                        for (String file : files) {
                            System.out.println("  - " + file);
                        }
                    } else {
                        System.out.println("  (нет XML файлов)");
                    }
                }
                return;
            }

            System.out.println("\n[ФАЙЛ] Обрабатывается файл: " + inputFile.getAbsolutePath());

            // Обработка XML файла
            boolean needsCorrection = processXMLFile(inputFile);

            if (!needsCorrection) {
                System.out.println("\n[УСПЕХ] Данные верны! Файл не требует изменений.");
            }

        } catch (Exception e) {
            System.err.println("\n[ОШИБКА] Ошибка при обработке файла:");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static boolean processXMLFile(File inputFile) throws Exception {
        // Создание DOM парсера с поддержкой DTD
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true); // Включаем проверку по DTD
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Настройка обработчика ошибок DTD
        builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
            @Override
            public void warning(org.xml.sax.SAXParseException e) {
                System.out.println("[DTD Предупреждение] " + e.getMessage());
            }

            @Override
            public void error(org.xml.sax.SAXParseException e) {
                System.out.println("[DTD Ошибка] " + e.getMessage());
            }

            @Override
            public void fatalError(org.xml.sax.SAXParseException e) {
                System.out.println("[DTD Критическая ошибка] " + e.getMessage());
            }
        });

        // Парсинг XML документа
        Document document = builder.parse(inputFile);
        document.getDocumentElement().normalize();

        System.out.println("\n[АНАЛИЗ] Анализ данных студента...");

        // Получение информации о студенте
        Element studentElement = document.getDocumentElement();
        String lastName = studentElement.getAttribute("lastname");
        System.out.println("Студент: " + lastName);

        // Сбор оценок
        NodeList subjectNodes = studentElement.getElementsByTagName("subject");
        System.out.println("Найдено предметов: " + subjectNodes.getLength());

        if (subjectNodes.getLength() == 0) {
            System.out.println("[ВНИМАНИЕ] У студента нет предметов!");
            return false;
        }

        // Вычисление средней оценки
        double sum = 0;
        System.out.println("\nОценки студента:");
        for (int i = 0; i < subjectNodes.getLength(); i++) {
            Element subject = (Element) subjectNodes.item(i);
            String title = subject.getAttribute("title");
            int mark = Integer.parseInt(subject.getAttribute("mark"));
            sum += mark;
            System.out.println("  " + title + ": " + mark);
        }

        double calculatedAverage = sum / subjectNodes.getLength();
        System.out.printf("Вычисленная средняя оценка: %.2f%n", calculatedAverage);

        // Проверка существующей средней оценки
        NodeList averageNodes = studentElement.getElementsByTagName("average");
        boolean needsCorrection = false;

        if (averageNodes.getLength() > 0) {
            // Сравнение с существующей оценкой
            Element averageElement = (Element) averageNodes.item(0);
            String currentAverageStr = averageElement.getTextContent().trim();
            double currentAverage = Double.parseDouble(currentAverageStr);

            System.out.println("Текущая средняя в файле: " + currentAverage);

            // Проверка на необходимость коррекции
            if (Math.abs(currentAverage - calculatedAverage) > 0.01) {
                System.out.println("[ВНИМАНИЕ] Обнаружено несоответствие!");
                needsCorrection = true;

                // Обновление средней оценки
                averageElement.setTextContent(String.format("%.2f", calculatedAverage));
                System.out.println("Средняя оценка скорректирована.");
            } else {
                System.out.println("[УСПЕХ] Средняя оценка верна.");
            }
        } else {
            // Добавление элемента average если отсутствует
            System.out.println("[ИНФО] Элемент 'average' отсутствует, добавляется...");
            Element averageElement = document.createElement("average");
            averageElement.setTextContent(String.format("%.2f", calculatedAverage));
            studentElement.appendChild(averageElement);
            needsCorrection = true;
        }

        // Если требуется коррекция, сохраняем файл
        if (needsCorrection) {
            String inputFileName = inputFile.getName();
            String baseName = inputFileName.substring(0, inputFileName.lastIndexOf('.'));
            String correctedFileName = baseName + "_corrected.xml";
            File outputFile = new File(RESOURCES_PATH + correctedFileName);

            saveDocument(document, outputFile);
            System.out.println("\n[СОХРАНЕНО] Файл сохранен: " + outputFile.getAbsolutePath());
        }

        return needsCorrection;
    }

    private static void saveDocument(Document doc, File outputFile) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Настройка форматирования вывода
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "student.dtd");

        // Сохранение документа
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outputFile);
        transformer.transform(source, result);
    }
}