package com.erbre.appstatus;

import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.erbre.appstatus.entity.SchemaVersionEntity;

import net.sf.appstatus.core.check.AbstractCheck;
import net.sf.appstatus.core.check.CheckResultBuilder;
import net.sf.appstatus.core.check.ICheck;
import net.sf.appstatus.core.check.ICheckResult;

public class FlywayDataBaseCheck extends AbstractCheck implements ICheck {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Inject
    private EntityManager em;

    @Override
    public String getGroup() {
        return "DataBase";
    }

    @Override
    public String getName() {
        return name == null ? "DataBase" : name;
    }

    @Override
    public ICheckResult checkStatus(Locale locale) {
        CheckResultBuilder builder = new CheckResultBuilder();
        builder.from(this);
        try {
            Query query = em.createQuery("Select s from SchemaVersionEntity s order by s.version desc");
            SchemaVersionEntity entity = (SchemaVersionEntity) query.setMaxResults(1).getSingleResult();

            builder.description(String.format("DataBase version[%s] installed at [%tc] last script n°[%d] status[%b]",
                    entity.getVersion(), entity.getInstalledOn(), entity.getInstalledRank(), entity.getSuccess()));
            if (Boolean.TRUE.equals(entity.getSuccess())) {
                builder.code(OK);
            } else {
                builder.code(WARN);
                builder.resolutionSteps(String.format("check script [%s]", entity.getScript()));
            }
        } catch (Exception ex) {
            builder.description(String.format("Unavailable DataBase : Error [%s]", ex.getMessage()));
            builder.code(FATAL);
            builder.resolutionSteps("Check database status");
        }
        return builder.build();
    }
}
