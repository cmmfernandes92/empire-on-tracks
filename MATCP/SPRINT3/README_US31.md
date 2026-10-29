# US31

# Objective

The goal is to identify the cargo type that has the highest correlation with revenue at a specific station and year. Once the cargo with the highest correlation is identified, a linear regression model is applied to predict future revenue based on changes in cargo volume. The model also calculates the standard error and provides a 95% confidence interval for the predicted revenue.


## **Theoretical Background**

### **Correlation Analysis**

To determine the cargo type most strongly associated with revenue, we use the **Pearson correlation coefficient**. The formula for the correlation is:

$$
r = \frac{\sum (X_i - \bar{X})(Y_i - \bar{Y})}{\sqrt{\sum (X_i - \bar{X})^2 \sum (Y_i - \bar{Y})^2}}
$$

Where:
- $X_i$ represents cargo data,
- $Y_i$ represents revenue data,
- $\bar{X}$ and $\bar{Y}$ are the means of the cargo and revenue datasets, respectively.

---

**Pearson correlation** is commonly used to measure the strength and direction of a **linear relationship** between two continuous variables. It is most effective when the data follows a **normal distribution** and when the relationship is **homoscedastic** (i.e., the spread of data points remains consistent across values).

#### Advantages:
- **Easy interpretation**: Values range from -1 to 1, indicating strength and direction of the relationship.
- **Simple calculation**: Straightforward mathematical formula.
- **Compatibility**: Well-suited for linear regression models.

#### Limitations:
- **Linear assumption**: It assumes a linear relationship, so it may miss **nonlinear patterns**.
- **Sensitive to outliers**: Extreme values can skew the results.
- **Normality and variance**: Assumes data is normally distributed and has equal variance, which may not always hold.

---

### **Alternatives to Pearson Correlation**

If the assumptions of Pearson correlation are violated, other methods may provide more robust insights:

- **Spearman’s rank correlation**:
  - Measures **monotonic relationships** (whether one variable consistently increases or decreases with the other).
  - More robust to outliers and can capture **nonlinear trends** (e.g., exponential or logarithmic growth).

- **Kendall’s tau**:
  - Particularly useful with **small datasets** and **many tied values**.
  - Based on the concordance between pairs of data points, making it more robust to noisy data.

- **Mutual Information**:
  - A **non-parametric** method that detects both **linear and nonlinear dependencies**.
  - Does not assume a specific distribution, making it ideal for more complex data patterns.

##### For this analysis, **Pearson correlation** was appropriate because we were focused on identifying a **linear relationship** between cargo volumes and revenue, aligning well with the assumptions of **linear regression**. However, if the data had been more complex or noisy, methods like **Spearman** or **Kendall** could have provided additional insights and potentially uncovered **nonlinear relationships** that Pearson might have missed.
    
---


### **Linear Regression Model**
Once the cargo with the highest correlation is determined, a linear regression model is applied to establish a relationship between the cargo and revenue. The linear regression equation is:

$$
Y = \beta_0 + \beta_1 X
$$

Where:
- $(Y)$ is the revenue,
- $(X)$ is the cargo value,
- $(beta_0)$ is the intercept,
- $(beta_1)$ is the slope (indicating how cargo affects revenue).

The model coefficients $(beta_0)$ and $(beta_1)$ are estimated using the **least squares method**:

$$
\hat{\beta}_1 = \frac{S_{xy}}{S_{xx}}
$$

$$
\hat{\beta}_0 = \bar{y} - \hat{\beta}_1 \bar{x}
$$

Where:
- $(S_{xy})$ is the covariance of $(X)$ and $(Y)$,
- $(S_{xx})$ is the variance of $(X)$,
- $(\bar{x})$ and $(\bar{y})$ are the sample means of the independent and dependent variables.

### **Confidence Interval for Revenue Prediction**
After calculating the regression line, we predict the revenue for January of the next year, assuming a 10% increase in cargo. The **95% confidence interval** for the predicted revenue is calculated using the formula:

$$
\hat{y} \pm t_{\frac{\alpha}{2}, n-2} \cdot s \cdot \sqrt{\frac{1}{n} + \frac{(x_p - \bar{x})^2}{S_{xx}}}
$$

Where:
- $(\hat{y})$ is the predicted revenue,
- $(t_{\frac{\alpha}{2}, n-2})$ is the critical value from the **t-distribution**,
- $(s)$ is the standard error of the residuals,
- $(x_p)$ is the projected cargo value for the following year.

---
### **Prediction Interval**
The **prediction interval** is used to estimate the range of an individual future revenue observation, and it is calculated as:

$$
\hat{y} \pm t_{\frac{\alpha}{2}, n-2} \cdot s \cdot \sqrt{1 + \frac{1}{n} + \frac{(x_0 - \bar{x})^2}{S_{xx}}}
$$

