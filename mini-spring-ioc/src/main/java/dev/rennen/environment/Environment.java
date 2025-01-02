package dev.rennen.environment;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:29
 */
public interface Environment extends PropertyResolver{
    String[] getActiveProfiles();

    String[] getDefaultProfiles();

    boolean acceptsProfiles(String... profiles);
}
