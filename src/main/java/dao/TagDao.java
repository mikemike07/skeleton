package dao;

import api.TagResponse;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String tagName, int recieve_id) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAGNAME, TAGS.RECIEVE_ID)
                .values(tagName, recieve_id)
                .returning(TAGS.ID, TAGS.RECIEVE_ID, TAGS.TAGNAME)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");

        return tagsRecord.getId();

    }

    public List<TagsRecord> getAllReceipts() {
        return dsl.selectFrom(TAGS).fetch();
    }
}
