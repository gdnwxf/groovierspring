package com.nearinfinity.spring.scripting.support;

import java.io.IOException;
import java.sql.Timestamp;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.StringUtils;

public class DatabaseScriptSource implements ScriptSource {

    private static final Log log = LogFactory.getLog(DatabaseScriptSource.class);

    private String scriptName;
    private Timestamp lastKnownUpdate;
    private JdbcTemplate jdbcTemplate;

    private final Object lastModifiedMonitor = new Object();

    public DatabaseScriptSource(String scriptName, DataSource dataSource) {
        this.scriptName = scriptName;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getScriptAsString() throws IOException {
        log.debug("Find script with name: " + scriptName);

        synchronized (this.lastModifiedMonitor) {
            Timestamp lastUpdated = retrieveLastModifiedTime();
            log.debug("Setting this.lastKnownUpdate to " + lastUpdated);
            this.lastKnownUpdate = lastUpdated;
        }

        return (String) jdbcTemplate.queryForObject(
                "select script_source from groovy_scripts where script_name = ?",
                new Object[]{ scriptName }, String.class);
    }

    public boolean isModified() {
        synchronized (this.lastModifiedMonitor) {
            log.debug("Check if script was modified since last check");

            Timestamp lastUpdated = retrieveLastModifiedTime();

            log.debug("Last update we know about: " + this.lastKnownUpdate);
            log.debug("Last update from database: " + lastUpdated);

            boolean modified = lastUpdated.after(this.lastKnownUpdate);
            log.debug("Was modified? " + modified);

            return modified;
        }
    }

    public String suggestedClassName() {
        String suggestedClassName = StringUtils.stripFilenameExtension(scriptName);
        log.debug("Suggested class name: " + suggestedClassName);
        return suggestedClassName;
    }

    private Timestamp retrieveLastModifiedTime() {
        return (Timestamp) jdbcTemplate.queryForObject("select last_updated from groovy_scripts where script_name = ?",
                new Object[]{ scriptName }, Timestamp.class);
    }

}
