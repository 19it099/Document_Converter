package com.example.documentconverter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspose.words.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_PICKER = 111;
    String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    String lastsavedpdf=null;
    int loopnumber=0;
    public String convertPDF = storageDir ;
    TextView txtSelectedPath = null;
    private Uri document = null;
    Button btnDocToPdf;
    LinearLayout linearClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearClick = findViewById(R.id.linearClick);
        btnDocToPdf = findViewById(R.id.btnDocToPdf);
        txtSelectedPath = findViewById(R.id.txtSelectedPath);

        linearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shouldAskPermissions()) {
                    askPermissions();
                } else {
                    openaAndConvertFile(null);
                }

                openaAndConvertFile(null);

            }
        });

        btnDocToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPDFFile();


            }
        });

    }

    protected static boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, permissions, 1);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openaAndConvertFile(null);

                }
            }
        }
    }
    private void openaAndConvertFile(Uri pickerInitialUri) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, FILE_PICKER);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                document = intent.getData();
               // open the selected document into an Input stream
                try (InputStream inputStream =
                             getContentResolver().openInputStream(document);) {
                    Document doc = new Document(inputStream);

                 // save DOCX as PDF

                    doc.save(convertPDF+"Convertedpdf("+loopnumber+").pdf");
                    Toast.makeText(MainActivity.this, "File saved in: " + convertPDF, Toast.LENGTH_LONG).show();
                    txtSelectedPath.setText("PDF saved at: " + convertPDF+"Convertedpdf("+loopnumber+").pdf");
                    lastsavedpdf=convertPDF+"Convertedpdf("+loopnumber+").pdf";
loopnumber++;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
        }
         public void viewPDFFile() {
        Intent i= new Intent(this,WordPdfConverterActivity.class);
        i.putExtra("convertFile",lastsavedpdf);
        startActivity(i);
}


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to Exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
