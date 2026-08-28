package dev.kai.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Login
 *
 * @author Kai
 * @since 8/28/2026
 */
public final record Login(@NotNull UUID uuid, @NotNull String password) { }