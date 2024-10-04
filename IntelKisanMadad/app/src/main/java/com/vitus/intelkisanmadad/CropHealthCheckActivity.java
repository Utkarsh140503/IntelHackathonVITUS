package com.vitus.intelkisanmadad;

import android.content.res.AssetFileDescriptor;
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
import java.util.Arrays;

public class CropHealthCheckActivity extends AppCompatActivity {

    private Interpreter tflite;
    private EditText editTextCropName, editTextNValue, editTextPValue, editTextKValue;
    private TextView textViewResult;
    private static final int INPUT_SIZE = 3; // Adjust based on your model input

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_health_check);

        editTextCropName = findViewById(R.id.edittext_crop_name);
        editTextNValue = findViewById(R.id.edittext_n_value);
        editTextPValue = findViewById(R.id.edittext_p_value);
        editTextKValue = findViewById(R.id.edittext_k_value);
        textViewResult = findViewById(R.id.textview_result);
        Button buttonPredict = findViewById(R.id.button_predict);

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictNPKValues();
            }
        });
    }

    private void predictNPKValues() {
        float nValue, pValue, kValue;

        try {
            nValue = Float.parseFloat(editTextNValue.getText().toString());
            pValue = Float.parseFloat(editTextPValue.getText().toString());
            kValue = Float.parseFloat(editTextKValue.getText().toString());
        } catch (NumberFormatException e) {
            textViewResult.setText("Invalid input values!");
            return;
        }

        // Prepare input data
        float[][] inputArray = new float[1][INPUT_SIZE];
        inputArray[0][0] = nValue;
        inputArray[0][1] = pValue;
        inputArray[0][2] = kValue;

        // Get the output shape from the model
        int[] outputShape = tflite.getOutputTensor(0).shape(); // Log output shape
        System.out.println("Output shape: " + Arrays.toString(outputShape));

        // Make sure output shape is as expected
        if (outputShape.length == 2 && outputShape[0] > 0 && outputShape[1] == INPUT_SIZE) {
            // Create the output array based on the model's output shape
            float[][] outputArray = new float[outputShape[0]][outputShape[1]];

            // Run inference
            tflite.run(inputArray, outputArray);

            // Process results
            float[] results = outputArray[0];
            String resultText = String.format("Predicted N: %.2f, P: %.2f, K: %.2f", results[0], results[1], results[2]);
            textViewResult.setText(resultText);
        } else {
            textViewResult.setText("Error: Unexpected model output shape.");
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("kisanmadad_soil_health.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