Where:
- $(\hat{y})$ is the predicted revenue for a new observation,
- $( x_0)$ is the specific value of cargo for which the prediction is made.

##### 
- This is **always wider** than the confidence interval for the mean because it accounts for individual variability.
---

## Analysis and Interpretation of Results

### Selected Cargo and Model Fitting

For the station **Stuttgart** in the year **2016**, the cargo that showed the highest correlation with revenue was **Prata**. The Pearson correlation coefficient between **Prata** and revenue was **0.6252**, indicating a moderate positive linear relationship. This suggests that fluctuations in the quantity of Prata cargo have a noticeable impact on the revenue generated by the station.

A simple linear regression model was then fitted, with **Prata** as the independent variable and **Revenue** as the dependent variable. The estimated regression equation is:

$$
\hat{y} = 801.2883 + 0.5790 \cdot \text{Prata}
$$

Where:
- $\hat{y}$ is the predicted revenue,
- Prata represents the amount of Prata cargo at the station.

The residual variance $s^2$ was calculated as:

$$
s^2 = 16780.39
$$

This variance quantifies the degree of spread or dispersion of the actual revenue data around the predicted regression line. A higher residual variance suggests that there is more unpredictability or noise in the data, even after fitting the model.

### Revenue Forecast for a 10% Cargo Increase

For the January 2016 dataset, the recorded value for Prata was **404.37** tons. Assuming that the amount of Prata increases by **10%** in January of the following year, the expected new value for Prata would be:

$$
\text{New Prata value} = 404.37 \times 1.10 = 444.81 \text{ tons}
$$

Using the regression model, the predicted revenue for this new Prata value is:

$$
\hat{y}_0 = 801.2883 + 0.5790 \times 444.81 = 1058.85 \text{ Millions (€)}
$$

The 95% confidence interval for the predicted revenue is:

$$
[734.38, 1383.33] \text{ Millions (€)}
$$

This interval means that, based on the model, there is a **95% probability** that the actual revenue, given a 10% increase in Prata, will fall between **734.38** and **1383.33**.


### Interpretation of Results

The regression model indicates that a **10% increase** in the amount of Prata cargo in January 2017 would result in an expected revenue of **1058.85** Millions (€). The confidence interval for this prediction is **[734.38, 1383.33]**, meaning that future revenues will likely fall within this range with **95% confidence**.

## Critical Reflection

#### Is the Correlation Value Good?

- A correlation of **0.6252** is **moderate**, indicating a reasonable but **not highly strong** relationship.
- There is room for improvement — other cargo types, external factors, or seasonality may also influence revenue.

#### Interpretation of Coefficients

- Each **additional ton of Prata** corresponds to an average **increase of €579,000** in revenue.
- The intercept of €801.29M can be seen as baseline revenue independent of Prata, but its **interpretability depends** on the data domain.

#### Width of the Confidence Interval

- The **range is wide** (~649 million €), which reduces forecasting precision.
- This likely reflects **high variance** in the data and suggests that the model, while capturing a trend, still leaves room for significant uncertainty.

- This has a **clear impact for the player**. In a game where decisions depend on expected revenue, having an uncertainty of **hundreds of millions of euros** can affect choices like which cargo to prioritize or whether to invest. The model shows useful trends, but the wide interval means it should be used **with caution**, not as an exact prediction.

> #### What happens if we change the confidence level?
> - **Increasing the confidence level (e.g., to 99%)** would make the interval **even wider**. The prediction becomes more reliable (i.e., we are more confident it contains the true value), but also **less precise**, which may make decisions harder.
> - **Decreasing the confidence level (e.g., to 90%)** would **narrow the interval**, offering a more precise estimate — but with less certainty. This means there’s a higher chance the true revenue could fall outside the predicted range.

In practical terms:
- **Higher confidence** → **wider range** → safer prediction, but more vague
- **Lower confidence** → **narrower range** → sharper prediction, but riskier

---

## Project Context and Implications

These results are useful for the player during the simulation:

- **Helps the player prioritize cargo**: Since Prata has the highest impact on revenue in Stuttgart for 2016, the player can focus on increasing or managing this cargo to improve results. This helps in choosing the best cargo type to invest in.

- **Supports scenario analysis**: The simulation allows players to run scenarios, like increasing cargo by 10% in a certain industry. This analysis shows exactly what revenue change to expect from that increase, making the scenario more meaningful.

- **Useful for planning and decision-making**: With this model, the player can make smarter decisions by predicting how revenue will change with cargo. This is important for planning actions in the game, especially when managing station and industries locations and cargotypes.

In summary, this approach gives the player clear insights on how cargo affects revenue, making the game more strategic and realistic.
