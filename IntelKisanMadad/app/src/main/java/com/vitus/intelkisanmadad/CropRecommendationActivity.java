package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class CropRecommendationActivity extends AppCompatActivity {

    private EditText editTextN, editTextP, editTextK, editTextTemperature, editTextRainfall, editTextPh, editTextHumidity;
    private TextView textViewResult;
    private Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_recommendation);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        editTextN = findViewById(R.id.editTextN);
        editTextP = findViewById(R.id.editTextP);
        editTextK = findViewById(R.id.editTextK);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        editTextRainfall = findViewById(R.id.editTextRainfall);
        editTextPh = findViewById(R.id.editTextPh);
        editTextHumidity = findViewById(R.id.editTextHumidity);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonPredict = findViewById(R.id.buttonPredict);

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] crops = {
                        "apple", "banana", "blackgram", "chickpea", "coconut", "coffee",
                        "cotton", "grapes", "jute", "kidneybeans", "lentil", "maize",
                        "mango", "mothbeans", "mungbean", "muskmelon", "orange", "papaya",
                        "pigeonpeas", "pomegranate", "rice", "watermelon"
                };

                float[] inputValues = new float[7];
                inputValues[0] = Float.parseFloat(editTextN.getText().toString());
                inputValues[1] = Float.parseFloat(editTextP.getText().toString());
                inputValues[2] = Float.parseFloat(editTextK.getText().toString());
                inputValues[3] = Float.parseFloat(editTextTemperature.getText().toString());
                inputValues[4] = Float.parseFloat(editTextRainfall.getText().toString());
                inputValues[5] = Float.parseFloat(editTextPh.getText().toString());
                inputValues[6] = Float.parseFloat(editTextHumidity.getText().toString());

                // Adjust output shape to match model's output
                float[][] outputValues = new float[1][22]; // Adjust this based on your model's output

                // Run the model
                tflite.run(inputValues, outputValues);

//                float maxValue = outputValues[0][0];
                // Process and display the result
                StringBuilder result = new StringBuilder("Recommended Crop: ");
                int maxIndex = 0;
                float maxValue = outputValues[0][0];
                for (int i = 1; i < outputValues[0].length; i++) {
                    if (outputValues[0][i] > maxValue) {
                        maxValue = outputValues[0][i];
                        maxIndex = i;
                    }
                }


                result.append(crops[maxIndex]);
//                result.append(outputValues[0][maxValue]);
                textViewResult.setText(result.toString());
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream fis = new FileInputStream(getAssets().openFd("model.tflite").getFileDescriptor());
        FileChannel fileChannel = fis.getChannel();
        long startOffset = getAssets().openFd("model.tflite").getStartOffset();
        long declaredLength = getAssets().openFd("model.tflite").getDeclaredLength();
        return fileChannel.map(MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
