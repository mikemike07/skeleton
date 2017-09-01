package dao;

import api.ReceiptResponse;
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

    public int insert(String tagname, int recieve_id) {
        TagsRecord receiptsRecord = dsl
                .insertInto(TAGS, TAGS.TAGNAME, TAGS.RECIEVE_ID)
                .values(tagname, recieve_id)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<TagsRecord> getAllReceipts() {
        return dsl.selectFrom(TAGS).fetch();
    }
}
