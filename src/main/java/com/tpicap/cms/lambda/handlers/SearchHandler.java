package com.tpicap.cms.lambda.handlers;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tpicap.cms.AppConfig;
import com.tpicap.cms.DBOps;
import com.tpicap.cms.Note;

public class SearchHandler implements RequestHandler<Map<String, String>, List<Note>> {

    @Override
    public List<Note> handleRequest(Map<String, String> event, Context context) {
        try (DBOps db = new DBOps(new AppConfig())) {
            return db.findItems(event.get("UserId"), event.get("text"));
        }
    }
}
