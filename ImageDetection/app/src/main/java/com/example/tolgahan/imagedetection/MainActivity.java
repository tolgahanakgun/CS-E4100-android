package com.example.tolgahan.imagedetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private static boolean barcodeFound = false;
    private static boolean facesFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this.getApplicationContext());
    }

    public void pickPhoto(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.imageView);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int xBounding = imageView.getWidth();//EXPECTED WIDTH
                int yBounding = imageView.getHeight();//EXPECTED HEIGHT

                float xScale = ((float) xBounding) / width;
                float yScale = ((float) yBounding) / height;

                Matrix matrix = new Matrix();
                matrix.postScale(xScale, yScale);

                Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                imageView.setImageBitmap(scaledBitmap);

                detect(scaledBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void detect(Bitmap bitmap){
        barcodeFound = false;
        facesFound = false;
        if(!detectFace(bitmap) && !detectBarcode(bitmap)){
            foundNothing();
        }
    }

    private boolean detectBarcode(Bitmap bitmap){
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_ALL_FORMATS
                )
                .build();

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                if(barcodes.size() > 0) {
                    foundBarcode(barcodes);
                    barcodeFound = true;
                    Log.e("@@@", "Found barcode");
                } else {
                    barcodeFound = false;
                    Log.e("@@@", "Not found barcode");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                barcodeFound = false;
                Log.e("@@@", "Not found barcode");
            }
        });
        return barcodeFound;
    }

    private boolean detectFace(Bitmap bitmap){
        FirebaseVisionFaceDetectorOptions options = new  FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(
                        FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(
                        FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.15f)
                .setTrackingEnabled(false)
                .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {
                if(faces.size() > 0) {
                    foundFaces(faces);
                    facesFound = true;
                    Log.e("@@@", "Found face");
                } else {
                    facesFound = false;
                    Log.e("@@@", "Not found face");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                facesFound = false;
                Log.e("@@@", "Not found face");
            }
        });
        return facesFound;
    }

    private void foundBarcode(List<FirebaseVisionBarcode> barcodes){
        TextView eyes = findViewById(R.id.txtEyes);
        eyes.setText("no");

        TextView smile = findViewById(R.id.txtSmile);
        smile.setText("no");

        TextView barcode = findViewById(R.id.txtBarcode);
        barcode.setText("yes");

        TextView persons = findViewById(R.id.txtNumPeople);
        persons.setText("0");
    }

    private void foundNothing(){
        TextView eyes = findViewById(R.id.txtEyes);
        eyes.setText("no");

        TextView smile = findViewById(R.id.txtSmile);
        smile.setText("no");

        TextView barcode = findViewById(R.id.txtBarcode);
        barcode.setText("no");

        TextView persons = findViewById(R.id.txtNumPeople);
        persons.setText("0");
    }

    private void foundFaces(List<FirebaseVisionFace> faces) {
        float eyesOpenProbability = 0f;
        float smileProbability = 0f;
        int peopleCount = faces.size();
        for (FirebaseVisionFace face : faces) {
            smileProbability += face.getSmilingProbability();
            eyesOpenProbability += (face.getLeftEyeOpenProbability() + face.getRightEyeOpenProbability()) / 2f;
        }
        eyesOpenProbability /= (float) peopleCount;
        smileProbability /= (float) peopleCount;

        TextView persons = findViewById(R.id.txtNumPeople);
        persons.setText(String.valueOf(peopleCount));

        TextView barcode = findViewById(R.id.txtBarcode);
        barcode.setText("no");

        if (smileProbability > 0.5) {
            TextView textView = findViewById(R.id.txtSmile);
            textView.setText("yes");
        } else {
            TextView textView = findViewById(R.id.txtSmile);
            textView.setText("no");
        }
        if (eyesOpenProbability > 0.5) {
            TextView textView = findViewById(R.id.txtEyes);
            textView.setText("yes");
        } else {
            TextView textView = findViewById(R.id.txtEyes);
            textView.setText("no");
        }
    }
}
