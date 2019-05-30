package com.sample.iscanreorder;

import com.itextpdf.text.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IScanReorder {

    public static void main(String[] args) throws IOException, DocumentException, URISyntaxException {

        File[] listOfFiles = new File (args[0]).listFiles();
        String folderDest = args[0] +  "/output/";

        int totalFiles = Integer.valueOf(args[1]);

        List<String> fileName = new ArrayList<>();

        // Delete all files from folderDest
        File[] listOfFilesDest = new File(folderDest).listFiles();
        for (File file : listOfFilesDest) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }

        // Rename files and put in folder output
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].isHidden()) {
                File fileDest = new File(folderDest + changeName(listOfFiles[i].getName(), totalFiles));
                try {
                    Files.copy(listOfFiles[i].toPath(), fileDest.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // reorder
        listOfFilesDest = new File(folderDest).listFiles();
        reOrderFiles(listOfFilesDest, fileName);

        // create one single PDF file
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        PDFmerger.setDestinationFileName(folderDest+"output.pdf");
        for (int i = 0; i < fileName.size(); i++) {
            System.out.println(fileName.get(i));
            File sourceFile = new File(folderDest+fileName.get(i));
            PDDocument document = PDDocument.load(sourceFile);
            PDFmerger.addSource(sourceFile);
            document.close();
        }
        PDFmerger.mergeDocuments();


    }

    private static void reOrderFiles(File[] listOfFiles, List<String> fileName) {

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].isHidden()) {
                fileName.add(listOfFiles[i].getName());
            }
        }
        Collections.sort(fileName, new FileNameComparator());

    }

    private static String changeName(String name, int length) {

        if (name != null) {

            int order = Integer.valueOf(name.substring(name.indexOf('_')+1, name.length()-4));

            int newOrder = 0;
            if (order <= length / 2) {
                newOrder = order*2-1;
            } else {
                newOrder = 2*length-2*order+2;
            }

            String suffix = name.substring(name.length()-4);
            name = name.substring(0, name.length()-4) + "_" + String.valueOf(newOrder) + suffix;

        }

        return name;
    }

}
