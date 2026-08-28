package dev.kai.mode;

import org.jetbrains.annotations.NotNull;

/**
 * Login
 *
 * @author Kai
 * @since 8/28/2026
 */
public final record Login(@NotNull String username, @NotNull String password) { }