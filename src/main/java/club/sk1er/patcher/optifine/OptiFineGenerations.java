package club.sk1er.patcher.optifine;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class OptiFineGenerations {
    public enum Generation {
        I,
        L,
        M,
        G,
        FUTURE,
        UNKNOWN,
    }

    @SerializedName("iGeneration")
    private final Set<String> iGeneration = new HashSet<>(1);

    @SerializedName("lGeneration")
    private final Set<String> lGeneration = new HashSet<>(2);

    @SerializedName("mGeneration")
    private final Set<String> mGeneration = new HashSet<>();

    // 1.12
    @SerializedName("gGeneration")
    private final Set<String> gGeneration = new HashSet<>();

    @SerializedName("futureGeneration")
    private final Set<String> futureGeneration = new HashSet<>();

    public OptiFineGenerations() {
        iGeneration.add("I7");

        lGeneration.add("L5");
        lGeneration.add("L6");

        mGeneration.add("M5");
        mGeneration.add("M6_pre1");
        mGeneration.add("M6_pre2");
        mGeneration.add("M6");

        gGeneration.add("G5");
        gGeneration.add("G6_pre1");
        gGeneration.add("G6");
    }

    public Generation getGeneration(String version) {
        if (iGeneration.contains(version)) {
            return Generation.I;
        } if (lGeneration.contains(version)) {
            return Generation.L;
        } if (mGeneration.contains(version)) {
            return Generation.M;
        } if (gGeneration.contains(version)) {
            return Generation.G;
        } if (futureGeneration.contains(version)) {
            return Generation.FUTURE;
        }
        return Generation.UNKNOWN;
    }
}
