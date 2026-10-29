package MDISC.domain;

/**
 * Represents types of train, used to filter by electrification in canTravel:
 * <ul>
 *   <li>ELECTRIC – only utilises electrified lines</li>
 *   <li>DIESEL  – may utilise any line</li>
 *   <li>STEAM   – may utilise any line</li>
 * </ul>
 */
public enum TrainType {
    /** Uses only electrified lines. */
    ELECTRIC,

    /** May utilise any line. */
    DIESEL,

    /** May utilise any line. */
    STEAM;
}
