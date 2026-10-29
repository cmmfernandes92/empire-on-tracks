package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.LocomotiveCatalog;

/**
 * Central access point for all repositories and shared data sources in the application.
 * <p>
 * Implements the Singleton pattern to ensure only one instance exists.
 * Provides access to:
 * <ul>
 *   <li>{@link MapRepository} – stores and manages maps</li>
 *   <li>{@link IndustryRepository} – manages industry definitions</li>
 *   <li>{@link LocomotiveCatalog} – holds available locomotives</li>
 *   <li>{@link ScenarioRepository} – holds defined scenarios</li>
 *   <li>{@link SizeRepository} – stores predefined map sizes</li>
 * </ul>
 */
public class Repositories {

    private static Repositories instance;

    private final MapRepository repositoryMap;
    private final IndustryRepository industryRepository;
    private final LocomotiveCatalog locomotiveCatalog;
    private final ScenarioRepository scenarioRepository;
    private final SizeRepository sizeRepository;

    /**
     * Private constructor to prevent external instantiation.
     * Initializes all internal repositories and catalogs.
     */
    private Repositories() {
        this.repositoryMap = new MapRepository();
        this.industryRepository = IndustryRepository.getInstance();
        this.locomotiveCatalog = new LocomotiveCatalog();
        this.scenarioRepository = new ScenarioRepository();
        this.sizeRepository = new SizeRepository();
    }

    /**
     * Returns the singleton instance of {@code Repositories}, creating it if necessary.
     *
     * @return the singleton instance
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                if (instance == null) {
                    instance = new Repositories();
                }
            }
        }
        return instance;
    }

    /**
     * @return the repository that manages all map instances
     */
    public MapRepository getMapRepository() {
        return repositoryMap;
    }

    /**
     * @return the repository that manages all industry definitions
     */
    public IndustryRepository getIndustryRepository() {
        return industryRepository;
    }

    /**
     * @return the catalog that stores all available locomotives
     */
    public LocomotiveCatalog getLocomotiveCatalog() {
        return locomotiveCatalog;
    }

    /**
     * @return the repository that manages scenario definitions
     */
    public ScenarioRepository getScenarioRepository() {
        return scenarioRepository;
    }

    /**
     * @return the repository that stores predefined map sizes
     */
    public SizeRepository getSizeRepository() {
        return sizeRepository;
    }
}
