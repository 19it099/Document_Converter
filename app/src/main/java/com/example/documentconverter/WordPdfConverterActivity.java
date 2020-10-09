package com.example.documentconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class WordPdfConverterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_pdf_converter);

        Intent intent = getIntent();
        String outputPDF = intent.getStringExtra("convertFile");

        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromFile(new File(outputPDF)).load();
    }
}