package club.sk1er.patcher.util.enhancement;

public interface Enhancement {
    String getName();

    default void tick() {
    }
}
