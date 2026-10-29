package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Cargo;

import java.util.Objects;

public class CargoRepository {
    public void addAvailableCargo(Cargo cargo) {
        Objects.requireNonNull(cargo, "cargo must not be null");
        // persist or cache the cargo type
    }
}

