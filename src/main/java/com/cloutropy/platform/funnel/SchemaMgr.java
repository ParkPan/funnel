package com.cloutropy.platform.funnel;

import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class SchemaMgr {
    private static SchemaMgr ourInstance = new SchemaMgr();
    public static SchemaMgr getInstance() {
        return ourInstance;
    }

    private final static Logger logger = CommonLogger.getLogger();

    private HashMap<String,ArrayList<EventSchema>> schemas;

    public boolean load(String path) {
        schemas = new HashMap<>();
        File file = new File(path);
        if(file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for(File schemaFile : files) {
                if(schemaFile.isDirectory()){
                    continue;
                }
                EventSchema schema = EventSchema.create(schemaFile.getAbsolutePath());
                if(schema == null) {
                    logger.error("op=[createEventSchema],info=[{}]",schemaFile.getAbsoluteFile());
                    return false;
                }
                ArrayList<EventSchema> arr = schemas.get(schema.report_name);
                if(arr == null){
                    arr = new ArrayList<>();
                    schemas.put(schema.report_name,arr);
                }
                arr.add(schema);
            }
            return true;
        }
        return false;
    }

    public ArrayList<EventSchema> getSchema(String report_name) {
        return schemas.get(report_name);
    }

    private SchemaMgr() {

    }
}
