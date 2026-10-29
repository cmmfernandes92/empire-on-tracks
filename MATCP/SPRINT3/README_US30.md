# US30

## Objective

The goal is to obtain the best-fitting curve for the execution time data collected in **US29** and visualize it graphically. The main objective is to fit a mathematical model that describes the relationship between the input size and execution time for the algorithms implemented in **US13** and **US14**. This analysis helps assess the asymptotic behavior of these algorithms and determine which model best fits the observed data.

## Theoretical Background

### 1. Theoretical Models

In the context of **US13** and **US14**, the analysis aims to understand the time complexity of the algorithms based on the execution time data. The three most common time complexities that are often evaluated are:

#### Linear time complexity \(O(n)\):

This model assumes that the execution time increases **proportionally** with the input size. It is represented by the following equation:

$$
T(n) = a \cdot n + b
$$

Where:
- $(T(n))$ is the execution time for an input of size \(n\),
- $(a)$ is a constant that represents the rate of increase of the execution time with the input size,
- $(b)$ is a constant that represents the base execution time (when the input size is zero).

#### Quadratic time complexity \(O(n²)\):

This model assumes that the execution time increases **quadratically** with the input size, which is typical for algorithms that involve nested loops or more complex operations. The equation is represented as:

$$
T(n) = a \cdot n^2 + b \cdot n + c
$$

Where:
- $(T(n))$ is the execution time for an input of size \(n\),
- $(a)$, $(b)$, and $(c)$ are constants to be determined during the fitting process.

#### Log-linear time complexity \(O(n log n)\):

This model assumes that the execution time increases **logarithmically** with the input size, multiplied by the size itself. This is typically observed in algorithms that divide the problem at each step (e.g., sorting algorithms like Merge Sort or Quick Sort). The equation is represented as:

$$
T(n) = a \cdot n \cdot \log_2(n) + b
$$

Where:
- $(T(n))$ is the execution time for an input of size \(n\),
- $(a)$ is a constant representing the scaling factor of the execution time,
- $(b)$ is a constant representing the base execution time,
- \($\log_2(n)$\) is the logarithmic term based on the base 2, representing the logarithmic growth in the time complexity.

 ##### Each model describes different growth behaviors and is chosen based on the algorithm's structure (e.g., loops, recursion, division steps).
### 2. Coefficient of Determination (R²)

To assess the quality of the fitted models (whether linear, quadratic, or log-linear), the **coefficient of determination (R²)** is used. This value ranges from 0 to 1, where:
- \( R² = 1 \) indicates a perfect fit,
- \( R² = 0 \) indicates that the model does not explain the variance in the data.

The formula for \( R² \) is:

$$
R^2 = 1 - \frac{SS_{\text{res}}}{SS_{\text{tot}}}
$$

Where:
- ($SS_{\text{res}}$) is the sum of squared residuals (the difference between the observed and predicted values):

$$
SS_{\text{res}} = \sum (y_{\text{observed}} - y_{\text{predicted}})^2
$$

- ($SS_{\text{tot}}$) is the total sum of squares (the variance of the observed values):

$$
SS_{\text{tot}} = \sum (y_{\text{observed}} - \bar{y})^2
$$

Where ($\bar{y}$) is the mean of the observed values.

#### Acceptable Values for R²

When evaluating ( R²), the following general guidelines for interpreting the values are commonly used:

- \( R² = 1 \): Perfect fit – the model explains all the variance in the data.
- \( 0.9 < R² < 1 \): Very good fit – the model explains almost all the variance.
- \( 0.7 < R² < 0.9 \): Good fit – the model explains a substantial portion of the variance.
- \( 0.5 < R² < 0.7 \): Fair fit – the model explains a moderate portion of the variance.
- \( R² < 0.5 \): Poor fit – the model explains less than half of the variance.

In the context of **US13** and **US14**, an \( R² \) value greater than 0.7 is typically considered a good fit for the model to be useful in explaining the relationship between input size and execution time.

### 3. Residuals and Model Evaluation

The **residuals** are the differences between the observed values and the predicted values from the fitted model:

$$
\text{Residuals} = y_{\text{observed}} - y_{\text{predicted}}
$$

These residuals are used to evaluate the goodness of fit. Ideally, the residuals should exhibit no systematic patterns, meaning the model has accounted for the structure of the data. Common ways to evaluate residuals include:

- **Residual Plot**: A scatter plot of residuals vs. predicted values. Ideally, the residuals should be randomly scattered around zero, indicating that the model has captured all the systematic variance.

- **Normality of Residuals**: A histogram or Q-Q plot can be used to check if the residuals follow a normal distribution. For linear regression, residuals should ideally be normally distributed.

