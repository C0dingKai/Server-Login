package dev.kai.storage.provider;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.kai.model.LoginHolder;
import dev.kai.storage.DatabaseProvider;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * LoginDatabaseProvider
 *
 * @author Kai
 * @since 8/28/2026
 */
public final class LoginDatabaseProvider implements DatabaseProvider<LoginHolder> {

    private final MongoCollection<Document> collection;

    public LoginDatabaseProvider(final MongoDatabase database) {
        this.collection = database.getCollection("logins");
    }

    @Override
    public void start() {
        collection.createIndex(new Document("uuid", 1));
    }

    @Override
    public void save(final LoginHolder login) {
        final var doc = new Document("uuid", login.uuid().toString())
                .append("password", login.password());

        collection.replaceOne(
                Filters.eq("uuid", login.uuid().toString()),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public @NotNull Optional<LoginHolder> get(final String key) {
        final var doc = this.collection.find(
                Filters.eq("uuid", key)
        ).first();

        if (doc == null) {
            return Optional.empty();
        }

        return Optional.of(new LoginHolder(
                UUID.fromString(doc.getString("uuid")),
                doc.getString("password")
        ));
    }

    @Override
    public @NotNull List<LoginHolder> getAll() {
        final List<LoginHolder> result = new ArrayList<>();

        for (final Document doc : this.collection.find()) {
            result.add(new LoginHolder(
                    UUID.fromString(doc.getString("uuid")),
                    doc.getString("password")
            ));
        }

        return result;
    }

    public void delete(final String uuid) {
        collection.deleteOne(Filters.eq("uuid", uuid));
    }
}