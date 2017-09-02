package dao;

import api.TagResponse;
import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
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

    public List<ReceiptsRecord> getSomeTags(String tagname) {

        List <Integer> receiptid = dsl.selectFrom(TAGS).where(TAGS.TAGNAME.eq(tagname)).fetch()
                .stream().map(x->x.getRecieveId()).collect(Collectors.toList());
        System.out.println(receiptid);
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(receiptid)).fetch();
    }
}
