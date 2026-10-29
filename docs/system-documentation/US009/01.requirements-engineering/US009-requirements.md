# US009 - Buy a Locomotive

## 1. Requirements Engineering

### 1.1. User Story Description

As a Player, I want to buy a locomotive.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>	Trains are composed of locomotives and carriages. 

>	The locomotives are characterized by several technical aspects, such as power, acceleration, top speed, start year of operation, and acquisition price. 

>	Note that electric locomotives require electrified lines.

>	In the simulator, the player/user acquires trains within the available budget and can put the train into service on a specific route.

**From the client clarifications:**

> **Question:** Is there a limit to how many trains a player can own?
>
> **Answer:** There is no physical limit, just the available memory.

> **Question:** Can a player buy the same train multiple times?
>
> **Answer:** The same type of locomotive, yes; but they will have a unique identification (plate/serial number).

> **Question:** This user story has a dependency to the creation of a list of available locomotives for the scenario and current date. Will that feature be part of a future user story?
>
> **Answer:** The locomotives that will be presented in the selection list are restricted to the current date and any restrictions that may have been defined in the scenario. The presentation of the list of locomotives is part of US09.

> **Question:** How is the availability of locomotives determined?
>
> **Answer:** Each locomotive model has a one-year entry into service period, after which it becomes available for purchase.

> **Question:** Monetary data is expressed in any particular currency?
>
> **Answer:** An abstract one.

> **Question:** What happens if the player does not have enough budget to buy a locomotive?
>
> **Answer:** He can't buy! Bonds and loans will not be considered right now.

> **Question:** What is the price for an electrical, diesel, or steam locomotive?
>
> **Answer:** The price depends on the locomotive model, not in the locomotion type, please refer to game wiki.

> **Question:** Can the player purchase multiple locomotives at once?
>
> **Answer:** Not planned.

> **Question:** Os Train/Locomotivas pertencem ao Player independentemente do cenário (Scenario) em que ele está? Por exemplo, se o jogador mudar de cenário e jogar um novo mapa, os comboios antigos ainda devem continuar associados a ele?
>
> **Answer:** Não, os elementos dinâmicos (estações, linhas, comboios, ...) construidos/adquiridos no contexto de uma simulação só existem no contexto dessa simulação.

### 1.3. Acceptance Criteria

* **AC01:** The player should choose the locomotive from a list of available locomotives for the scenario as well as a current date.

### 1.4. Found out Dependencies

* There is a dependency on **US004 - Create a Scenario** as the scenario determines what locomotives are available.

### 1.5 Input and Output Data

**Input Data:**

* Typed data:
    * a name for the train (composed of the purchased locomotive)
	
* Selected data:
    * a locomotive (from available options in the scenario/current date) 


**Output Data:**

* List of available locomotives
* Cost of the selected locomotive
* (In)Success of the operation

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram.svg](svg/System%20Sequence%20Diagram.svg)

**_Other alternatives might exist._**

### 1.7 Other Relevant Remarks

* The player must have sufficient funds to purchase the locomotive.
