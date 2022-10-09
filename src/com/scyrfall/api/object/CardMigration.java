package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * <p>For the vast majority of Scryfall’s database, Magic card entries are additive. We add new and upcoming cards as we
 * learn about them and obtain images.</p>
 *
 * <p>In rare instances, Scryfall may discover that a card in our database does not really exist, or it has been deleted
 * from a digital game permanently. In these situations, we provide endpoints to help you reconcile downstream data you
 * may have synced or imported from Scryfall.</p>
 */
public class CardMigration extends ScryfallObject {

    private URL url;
    private UUID id;
    private Date createdAt;
    private MigrationStrategy migrationStrategy;
    private UUID oldScryfallId, newScryfallId;
    private String note;

    public CardMigration(JSONObject data) {
        super(data);

        url = getURL("uri");
        id = getUUID("id");
        createdAt = getDate("created_at");
        migrationStrategy = MigrationStrategy.fromString(getString("migration_strategy"));
        oldScryfallId = getUUID("old_scryfall_id");
        newScryfallId = getUUID("new_scryfall_id");
        note = getString("note");
    }

    /**
     * @return  A link to the current object on Scryfall’s API.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return  This migration’s unique UUID.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return  The date this migration was performed.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @return  A computer-readable indicator of the migration strategy.
     * @see MigrationStrategy
     */
    public MigrationStrategy getMigrationStrategy() {
        return migrationStrategy;
    }

    /**
     * @return  The <code>id</code> of the affected API Card object.
     */
    public UUID getOldScryfallId() {
        return oldScryfallId;
    }

    /**
     * @return  The replacement id of the API Card object if this is a <code>merge</code>.
     */
    public UUID getNewScryfallId() {
        return newScryfallId;
    }

    /**
     * @return  A note left by the Scryfall team about this migration.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return Returns a list of all Card Migrations on Scryfall.
     */
    public static CardMigration[] getMigrations() {
        return new ScryfallList(Query.dataFromPath("migrations")).getContents(new CardMigration[0]);
    }

    /**
     * @param id The UUID of the migration to return
     * @return Returns a single Card Migration with the given id
     */
    public static CardMigration getMigration(UUID id) {
        return new CardMigration(Query.dataFromPath("migrations/" + id.toString()));
    }

    public enum MigrationStrategy {
        MERGE, DELETE;

        public static MigrationStrategy fromString(String value) {
            switch (value) {
                case "merge":
                    return MERGE;
                case "delete":
                    return DELETE;
                default:
                    return null;
            }
        }
    }
}
