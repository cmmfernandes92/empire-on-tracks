    package pt.ipp.isep.dei.domain;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Objects;
    /**
     * Represents a Station in the railway simulation.
     * <p>
     * A station handles cargo operations such as loading, unloading, storing, and distributing cargo
     * to and from industries within a defined range. Each station has a unique ID, name, location,
     * building upgrades, cargo capacity, and tracking of delivered cargo per year.
     * </p>
     */
    public class Station implements Serializable {
        private final String id;
        private final String name;
        private final Location location;
        private final int range;
        private final int northRange;
        private final int southRange;
        private final int eastRange;
        private final int westRange;
        private final int capacity = 240;
        private final List<Cargo> cargoes;
        private List<Building> buildings;
        private String cityName;

        private final Map<CargoType, Integer> demandByCargo = new HashMap<>();
        private final Map<CargoType, Double> baseValueByCargo = new HashMap<>();
        private final Map<CargoType, Integer> deliveredThisYear = new HashMap<>();
        /**
         * Constructs a Station with a square interaction range.
         *
         * @param id    unique identifier
         * @param name  station name
         * @param x     x-coordinate
         * @param y     y-coordinate
         * @param range interaction range (applied equally in all directions)
         */
        public Station(String id, String name, int x, int y, int range) {
            this.id = Objects.requireNonNull(id);
            this.name = Objects.requireNonNull(name);
            this.location = new Location(x, y);
            this.range = range;
            this.northRange = range;
            this.southRange = range;
            this.eastRange = range;
            this.westRange = range;

            this.cargoes = new ArrayList<>();
            this.buildings = new ArrayList<>();
            for (CargoType type : CargoType.values()) {
                this.cargoes.add(new Cargo(type, 0.0));
                demandByCargo.put(type, 5);
                baseValueByCargo.put(type, 100.0);
                deliveredThisYear.put(type, 0);
            }
        }
        /**
         * Constructs a Station with custom orientation ranges.
         *
         * @param id          unique identifier
         * @param name        station name
         * @param x           x-coordinate
         * @param y           y-coordinate
         * @param station     station type (provides height)
         * @param orientation directional string ("NE", "SE", "SW", "NW")
         */
        public Station(String id, String name, int x, int y, StationType station, String orientation) {
            this.id = Objects.requireNonNull(id);
            this.name = Objects.requireNonNull(name);
            this.location = new Location(x, y);
            this.range = station.getHeight();

            int r = this.range;
            switch (orientation) {
                case "NE": this.northRange = r - 1; this.eastRange = r - 1; this.southRange = r; this.westRange = r; break;
                case "SE": this.southRange = r - 1; this.eastRange = r - 1; this.northRange = r; this.westRange = r; break;
                case "SW": this.southRange = r - 1; this.westRange = r - 1; this.northRange = r; this.eastRange = r; break;
                case "NW": this.northRange = r - 1; this.westRange = r - 1; this.southRange = r; this.eastRange = r; break;
                default:  this.northRange = r;     this.southRange = r;     this.eastRange = r; this.westRange = r; break;
            }

            this.cargoes = new ArrayList<>();
            this.buildings = new ArrayList<>();
            for (CargoType type : CargoType.values()) {
                this.cargoes.add(new Cargo(type, 0.0));
                demandByCargo.put(type, 5);
                baseValueByCargo.put(type, 100.0);
                deliveredThisYear.put(type, 0);
            }
        }
        /**
         * @return the station ID
         */
        public String getId() { return id; }
        /**
         * @return the station name
         */
        public String getStationName() { return name; }
        /**
         * @return the station's map location
         */
        public Location getLocation() { return location; }
        /**
         * Sets the city name associated with the station.
         *
         * @param cityName name of the city
         */
        public void setCityName(String cityName) { this.cityName = cityName; }
        /**
         * @return the name of the city associated with the station (if any)
         */
        public String getCityName() { return cityName; }
        /**
         * @return the buildings installed at the station
         */
        public List<Building> getBuildings() { return buildings; }
        /**
         * Sets the list of buildings at this station.
         *
         * @param buildings list of buildings
         */
        public void setBuildings(List<Building> buildings) { this.buildings = buildings; }
        /**
         * Adds a new building to the station.
         *
         * @param building the building to add
         */
        public void addBuilding(Building building) { this.buildings.add(building); }
        /**
         * @return a copy of the station's cargo list
         */
        public List<Cargo> getCargoes() { return new ArrayList<>(cargoes); }

        /**
         * Attempts to load the specified quantity of cargo of a given type.
         *
         * @param type     the type of cargo to load
         * @param requested quantity requested
         * @return the amount actually loaded
         */
        public double loadCargo(CargoType type, double requested) {
            Objects.requireNonNull(type);
            if (requested <= 0) return 0.0;
            for (Cargo c : cargoes) {
                if (c.getType() == type) {
                    double taken = Math.min(c.getQuantity(), requested);
                    c.removeQuantity(taken);
                    return taken;
                }
            }
            return 0.0;
        }
        /**
         * Unloads a specified quantity of cargo into the station.
         *
         * @param type     type of cargo
         * @param quantity amount to add
         */
        public void unloadCargo(CargoType type, double quantity) {
            Objects.requireNonNull(type);
            if (quantity < 0) throw new IllegalArgumentException();
            for (Cargo c : cargoes) {
                if (c.getType() == type) {
                    c.addQuantity(quantity);
                    return;
                }
            }
            cargoes.add(new Cargo(type, quantity));
        }
        /**
         * Returns the quantity of a given cargo type stored in the station.
         *
         * @param type the cargo type
         * @return the quantity stored
         */
        public double getCargoQuantity(CargoType type) {
            for (Cargo c : cargoes) {
                if (c.getType() == type) return c.getQuantity();
            }
            return 0.0;
        }
        /**
         * @param type the cargo type
         * @return the station's demand level for the cargo
         */
        public int getDemandFor(CargoType type) {
            return demandByCargo.getOrDefault(type, 0);
        }
        /**
         * Sets the demand for a specific cargo type (clamped between 0 and 9).
         *
         * @param type   the cargo type
         * @param demand the new demand value
         */
        public void setDemandFor(CargoType type, int demand) {
            demandByCargo.put(type, Math.max(0, Math.min(9, demand)));
        }
        /**
         * @param type the cargo type
         * @return the base value used for financial calculations
         */
        public double getBaseValueFor(CargoType type) {
            return baseValueByCargo.getOrDefault(type, 0.0);
        }
        /**
         * Sets the base value for a specific cargo type.
         *
         * @param type  the cargo type
         * @param value the value to assign
         */
        public void setBaseValueFor(CargoType type, double value) {
            baseValueByCargo.put(type, value);
        }
        /**
         * Clears all stored cargo quantities (resets to 0).
         */
        public void clearCargoes() {
            cargoes.forEach(c -> c.setQuantity(0.0));
        }
        /**
         * @param type the cargo type
         * @return the amount delivered this year
         */
        public int getDeliveredThisYear(CargoType type) {
            return deliveredThisYear.getOrDefault(type, 0);
        }
        /**
         * Adds a quantity to the delivered counter for a cargo type.
         *
         * @param type the cargo type
         * @param qty  amount delivered
         */
        public void addDeliveredThisYear(CargoType type, int qty) {
            deliveredThisYear.put(type, deliveredThisYear.getOrDefault(type, 0) + qty);
        }
        /**
         * Resets the delivery counter for a cargo type.
         *
         * @param type the cargo type
         */
        public void resetDeliveredThisYear(CargoType type) {
            deliveredThisYear.put(type, 0);
        }
        /**
         * Checks whether the station has an active building of the specified type.
         *
         * @param type the building type
         * @return true if present and active, false otherwise
         */
        public boolean hasBuilding(String type) {
            if (buildings == null) return false;
            return buildings.stream().anyMatch(b -> b.getType().equalsIgnoreCase(type) && b.isActive());
        }
        /**
         * Removes all buildings of a given type from the station.
         *
         * @param type the building type
         */
        public void removeBuilding(String type) {
            if (buildings != null) {
                buildings.removeIf(b -> b.getType().equalsIgnoreCase(type));
            }
        }

        private double totalOfType(CargoType type) {
            return cargoes.stream().filter(c -> c.getType() == type).mapToDouble(Cargo::getQuantity).sum();
        }
        private double capacityOfType(CargoType type) {
            return (double) capacity;
        }
        /**
         * Loads cargo of a specific type from this station into the given carriages.
         *
         * @param ctype     the type of cargo to load
         * @param carriages the carriages to load into
         * @return the total quantity loaded
         */
        public int collectCargoOfType(CargoType ctype, List<Carriage> carriages) {
            for (Cargo c : cargoes) {
                if (c.getType() == ctype && c.getQuantity() > 0) {
                    int available = (int) c.getQuantity();
                    int totalLoaded = 0;
                    for (Carriage carr : carriages) {
                        if (carr.getCargoType() == ctype && carr.getAvailableCapacity() > 0 && available > 0) {
                            int loaded = carr.loadCargo(available);
                            c.removeQuantity(loaded);
                            totalLoaded += loaded;
                            available -= loaded;
                            if (available <= 0) break;
                        }
                    }
                    return totalLoaded;
                }
            }
            return 0;
        }
        /**
         * Collects and distributes cargo to and from nearby industries within the station's range.
         * Also updates industry input stock and station delivery counters.
         *
         * @param industries list of all industries in the simulation
         * @return list of event descriptions for UI/logging
         */
        public List<String> collectAndDistribute(List<Industry> industries) {
            List<String> events = new ArrayList<>();
            for (Industry industry : industries) {
                Location indLoc = industry.getLocation();
                int dx = indLoc.getX() - location.getX();
                int dy = indLoc.getY() - location.getY();
                if (dx < -westRange || dx > eastRange || dy < -southRange || dy > northRange) continue;

                Cargo produced = industry.getCargo();
                if (produced != null && produced.getQuantity() > 0) {
                    double spaceLeft = capacityOfType(produced.getType()) - totalOfType(produced.getType());
                    if (spaceLeft > 0) {
                        double toCollect = Math.min(produced.getQuantity(), spaceLeft);
                        for (Cargo c : cargoes) {
                            if (c.getType() == produced.getType()) {
                                c.addQuantity(toCollect);
                                break;
                            }
                        }
                        industry.consumeCargo(toCollect);
                        events.add(String.format("Station %s collected %.2f of %s from %s", name, toCollect, produced.getType(), industry.getIndustryName()));
                    }
                }

                if (industry instanceof TransformingIndustry ti) {
                    CargoType type = ti.getInputCargoType();
                    double needed = ti.getDemand() - ti.getInputCargoQuantity();
                    if (needed > 0) {
                        double avail = getCargoQuantity(type);
                        double give = Math.min(needed, avail);
                        if (give > 0) {
                            unloadCargo(type, give);
                            ti.setInputCargoQuantity(ti.getInputCargoQuantity() + give);
                            addDeliveredThisYear(type, (int) give);
                            events.add(String.format("Station %s delivered %.2f of %s to %s", name, give, type, ti.getIndustryName()));
                        }
                    }
                } else if (industry instanceof MixedIndustry mi) {
                    CargoType type = mi.getInputCargoType();
                    double needed = mi.getDemand() - mi.getInputCargoQuantity();
                    if (needed > 0) {
                        double avail = getCargoQuantity(type);
                        double give = Math.min(needed, avail);
                        if (give > 0) {
                            unloadCargo(type, give);
                            mi.setInputCargoQuantity(mi.getInputCargoQuantity() + give);
                            addDeliveredThisYear(type, (int) give);
                            events.add(String.format("Station %s delivered %.2f of %s to %s", name, give, type, mi.getIndustryName()));
                        }
                    }
                } else if (industry instanceof City city) {
                    for (CargoType type : CargoType.values()) {
                        double available = getCargoQuantity(type);
                        if (available <= 0) continue;
                        double toDeliver = Math.min(available, 0.5 * city.getHouseBlockCount());
                        if (toDeliver > 0) {
                            unloadCargo(type, toDeliver);
                            city.receiveCargo(type, toDeliver);
                            addDeliveredThisYear(type, (int) toDeliver);
                            events.add(String.format("Station %s delivered %.2f of %s to city %s", name, toDeliver, type, city.getIndustryName()));
                        }
                    }
                }
            }
            return events;
        }
        /**
         * @return string representation of the station
         */

        @Override
        public String toString() {
            String base = name + " @ " + location + " [cargoes=" + cargoes.size() + "/" + capacity + "]";
            return cityName != null ? base + " (city: " + cityName + ")" : base;
        }
    }
