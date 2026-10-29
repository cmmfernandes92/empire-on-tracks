# US013 - Validate a route by train type

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to verify if a train of a specific type can go on a specific railway network

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	Each task is characterized by having a unique reference per organization, a designation, an informal and a technical description, an estimated duration and cost, as well as a task category. 

>	As long as it is not published, access to the task is exclusive to the employees of the respective organization. 

**From the client clarifications:**

> **Question:** Regarding trains with different types of fuels (steam, diesel and electricity) that work on certain types of railroad. Diesel and steam trains can work on any type of railroads, while electric trains should work exclusively on electrified railroads? If not, then which types of trains work on which types of railroads?
>
> **Answer:** yes, Diesel and steam trains can work on any type of railroads, while electric trains should work exclusively on electrified railroads.

> **Question:** Will this US be implemented as an automatic validation when the player establishes a train route between two stations? For instance: the player tries to add an electric train on a route that doesn't have electrified railroads, the game should detect that and deny the player's action? If this is incorrect, then this US is a manual verification the player can select?
>
> **Answer:** There is an important difference between route and path, a route is ordered list of stations that should be visited and where the cargos should be dropped and loaded, the path is the sequence of lines to be used to complete a route. So, a route can be satisfied with multiple different paths. If a route is assigned to a train and there is no possible path to accomplish the route, then a message should raised.


### 1.3. Acceptance Criteria

* **AC1:** The player should be able to choose the type of train (steam, diesel or electric) and station type (depot, station or terminal) in real time.
* **AC2:** A visualization of the stations, and the lines connecting stations of this scenario should be displayed to the player, where electrified railway lines are drawn with a different color from the others.
* **AC3:** All implemented procedures (except the used for graphic visualization) must use primitive operations only, and not existing functions in JAVA libraries.
* **AC4:** The algorithm(s) implemented to solve this problem should be documented/detailed in the repository documentation (using markdown format).

### 1.4. Found out Dependencies

* There is a dependency on "US001 - Create a Map" as there must be a map so that you can do all sorts of validations
* There is a dependency on "US005 - Build a Station" as there must be at least two stations so that the player can have trains to be validated.
* There is a dependency on "US007 - List all the stations" 

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * a reference
    * a designation 
    * an informal description
    * a technical description
    * an estimated duration
    * an estimated cost
	
* Selected data:
    * a task category 

**Output Data:**

* List of existing task categories
* (In)Success of the operation

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US013-SSD.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The created task stays in a "not published" state in order to distinguish from "published" tasks.