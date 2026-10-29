# US001 - Create a Map with a size and a name

## 1. Requirements Engineering

### 1.1. User Story Description

As an Editor, I want to create a map with a size and a name. 

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	 The Map Editor allows you to create rectangular maps and place static elements on the map

**From the client clarifications:**

> **Question:** What are the maximum and minimum possible sizes of a world map?
>
> **Answer:** We are the ones who define them.

> **Question:** Does the name of a map have any restrictions?
>
> **Answer:** File name like restrictions.

> **Question:** Is there a predefined list of sizes, or should users be able to input custom dimensions?
> 
> **Answer:** Custom dimensions but suggesting predefined sizes could be a good idea.

> **Question:** Should map names be unique within the system?
>
> **Answer:** Yes.

> **Question:** Can the names of scenarios and maps be the same?
>
> **Answer:** Yes, they are different objects.

> **Question:** O tamanho do mapa pode ser um número real ou precisa de ser um número inteiro?
>
> **Answer:** The map is a set of cells, similar to a spreadsheet. The coordinates are integers (row and column, or alternatively X and Y)


### 1.3. Acceptance Criteria

*  **AC1**: The maps dimensions are positive integers
*  **AC2**: Map name should be a valid file name.
*  **AC3**The maps should have a scale that states the size of cell map in kms.
*  **AC4**: The name of the map must be unique in the system.
*  **AC5**: The maps should have a scale that states the size of cell map in
   kms

### 1.4. Found out Dependencies

* **N/A**

### 1.5 Input and Output Data

**Input Data:**

* Typed data:

    * name
    * width
    * height
	
* Selected data:
    *Size

**Output Data:**

* validation of the name and size of the map
* (In)Success of the operation
* map

### 1.6. System Sequence Diagram (SSD)

![US001-SSD.svg](svg/US001-SSD.svg)

**__**

### 1.7 Other Relevant Remarks

* 