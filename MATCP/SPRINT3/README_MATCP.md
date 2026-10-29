# README_MATCP

This file contains the location of the code and documentation.

## Code Location

The code for **US30** and **US31** can be found in the Jupyter Notebook file:

- **File Path**: "MATCP/SPRINT3/MATCP_sem2_pi_G031_sprint3.ipynb"
- [US30, US31 CODE](MATCP/SPRINT3/MATCP_sem2_pi_G031_sprint3.ipynb)


## Documentation Links

The documentation for **US30** and **US31** can be accessed through the following links:

- [US30 Documentation](MATCP/SPRINT3/README_US30.md)
- [US31 Documentation](MATCP/SPRINT3/README_US31.md)


## Notes 

## US29 - Objective and Data Collection Methodology

In accordance with the requirements of US29, execution time data was collected specifically to observe the **asymptotic behavior** of the algorithms implemented in **US13** and **US14**. The procedures were executed using multiple given datasets with varying input sizes to analyze how runtime evolves as input scales.
The data collected through US29 will be used in US30.

To measure only the time complexity of the implemented algorithms, the Java program was adapted to isolate and record the **execution time of the algorithm only**, excluding any graphical visualization components (such as those implemented using GraphStream).

The times were recorded and stored in the following csv files:

- [us13_execution_times.csv](MATCP/SPRINT3/files/us13_execution_times.csv) — containing the execution times for the algorithms from US13;
- [us14_execution_times.csv](MATCP/SPRINT3/files/us14_execution_times.csv) — containing the execution times for the algorithms from US14.


Each line in these files follows this format:
- `<size>,<execution_time>`

Where:
- `<size>` is the input size (number of stations or railway lines).
- `<execution_time>` is the time in seconds (using a dot as decimal separator).

## US13 – Connectivity Check

US13 allows verifying if a train of a specific type (steam, diesel, or electric) can travel between two stations in the network.

- **Input Size**: Corresponds to the number of **stations** in the dataset.
- Each dataset was tested under two conditions:
  1. Using **only stations connected by electrified lines** (for electric trains),
  2. Using **all stations** (for steam and diesel trains).


### Example entries from `us13_execution_times.csv` (dataset: `Scenario_50_stations.csv`, `Scenario_50_lines.csv`):
- `30,0.0074753000`
- `50,0.0139957000`

- `30` is the number of stations connected via electrified railway lines.
- `50` is the total number of stations in the dataset.

## US14 – Maintenance Route over Railway Lines

US14 searches for a route that visits each railway line exactly once, to simulate a maintenance operation across the network.

- **Input Size**: Corresponds to the number of **railway lines** in the dataset.
- Each dataset was tested under two conditions:
  1. Considering **only electrified lines**,
  2. Considering **all lines** (electrified and non-electrified).


### Example entries from `us14_execution_times.csv` (dataset: `Scenario_50_stations.csv`, `Scenario_50_lines.csv`):

- `38,0.0045209000`
- `107,0.0090474000`

- `38` is the number of electrified lines in the dataset.
- `107` is the total number of railway lines and corresponds to the number of dataset file lines too.
