package melancholy.math;

// this is only used in like two places but whatever, i'm lazy
public class Mathm {
    public static float spike(float fin, float peak) {
        if (fin < peak) {
            return fin / peak;
        } else {
            return (1f - fin) / (1f - peak);
        }
    }
}
