# KisanMadad

[![Download APK](https://img.shields.io/badge/Download-APK-brightgreen)](./app-debug.apk)

**CLICK ON THE LINK ABOVE AND CLICK ON THE SMALL DOWNLOAD ICON ON THE RIGHT CORNER TO DOWNLOAD THE APK FILE**

**KISANMADAD ANDROID APPLICATION WALKTHROUGH VIDEO**  
[![VIDEO](https://img.youtube.com/vi/rcWM6QxNoHY/0.jpg)](https://youtu.be/rcWM6QxNoHY)

**Team ID**: A0237  
**Team Name**: VIT-US

KisanMadad is an innovative platform designed to empower farmers by leveraging **AI**, **IoT**, and **drones**. It provides solutions for direct market access, smart irrigation, crop health monitoring, and educational resources, all with the aim of increasing productivity and profitability for farmers.

---

## Features

- **Direct Market Access**: Enables direct transactions between farmers and buyers, ensuring fair pricing by eliminating intermediaries.
- **AI-Driven Crop Recommendation**: Provides suggestions based on soil and weather data to help farmers choose the best crops for their land.
- **Smart Irrigation**: Uses IoT sensors to monitor soil moisture and weather conditions, optimizing water usage.
- **Crop Health Monitoring**:
  - **Crop Disease Prediction**: Utilizes a CNN model trained on a dataset of 5000 images to classify crop diseases such as 'Corn (maize) – Common Rust' and 'Potato – Early Blight'. The model architecture consists of 30 layers, including convolutional layers, batch normalization, ReLU activation, max-pooling layers, and fully connected layers.
  ![Crop Disease Prediction Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Disease%20Prediction%20Screen.jpg)
  ![Crop Disease Prediction Model](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/CropDiseaseML.png)
  
  - **Crop Health Check**: A CNN model evaluates soil suitability based on Nitrogen (N), Phosphorus (P), and Potassium (K) values, trained on a dataset of 2200 records. 
  ![Crop Health Check Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Crop%20Health%20Check.jpg)
  
- **Smart Nutrient Management**: Provides personalized fertilizer recommendations based on soil conditions.
- **Educational Resources**: Localized farming tutorials integrated via YouTube, offering farmers access to important knowledge. Integrated with Bard AI, farmers can clarify their doubts on the go.
  ![Educational Resources Screen 1](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Educational%20Resources%20screen%201.jpg)
  ![Educational Resources Screen 2](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Educational%20Resources%20screen%202.jpg)
  ![Bard AI Integration](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Bard%20AI%20integration.jpg)

---

## Problem Addressed

KisanMadad addresses the inefficiencies in traditional farming practices by improving resource management, enhancing crop health, and increasing farmer profitability through direct access to markets. The platform reduces reliance on intermediaries and promotes sustainable farming.

---

## Technologies Used

- **Frontend**: XML, Java (Android)
- **Backend**: Firebase API, Flask API, YouTube API, Government Database API
- **AI/ML**: Artificial Neural Networks, ResNet, TensorFlow Lite
- **Database**: Firebase
- **Hardware**:
  - IoT Sensors (soil moisture, weather, and soil nutrient sensors)
  - Drones equipped with cameras for crop monitoring

---

## Architecture

KisanMadad integrates Intel's AI and oneAPI toolkits to create a seamless, high-performance system that runs on Intel® CPUs and GPUs, offering scalable and efficient agricultural solutions.

---

## Implementation

1. **Platform Development**: Modular design using Java and XML, with real-time data integration from sensors and drones.
2. **AI Integration**: Leveraging TensorFlow, PyTorch, Keras, CNN, and OpenCV for disease detection, irrigation scheduling, and market analytics.
3. **Pilot Testing**: Conducted pilot projects to gather feedback and refine the platform.
4. **Scaling**: Plans to partner with agricultural organizations to drive adoption.

---

## Feasibility and Challenges

**Feasibility**: KisanMadad’s integration of AI, IoT, and drones makes it scalable and adaptable to solve key agricultural challenges.

**Challenges**:
- Low digital literacy among farmers.
- High initial costs for IoT sensors and drones.
- Connectivity issues in rural areas.

**Strategies**:
- Training programs for farmers.
- Partnerships and subsidies to reduce costs.
- Offline functionality to mitigate connectivity challenges.

---

## Impact

- **Social**: Promotes fair pricing and market access, reducing economic disparity, and helps farmers adopt modern technology.
- **Economic**: Increases farmer incomes by connecting them directly to buyers and reducing input costs.
- **Environmental**: Supports sustainable farming through optimized resource usage and minimized chemical inputs.

---

## Machine Learning Models

### 1. Fertilizer Prediction Model
The Fertilizer Prediction model employs a Random Forest classifier with a random state set to 42, achieving an impressive accuracy of 97.8%. The model was trained on a dataset comprising 99 records and utilizes the following features: Temperature, Humidity, Moisture, Soil Type, Crop Type, Nitrogen, Potassium, and Phosphorous, to predict the appropriate fertilizer. The target variable, 'Fertilizer Name,' consists of seven unique fertilizers: Urea, DAP, 14-35-14, 28-28, 17-17-17, 20-20, and 10-26-26. This robust model aids in precise fertilizer recommendations for improved crop yield.

### 2. Crop Recommendation System
The Crop Recommendation System is powered by a Convolutional Neural Network (CNN) model with four layers. The architecture includes an input layer that processes seven key agricultural features (N, P, K, temperature, humidity, pH, and rainfall), followed by a hidden layer with 128 neurons using ReLU activation, a second hidden layer with 64 neurons and ReLU activation, and an output layer with softmax activation for classification. The model is trained on a dataset of 2200 records with crops such as rice, maize, chickpea, kidney beans, pigeon peas, and more. After 10 epochs of training, the model achieves an accuracy of 94.09%, providing reliable crop recommendations.

### 3. Smart Irrigation System
The Smart Irrigation System utilizes a RandomForestRegressor with 100 estimators, a minimum samples leaf of 4, and a minimum samples split of 10. Trained on a dataset containing 2880 records, it leverages features such as Crop Type, Soil Type, Region, Temperature, Weather Condition, and Water Requirement to predict optimal irrigation needs. The model achieved a Mean Absolute Error (MAE) of 1.4169, providing precise irrigation recommendations to help optimize water usage in agriculture.

### 4. Soil Health Prediction Model
The Soil Health Prediction model is a Convolutional Neural Network (CNN) with four layers. It starts with an input layer shaped to the dataset's features, followed by two dense layers with 64 and 32 neurons respectively, both using ReLU activation. The output layer employs a sigmoid activation function for binary classification. Trained for 1000 epochs on a dataset containing 2200 records with features such as N, P, K, temperature, humidity, pH, and rainfall, the model achieves a maximum accuracy of 75.11%, providing insights into soil health.

### 5. Crop Disease Prediction Model
The Crop Disease Prediction model is a CNN with 30 layers, consisting of convolutional layers (Conv2D), batch normalization (BatchNorm2D), ReLU activation, and max-pooling layers (MaxPool2D), culminating in a flatten layer and a fully connected (linear) layer. This deep model is trained on a dataset of 5000 images, classifying crop diseases across various categories such as 'Corn (maize) – Common Rust', 'Potato – Early Blight', 'Potato – Late Blight', 'Tomato – Bacterial Spot', 'Tomato – Late Blight', as well as healthy versions of these crops.

---

## Modules Overview

### 1. Login/Signup Module
This module allows users to create an account or log in based on their role (farmer or buyer). Users can select their account type and access the platform accordingly.

![Account Type Selection](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Account%20Type%20Selection.jpg)
![Farmer Signup/ Login](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Farmer%20Signup%20cum%20Login.jpg)
![Buyer Signup/ Login](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Buyer%20Signup%20cum%20Login.jpg)

### 2. Dashboards
The dashboards serve as the central hub for both farmers and buyers, providing access to various features of the app.

![Farmer Dashboard](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Farmer%20Dashboard.jpg)
![Buyer Dashboard](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Buyer%20Dashboard.jpg)

### 3. Crop Recommendation and Health Monitoring
Farmers can input soil data and receive crop recommendations based on current conditions. Additionally, they can check the health of their crops using the Crop Health Check feature.

![Crop Recommendation Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Crop%20Recommendation.jpg)

### 4. Smart Irrigation and Nutrient Management
The app provides data-driven irrigation schedules and fertilizer recommendations, ensuring optimized resource use.

![Irrigation Management Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Smart%20Irrigation%20Screen%201.jpg)

### 5. Crop Disease Prediction
Farmers can upload images of their crops for disease prediction, receiving real-time feedback on crop health.

![Crop Disease Prediction Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Disease%20Prediction%20Screen.jpg)

### 6. Crop Health Check
Based on the NPK values, provides suggestion for the growth of the crop in the soil.

![Crop Health Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Crop%20Health%20Check.jpg)

### 7. Educational Resources
Farmers can access educational content, including videos and tutorials, through the app.

![Educational Resources Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Educational%20Resources%20screen%201.jpg)
![Educational Resources Screen](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Educational%20Resources%20screen%202.jpg)

### Bard AI Integration
![Bard AI Integration](https://github.com/Utkarsh140503/IntelHackathonVITUS/blob/main/KisanMadad%20Screenshots/Bard%20AI%20integration.jpg)

---

## Conclusion

KisanMadad is a comprehensive solution for modern agriculture, addressing challenges faced by farmers and enabling them to thrive in an increasingly competitive environment. By integrating advanced technologies, KisanMadad not only enhances productivity but also fosters a sustainable future for farming.