- **Homogeneity of Variance**: The spread of residuals should be consistent across all levels of the independent variable (i.e., the variance should be constant). If the residuals fan out or contract as the input size increases, this could indicate issues with the model.

#### Interpreting Residuals

- **Small residuals**: Indicate a good fit, meaning the predicted values are close to the observed data.
- **Large residuals**: Indicate that the model did not fit well for those particular data points, which might be outliers or cases where the model assumptions were violated.

---
### 3. Data Sorting

The sorting was particularly necessary because the input sizes, due to the nature of the dataset (e.g., different numbers of stations connected by electrified lines versus all stations), were not in a straightforward ascending order. Sorting the data in this way ensures that the x-axis, representing the input size, progresses from smaller to larger values. This helps avoid distortions in the graph and ensures that the curve fitting process follows the correct sequence of data points, accurately reflecting the relationship between input size and execution time.

---
# **Results and Analysis**

## **US13 - Graph Analysis**

For **US13**, both **linear** and **quadratic** models were fitted to the execution time data. The **R² values** for the two models are:

- **Linear fit R² = 0.7742**: This indicates that the linear model explains **77.42%** of the variance, which suggests that a linear relationship does not fully capture the execution time behavior.
- **Quadratic fit R² = 0.9893**: The quadratic model explains **98.93%** of the variance, which is much better and indicates that the execution time increases quadratically with input size.

### **Equations obtained**:

- **Linear fit**:
    $$ y = 0.00624n - 2.03213 $$

- **Quadratic fit**: 
  $$ y = 0.00000794n^2 - 0.0051n + 0.6733 $$

### **Critical Analysis of the Equations**:

- **Linear Fit**: 
  - The equation suggests that for small values of $(n)$, the execution time increases at a rate of approximately 0.00624 units per station. However, the negative intercept of $-2.03213$ indicates that the model starts with a negative time, which doesn't align with the reality of execution time, since it should never be negative. This suggests that while the linear model fits the data to a degree, it fails to capture the full behavior of the algorithm, especially for larger values of $(n)$.
  
- **Quadratic Fit**: 
  - The equation reflects a more complex relationship between execution time and input size. The coefficient of the $(n^2)$ term $(0.00000794)$ is very small, meaning the quadratic growth is slow, but still noticeable as $(n)$ increases. This model accounts for the observed acceleration in execution time, and the positive constant $(0.6733)$ aligns with the observed baseline time for small datasets. The quadratic model explains the data far better than the linear one, as evidenced by the much higher $( R^2)$ value.

### **Coefficient Analysis**:
- The coefficient $(a)$ for the $(n²)$ term in the quadratic model is dominant, confirming that the execution time increases much more rapidly as the number of stations grows.

### Was this expected?

- Yes. Given the structure of the algorithm in **US13**, which involves nested iterations over stations, quadratic behavior was expected. The fitting results confirm this intuition and provide quantitative support.

### Project Implications

 - This has significant consequences for scalability. As the number of stations increases, execution time increases dramatically, which limits the algorithm's usability for large datasets. Optimizing this algorithm should be a priority to handle larger railway networks efficiently.

---

## **US14 - Graph Analysis**

For **US14**, only the **linear** model was fitted to the execution time data. The **R² value** for the linear fit was:

- **Linear fit R² = 0.9376**: This indicates that the linear model explains **93.76%** of the variance, meaning that the linear model provides a good fit and represents the data well.

### **Equations obtained**:

- **Linear fit**: 
  $$ y = 0.00010n + 0.00371 $$

### **Critical Analysis of the Equation**:

- The equation indicates that the execution time increases steadily at a rate of approximately $0.00010$ units per station. The positive intercept of 0.00371 suggests that even with a minimal input size, the algorithm still has a small baseline execution time. 
### **Interpretation**:
- **Linear Model**: The **R² = 0.9376** for the linear fit suggests that the execution time scales linearly with input size, indicating a time complexity of **O(n)**.

### **Coefficient Analysis**:
- The positive coefficient for the linear term suggests that execution time increases steadily as input size grows, confirming that the algorithm behaves with linear scalability.

### Project Implications

 - This is a very favorable outcome for the project. The algorithm scales well and remains practical even for large inputs, making it suitable for bigger scenarios involving a big number of railway lines.
---

## **Final Comments**

- **For US13**, the quadratic time complexity means that as the number of stations increases, the algorithm will experience a significant performance decline. Optimization efforts should focus on reducing the quadratic growth of execution time.
- **For US14**, the linear time complexity implies that the algorithm scales well with larger datasets.

Understanding these time complexities helps plan for future system scalability, allowing for informed decisions regarding optimizations and handling larger datasets in both **US13** and **US14**.
